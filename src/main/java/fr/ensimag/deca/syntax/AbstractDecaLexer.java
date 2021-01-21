package fr.ensimag.deca.syntax;

import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.LocationException;

/**
 * This is the super class for the lexer. It is extended by the lexer class
 * generated from DecaLexer.g.
 * 
 * @author gl40, Based on template by Jim Idle - Temporal Wave LLC
 *         (jimi@idle.ws)
 * @date 01/01/2021
 */
public abstract class AbstractDecaLexer extends Lexer {
    private static final Logger LOG = Logger.getLogger(AbstractDecaLexer.class);

    private DecacCompiler decacCompiler;
    private File source;

    public void setSource(File source) {
        this.source = source;
    }

    protected DecacCompiler getDecacCompiler() {
        return decacCompiler;
    }

    public void setDecacCompiler(DecacCompiler decacCompiler) {
        this.decacCompiler = decacCompiler;
    }

    /**
     * If e is linked to an input stream, return the source name for this
     * stream. Otherwise (e.g. user-defined RecognitionException that do not set
     * the input field), return the source name for this Lexer.
     */
    public String getSourceName(RecognitionException e) {
        final IntStream inputStream = e.getInputStream();
        if (inputStream != null) {
            return inputStream.getSourceName();
        } else {
            return getSourceName();
        }
    }

    /**
     * Display the list of tokens for the lexer in semi-human-readable form.
     * 
     * This consumes the stream of tokens, hence should never be called if the
     * parser has to read these tokens afterwards.
     * 
     * @return true if the lexer raised an error.
     */
    @SuppressWarnings("deprecation")
    public boolean debugTokenStream() {
        CommonTokenStream tokens = new CommonTokenStream(this);
        Token t;
        int i = 0;
        try {
            while ((t = tokens.LT(++i)).getType() != Token.EOF) {
                System.out.println(DecaParser.tokenNames[t.getType()] + ": " + t);
            }
        } catch (ParseCancellationException e) {
            if (e.getCause() instanceof LocationException) {
                ((LocationException)e.getCause()).display(System.err);
            }
            return true;
        } catch (DecaRecognitionException e) {
            new LocationException(e.getMessage(), e.getLocation()).display(System.err);
            return true;
        }
        return false;
    }

    /**
     * Default constructor for the lexer.
     */
    public AbstractDecaLexer(CharStream input) {
        super(input);
        removeErrorListeners();
        addErrorListener(new DecacErrorListner(input));
    }

    /**
     * Helper for test drivers, that creates a lexer from command-line
     * arguments.
     * 
     * @param args
     *            Either empty (read from stdin), or 1-element array (the file
     *            to read from)
     * @return The lexer built from args
     * @throws IOException
     */
    public static DecaLexer createLexerFromArgs(String[] args)
            throws IOException {
        Validate.isTrue(args.length <= 1, "0 or 1 argument expected.");
        DecaLexer lex;
        if (args.length == 1) {
            lex = new DecaLexer(CharStreams.fromFileName(args[0]));
            lex.setSource(new File(args[0]));
        } else {
            System.err.println("Reading from stdin ...");
            lex = new DecaLexer(CharStreams.fromStream(System.in));
        }
        return lex;
    }

    protected File getSource() {
        if (getDecacCompiler() != null
                && getDecacCompiler().getSource() != null) {
            return getDecacCompiler().getSource();
        } else {
            return source;
        }
    }

    // Code needed to implement the #include directive.
    // Adapted from https://theantlrguy.atlassian.net/wiki/pages/viewpage.action?pageId=2686987
    private static class IncludeSaveStruct {
        IncludeSaveStruct(CharStream input, int line, int charPositionInline) {
            this.input = input;
            this.line = line;
            this.charPositionInLine = charPositionInline;
        }

        /** Which stream to read from */
        public CharStream input;
        /** Where in the stream was the <code>#include</code> */
        public int line, charPositionInLine;
    }

