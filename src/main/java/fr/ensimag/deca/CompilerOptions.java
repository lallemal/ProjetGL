package fr.ensimag.deca;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public boolean getParse(){
        return parse;
    }

	public int getrMax() {
		return rMax;
	}

	public boolean getVerif() {return verif;}
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verif = false;
    private int rMax = 16;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
    	
    	// pas d'arguments, affiche les commandes disponibles
    	if (args.length == 0) {
    		return;
    	}
    	// -b affiche numero d'equipe
    	if(args.length == 1 && args[0].equals("-b") ) {
    		printBanner = true;
    		return;
    	}
    	
    	// analyse les options et les fichiers doit comporter au moins un fichier
    	for (int i = 0; i < args.length; i++) {
    		switch(args[i]) {
    			case "-p":
    				if (verif) {
    					throw new CLIException("parse option is not compatible with verification option");
					}
    				parse = true;
					break;
    			case "-v":
    				if (parse) {
    					throw new CLIException("verification option is not compatible with parse option");

					}
    			case "-n":
    				throw new UnsupportedOperationException("no check is not yet implemented");
    			case "-r":
    			    if (i+1 < args.length) {
    			    	int rmax = Integer.parseInt(args[i+1]);
    			    	if (4 <= rmax  && rmax <= 16 ) {
							rMax = rmax;
						} else {
    			    		throw new CLIException("number of maximum registers has to be between 4 and 16");
						}
					} else {
    			    	throw new CLIException("-r has to be followed by the maximum number of registers usable");
					}
    			case "-d":
    				if (debug == TRACE) {
    					throw new CLIException("Too much -d option : This level of debugging does not exist");
					}
    				debug++;
    				break;
    			case "-P":
    				parallel = true;
    				break;
    			case "-w":
    				throw new UnsupportedOperationException("warning is not yet implemented");
    		
    			// est peut être un fichier
    			default:
    				traiteFichier(args[i]);
    				break;
    		}
    	}
    	
    	if (sourceFiles.isEmpty()) {
    		throw new CLIException("pas de fichier en entrée");
    	}
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
        
        //throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {

    	System.out.println("Syntaxe d'utilisation a respecter sinon ne passe pas les tests automatises");
    	System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
    	System.out.println(".-b (banner)       : affiche une bannière indiquant le nom de l’équipe");
    	System.out.println(".-p (parser)       : arrête decac après l’étape de construction de");
    	System.out.println("                     l’arbre, et affiche la décompilation de ce dernier");
    	System.out.println("                     (i.e. s’il n’y a qu’un fichier source à");
    	System.out.println("                     compiler, la sortie doit être un programme");
    	System.out.println("                     deca syntaxiquement correct)");
    	System.out.println(".-v (verification) : arrête decac après l’étape de vérifications");
    	System.out.println("                     (ne produit aucune sortie en l’absence d’erreur)");
    	System.out.println(".-n (no check)     : supprime les tests de débordement à l’exécution");
    	System.out.println("                     - débordement arithmétique");
    	System.out.println("                     - débordement mémoire");
    	System.out.println("                     - déréférencement de null");
    	System.out.println(".-r X              : limite les registres banalisés disponibles à");
    	System.out.println("                     -R0 ... R{X-1}, avec 4 <= X <= 16");
    	System.out.println(".-d (debug)        : active les traces de debug. Répéter");
    	System.out.println("                     l’option plusieurs fois pour avoir plus de traces.");
    	System.out.println(".-P (parallel)     : s’il y a plusieurs fichiers sources,");
    	System.out.println("                     lance la compilation des fichiers en");
    	System.out.println("                     parallèle (pour accélérer la compilation)");
    	
        //throw new UnsupportedOperationException("not yet implemented");
    }
    
   // test si nom de fichier valide ou pas
    private void traiteFichier(String fichier)throws CLIException{
    	int longueur = fichier.length();
    	// longueur trop petit pour que ce soit un fichier
    	if (longueur < 6) {
    		throw new CLIException("mauvais nom de fichier");
    	}
    	// mauvais nom de fichier (pas verifier avec caractere speciaux )
    	String suffixe = fichier.substring(longueur-5, longueur);
    	if (!suffixe.equals(".deca")) {
    		throw new CLIException("mauvais nom de fichier");
    	}	
    	//System.out.println("suffixeOK");
    	File f = new File(fichier);
    	if(!f.exists()) {
    		throw new CLIException("Fichier introuvable");	
    	}
    	else {
    		ajouteFichier(f);
    	}
    }
    
    private void ajouteFichier(File f) {
    	if(!sourceFiles.contains(f)) {
    		sourceFiles.add(f);
    		System.out.println(getSourceFiles());
    	}
    }  
    
  }