    private final Stack<IncludeSaveStruct> includes = new Stack<IncludeSaveStruct>();

    /**
     * Look up the file to include in the current directory, or in the
     * $CLASSPATH (either a file or the content of a .jar file).
     * 
     * @return An ANTLR stream to read from
     * @throws IOException
     *             when the file was found but could not be opened
     * @throws IncludeFileNotFound
     *             when the file was not found.
     */
    CharStream findFile(String name) throws IOException,
            IncludeFileNotFound {
        // Look in the directory containing the source file ...
        String dir = "."; // default value used e.g. when reading from stdin
        File src = getSource();
        if (src != null && src.getParent() != null) {
            dir = src.getParent();
        }
        String full = dir + "/" + name;
        File f = new File(full);
        if (f.exists()) {
            LOG.debug("Using local file " + full);
            return CharStreams.fromFileName(full);
        }

        // ... and fall back to the standard library path if not found.
        final URL url = ClassLoader.getSystemResource("include/" + name);
        if (url != null) {
            LOG.debug("Using library " + url);
            // Use fromReader(Reader, String) to catch the file name --- fromStream(InputStream) does not.
            return CharStreams.fromReader(new InputStreamReader(url.openStream()), url.getFile());
        }

        throw new IncludeFileNotFound(name, this, getInputStream()); // TODO: check this
    }

    /**
     * Apply a <code>#include</code> directive.
     * 
     * Look up the file "name" using {@link #findFile(String)}, and set the
     * input stream of the lexer to this object. The previous input stream is
     * saved an {@link #includes} and will be restored by {@link #nextToken()}.
     * 
     * @throws IncludeFileNotFound
     *             When the file could not be found or opened.
     * @throws CircularInclude
     *             When an attempt to perform a circular inclusion is done
     */
    void doInclude(String includeDirective) throws IncludeFileNotFound, CircularInclude {
        String name = includeDirective.substring(includeDirective.indexOf('"') + 1,
                includeDirective.lastIndexOf('"'));
        Validate.notNull(name);
        Validate.notEmpty(name);
        CharStream newInput;
        try {
            newInput = findFile(name);
        } catch (IOException e1) {
            // The file is probably there but not readable.
            throw new IncludeFileNotFound(name, this, getInputStream());
        }
        for (IncludeSaveStruct s : includes) {
            if (newInput.getSourceName().equals(s.input.getSourceName())) {
                throw new CircularInclude(name, this, this.getInputStream());
            }
        }
        IncludeSaveStruct ss = new IncludeSaveStruct(getInputStream(),
                getLine(), getCharPositionInLine());
        includes.push(ss);
        setInputStream(newInput);
        throw new SkipANTLRPostAction();
    }

    /**
     * Exception used to skip ANTLR code from doInclude to nextToken().
     *
     * The normal call stack looks like:
     * <code>
     * nextToken()
     *  `-> FailOrAccept
     *      +-> LexerActionExecute
     *      |   `-> doInclude()
     *      |        `-> setInputStream() -> reset()
     *      `-> return return prevAccept.dfaState.prediction
     * </code>
     * Unfortunately, reset() sets dfaState to null hence this crashes.
     *
     * Instead, use this exception to skip the "return" call, and catch the
     * control back in nextToken().
     */
    private static class SkipANTLRPostAction extends RuntimeException {
        private static final long serialVersionUID = 6114145992238256449L;
    }

    /**
     * Override method nextToken for <code>#include</code> management.
     * @return the next Token which is read in an included files on
     *    a <code>#include</code>
     */
    @Override
    @SuppressWarnings("InfiniteRecursion")
    public Token nextToken() {
        Token token;
        try {
            token = super.nextToken();
        } catch (SkipANTLRPostAction e) {
            // It's OK, we just found a #include statement.
            return this.nextToken();
        }

        if (token.getType() == Token.EOF && !includes.empty()) {
            // We've got EOF and have non empty stack.
            LOG.debug("End of file, poping include stack");
            IncludeSaveStruct ss = includes.pop();
            setInputStream(ss.input);
            setLine(ss.line);
            setCharPositionInLine(ss.charPositionInLine);

            // this should be used instead of super [like below] to
            // handle exits from nested includes. It matters, when the
            // 'include' token is the last in previous stream (using
            // super, lexer 'crashes' returning EOF token)
            token = this.nextToken();
        }

        // Skip first token after switching on another input.
        // You need to use this rather than super as there may be nested include
        // files
        if (((CommonToken) token).getStartIndex() < 0) {
            token = this.nextToken();
        }

        return token;
    }
    /* Ajout de methode pour les debordements*/
    
    // nombre maximum entier signé positif sur 32 bits => un bit de signe 
    //le maximum est donc 2^31-1 donc 2147483647 on va comparer les chaine de caractere
    // des deux nombres
   

 	/* Fonction  associee si le float est un decimal*/

 	// valeur minimale = 1.4012985E-45
 	// valeur maximale = 3,402 823 5 × 10E38.
 	void isDecimal(String stringdec)throws DecaRecognitionException{
 		// System.out.println("est une decimal");
 		if(stringdec.charAt(0) == '0') {
 			beginZeroDec(stringdec);
 		}
 		else {
 			beginNotZeroDec(stringdec);
 		}

 	}


 	void beginZeroDec(String stringzero)throws DecaRecognitionException{
 		//System.out.println("dec commence par 0"); 
 		boolean debutmantisse = false;
 		int exposant = -1;
 		String mantisse = "";
 		for (int i = 2; i < stringzero.length(); i++){
 			switch(stringzero.charAt(i)) {
 			// fin du float
 			case 'f':
 				break;
 			case 'F':
 				break;
 			case 'e':
 				exposantFlottantDec(mantisse, exposant, stringzero.substring(i+1, stringzero.length()));
 				return;
 			case 'E':
 				exposantFlottantDec(mantisse, exposant, stringzero.substring(i+1, stringzero.length()));
 				return;
 			case '0':
 				if(!debutmantisse) {
 					exposant -= 1;
 				}
 				else {
 					mantisse += '0';
 				}
 				break;
 			default:
 				mantisse += stringzero.charAt(i);
 				debutmantisse = true;
 			}
 		}
 		// cas 0.000...
 		if (mantisse == "") {
 			return;
 		}
 		// deux premier cas le de la fonction a traiter en exemple
 		compareExposantDec(exposant, mantisse);

 	}

 	void beginNotZeroDec(String stringzero){
 		//System.out.println("dec ne commence pas par 0");
 		// ajoute exposant en plus exemple: 100.0 = 1.0 E2
 		boolean findot = false;
 		String mantisse = "";
 		int exposant = -1;
 		for (int i = 0; i < stringzero.length(); i++) {
 			switch(stringzero.charAt(i)) {
 			case 'f':
 				break;
 			case 'F':
 				break;
 			case 'e':
 				exposantFlottantDec(mantisse, exposant, stringzero.substring(i+1));
 				return;
 			case 'E':
 				exposantFlottantDec(mantisse, exposant, stringzero.substring(i+1));
 				return;
 			case '.':
 				findot = true;
 				break;
 			default:
 				mantisse += stringzero.charAt(i);
 				if(!findot) {
 					exposant++;
 				}
 				break;
 			}
 		}
 		compareExposantDec(exposant, mantisse);
 	}

 	// valeur minimale = 1.4012985E-45
 	// valeur maximale = 3.402 823 46 E38
 	void compareMantisseminDec(String mantisse){

 		String min = "14012985";
 		int longueur = minimum(mantisse.length(), min.length());

 		for (int i = 0; i< longueur; i++) {
 			int mininum = min.charAt(i);
 			int mantissei = mantisse.charAt(i);
 			if (mantissei < mininum) {
    		  throw new InvalidFloatSmall(this, getInputStream());
 			}
 		}
 		// mantisse plus longue donc >= min donc pas d'erreur de debordement

 	}

 	void compareManthissemaxDec(String mantisse){
 		String max =  "34028235";
 		int longueur = minimum(mantisse.length(),max.length());

 		for( int i = 0; i < longueur; i ++) {
 			int maximum =max.charAt(i);
 			int mantissei = mantisse.charAt(i);
 			if(maximum < mantissei) {
 				throw new InvalidFloatBig(this, getInputStream());
 			}
 		}
 		// mantisse plus longue mais que avec des 0 a la fin donc egaux sinon erreur
 		if (mantisse.length() > max.length()) {
 			for (int j = max.length(); j < mantisse.length(); j++ ) {
 				if(mantisse.charAt(j) != '0') {
 					throw new InvalidFloatBig(this, getInputStream());
 				}
 			}
 		}
 	}

 	void exposantFlottantDec(String mantisse, int exposant, String charexposant){
 		int expofinal = exposant;
 		char signe = '+';
 		String exposantsupp = "";
 		for (int i = 0; i< charexposant.length(); i++) {
 			switch (charexposant.charAt(i)) {
 			case '-':
 				signe = '-';
 				break;
 			case '+':
 				break;
 			case 'f':
 				break;
 			case 'F':
 				break;
 			default:
 				exposantsupp += charexposant.charAt(i);
 			}
 		}

 		// calcul de l'exposant
 		expofinal += calculExposant(signe,exposantsupp);
 		compareExposantDec(expofinal, mantisse);
 	}

 	int minimum(int i, int j) {
 		if (i>j) {
 			return j; 
 		}
 		else {
 			return i;
 		}
 	}

 	void compareExposantDec(int exposant, String mantisse){
 		//System.out.println("exposant =" + exposant + " mantisse =" + mantisse );
 		if (exposant< -45) {
 			throw new InvalidFloatSmall(this, getInputStream());

 		}

 		// cas limite il faut comparer mantisse
 		if(exposant== -45) {
 			compareMantisseminDec(mantisse);
 		}
 		if(exposant== 38) {
 			compareManthissemaxDec(mantisse); 
 		}
 		if (exposant> 38) {
 			throw new InvalidFloatBig(this, getInputStream());
 		}
 	}

 	int calculExposant(char signe,String exposant) {
 		int expo;
 		expo = Integer.parseInt(exposant);
 		if (signe == '-') {
 			expo = expo * (-1);
 		}
 		return expo; 
 	} 
 	/* fonctions permettant de calculer les debordements si c'est en hexadecimal*/

 	// le p signifie puissance de 2
 	// exemple: 0xff.0p19   // this is 255.0 x 2^19 
 	// exposant  de 2 entre -126 et 127
 	// min pour exposant = -126 mantisse = 1 
 	// min = 0x1.0p-126
 	// mantisse max = 111 1111 1111 1111 1111 1111 
 	//              = 7FFFFF
 	// exposant = 127 max = 0x1.FFFFFE * 2^127

 	// calculer exposant et mantisse tel que un seul chiffre apres la virgule
 	// ex: 10.23p1 = 1.023p5 

 	void isHexadecimal(String stringhex){
 		if(stringhex.charAt(2) == '0') {
 			// on skip 0x0.
 			beginZeroHex(stringhex.substring(4));
 		}
 		else {
 			beginNonZeroHex(stringhex.substring(2));
 		}
 	}

 	void beginZeroHex(String s) {
 		String mantisse = "";
 		int exposant = -4;
 		boolean debutmantisse = false;
 		for( int i = 0; i < s.length(); i++) {
 			switch(s.charAt(i)) {
 			case 'p':
 				exposantFlottantHex(mantisse, exposant, s.substring(i+1));
 				return;
 			case 'P':
 				exposantFlottantHex(mantisse, exposant, s.substring(i+1));
 				return;
 			case '0':
 				if(!debutmantisse) {
 					exposant-= 4;
 				}
 				else {
 					mantisse += s.charAt(i);
 				}
 				break;
 			default:
 				mantisse += s.charAt(i);
 				debutmantisse = true;
 				break;
 			}
 		}
 		compareExposantHex(exposant, mantisse);
 	}


 	void beginNonZeroHex(String s) {

 		// ajoute exposant en plus exemple: 100.0 = 1.0 E2
 		boolean findot = false;
 		String mantisse = "";
 		int exposant = -1;
 		for (int i = 0; i < s.length(); i++) {
 			switch(s.charAt(i)) {
 			case 'p':
 				exposantFlottantHex(mantisse, exposant, s.substring(i+1));
 				return;
 			case 'P':
 				exposantFlottantHex(mantisse, exposant, s.substring(i+1));
 				return;
 			case '.':
 				findot = true;
 				break;
 			default:
 				mantisse += s.charAt(i);
 				if(!findot) {
 					if(exposant == -1) {
 						exposant ++;
 					}
 					else {
 						exposant += 4;
 					}
 				}
 				break;
 			}
 		}
 		//compareExposantHex(exposant, mantisse); 
 	}

 	void exposantFlottantHex(String mantisse, int exposant ,String charexposant) {
 		int expofinal = exposant;
 		char signe = '+';
 		String exposantsupp = "";
 		for (int i = 0; i< charexposant.length(); i++) {
 			switch (charexposant.charAt(i)) {
 			case '-':
 				signe = '-';
 				break;
 			case '+':
 				break;
 			case 'f':
 				break;
 			case 'F':
 				break;
 			default:
 				exposantsupp += charexposant.charAt(i);
 			}
 		}

 		// calcul de l'exposant
 		expofinal += calculExposant(signe,exposantsupp);
 		compareExposantHex(expofinal, mantisse);
 	}

 	void compareExposantHex(int exposant, String mantisse) {
 		//System.out.println("exposant= " + exposant + " mantisse= "+ mantisse);
 		if(exposant < -126) {
 			throw new InvalidFloatSmall(this,getInputStream());
 		}
 		// si l'exposant == 126 OK
 		//if(exposant == -126) {  
 		//	compareMantisseMinHex(mantisse);
 		//}

 		if (exposant == 127) {
 			compareMantisseMaxHex(mantisse);
 		}

 		if (exposant >127) {
 			throw new InvalidFloatBig(this,getInputStream());
 		}

 	}

/*
 	void compareMantisseMinHex(String mantisse){
 		// comparer a 100000 il faut que la mantisse soit superieur lors du parcours
 		if(mantisse.charAt(0) != '1'){
 			System.err.println("arrondi a zero et flottant non nulle");
 		}
 		for(int i = 1; i< mantisse.length(); i++) {
 			if(mantisse.charAt(i) != '0') {
 				System.err.println("arrondi a zero et flottant non nulle");
 			}
 		}
 	}
*/
 	void compareMantisseMaxHex(String mantisse) {
 		// caractere asci 0..9 de 48 à 57
 		//				   A..F de 65 a 70
 		//			       a..f de 97 a 102
 		String mantissecomp = "17fffff";
 		int lengthmin = minimum(mantisse.length(), mantissecomp.length());
 		for(int i = 0; i < lengthmin; i++ ){
 			int max = mantissecomp.charAt(i);
 			int mantissen = mantisse.charAt(i);
 			if(max < mantissen) {
 				throw new InvalidFloatBig(this, getInputStream());
 			}
 		}
 		// si la longueur de la mantisse est la plus eleve et meme valeur verifier nombre apres

 		if(mantisse.length() > lengthmin) {
 			for( int j = lengthmin; j < mantisse.length(); j++) {
 				if (mantisse.charAt(j) != '0') {
 					throw new InvalidFloatBig(this, getInputStream());
 				}
 			}
 		}
 	}	
      

}
