package fr.ensimag.deca;

import fr.ensimag.deca.codegen.LabelClass;
import fr.ensimag.deca.codegen.LabelError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl40
 * @date 01/01/2021
 */
public class DecacCompiler implements Callable<Boolean> {
	
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    private LabelError labelError;
    private LabelClass labelClass;
    
    public LabelError getLabelError() {
    	return labelError;
    }
    
    public LabelClass getLabelClass() {
    	return labelClass;
    }
    
    private int kGB = 1;
    
    public void incrementKGB() {
    	kGB++;
    }
    
    public void decrementKGB() {
    	kGB--;
    	assert(kGB > 0);
    }
    
    public int getKGB() {
    	return kGB;
    }
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;

        this.symbols = new SymbolTable();
        this.env_types = new EnvironmentType(null);
        createPredefTypes();
        this.rmax = 15;
        this.labelError = new LabelError();
        this.labelClass = new LabelClass();
        
    }
    private int rmax;
    
    public int getRmax() {
    	return rmax;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        rmax = compilerOptions.getrMax()-1;
        String sourceFile = source.getAbsolutePath();
        int longueurSource = sourceFile.length();
        // on enleve le suffixe et on ajoute le nouveau
        // chemin absolu avec .ass a la place
        String destFile =sourceFile.substring(0, longueurSource-4)  +"ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());
        
        if (compilerOptions.getParse()){
            prog.decompile(out);
            LOG.info("the decompilation of the programm is done");
            return false;
        }

        initObject();
        prog.verifyProgram(this);
        if (compilerOptions.getVerif()) {
            LOG.info("Stopping at verification");
            return false;
        }
        assert(prog.checkAllDecorations());

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }
    
    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }



    /* Necessary utilities for compilation */
    private SymbolTable symbols;

    public SymbolTable getSymbols() {
        return symbols;
    }

    EnvironmentType env_types;

    public boolean createPredefTypes() {
        // Create and check existence of symbols for predef Types
        SymbolTable.Symbol voidSymbol = symbols.create("void");
        SymbolTable.Symbol boolSymbol = symbols.create("boolean");
        SymbolTable.Symbol floatSymbol = symbols.create("float");
        SymbolTable.Symbol intSymbol = symbols.create("int");
        SymbolTable.Symbol objectSymbol = symbols.create("Object");

        // Creation of predef types
        try {
            env_types.declare(voidSymbol, new TypeDefinition(new VoidType(voidSymbol), Location.BUILTIN));
            env_types.declare(boolSymbol, new TypeDefinition(new BooleanType(boolSymbol), Location.BUILTIN));
            env_types.declare(floatSymbol, new TypeDefinition(new FloatType(floatSymbol), Location.BUILTIN));
            env_types.declare(intSymbol, new TypeDefinition(new IntType(intSymbol), Location.BUILTIN));

            Signature equalsSig = new Signature();
            equalsSig.add(getBool());
            ClassType objectClass = new ClassType(objectSymbol, Location.BUILTIN, null);
            ClassDefinition objectDef = new ClassDefinition(objectClass, Location.BUILTIN, null);
            objectDef.getMembers().declare(symbols.create("equals"), new MethodDefinition(objectClass, Location.BUILTIN, equalsSig, 0));
            objectDef.setNumberOfMethods(1);
            env_types.declare(objectSymbol, objectDef);

        } catch (EnvironmentType.DoubleDefException e){
            LOG.fatal("Creation of Predefined Type in" + source.getName() + " failed", e);
            return true;
        } catch (EnvironmentExp.DoubleDefException f) {
            LOG.fatal("Creation of equals method for Object in" + source.getName() + " faled", f);
            return true;
        }
        return false;

    }

    public EnvironmentType getEnv_types() {
        return env_types;
    }

    // TODO Check if not exist
    public Type getBuiltInType(String name) {
        return  env_types.get(symbols.create(name)).getType();
    }

    public Type getInt() {
        return env_types.get(symbols.create("int")).getType();
    }
    public Type getFloat() {
        return env_types.get(symbols.create("float")).getType();
    }
    public Type getVoid() {
        return env_types.get(symbols.create("void")).getType();
    }
    public Type getBool() {
        return env_types.get(symbols.create("boolean")).getType();
    }
    public Type getNull() {
        return new NullType(symbols.create("null"));
    }
    public Type getString() {
        return new StringType(symbols.create("string"));
    }

    private DeclClass object;

    public DeclClass initObject() {
        ClassDefinition objectDef = (ClassDefinition)env_types.get(symbols.create("Object"));
        MethodDefinition equalDef = (MethodDefinition) objectDef.getMembers().get(symbols.create("equals"));
        equalDef.setLabel(new Label("code.Object.equals"));
        Identifier objectIdent = new Identifier(symbols.create("Object"));
        objectIdent.setLocation(Location.BUILTIN);
        objectIdent.setDefinition(objectDef);
        ListDeclField objectField = new ListDeclField();
        objectField.setLocation(Location.BUILTIN);
        ListDeclMethod objectMethod = new ListDeclMethod();
        objectMethod.setLocation(Location.BUILTIN);

        Identifier booleanIdent = new Identifier(symbols.create("boolean"));
        booleanIdent.setLocation(Location.BUILTIN);
        booleanIdent.setDefinition(env_types.get(symbols.create("boolean")));
        Identifier equalsIdent = new Identifier(symbols.create("equals"));
        equalsIdent.setLocation(Location.BUILTIN);
        equalsIdent.setDefinition(equalDef);

        ListParam listParamEquals = new ListParam();
        listParamEquals.setLocation(Location.BUILTIN);
        Identifier typeParamIdent = new Identifier(symbols.create("Object"));
        typeParamIdent.setLocation(Location.BUILTIN);
        typeParamIdent.setDefinition(objectDef);
        Identifier nameParamIdent = new Identifier(symbols.create("other"));
        nameParamIdent.setLocation(Location.BUILTIN);
        nameParamIdent.setDefinition(objectDef);
        DeclParam paramObject = new DeclParam(typeParamIdent, nameParamIdent);
        listParamEquals.add(paramObject);

        ListInst listInst = new ListInst();
        listInst.setLocation(Location.BUILTIN);
        This thisBody = new This();
        thisBody.setLocation(Location.BUILTIN);
        Identifier otherBody = new Identifier(symbols.create("other"));
        otherBody.setLocation(Location.BUILTIN);
        otherBody.setDefinition(new ParamDefinition(objectDef.getType(), Location.BUILTIN, 0));
        Equals equalBody = new Equals(thisBody, otherBody);
        equalBody.setLocation(Location.BUILTIN);
        Return returnBody = new Return(equalBody);
        returnBody.setLocation(Location.BUILTIN);
        listInst.add(returnBody);
        ListDeclVar listDeclVar = new ListDeclVar();
        listDeclVar.setLocation(Location.BUILTIN);

        MethodBody equalBodyMethod = new MethodBody(listDeclVar, listInst);
        equalBodyMethod.setLocation(Location.BUILTIN);

        DeclMethod equalMethod = new DeclMethod(booleanIdent, equalsIdent, listParamEquals, equalBodyMethod);
        equalMethod.setLocation(Location.BUILTIN);
        objectMethod.add(equalMethod);
        objectDef.setMethods(objectMethod);
        objectDef.setAddress(new RegisterOffset(1, Register.GB));
        object = new DeclClass(objectIdent, null, objectField, objectMethod);
        object.setLocation(Location.BUILTIN);

        objectIdent.setDefinition(env_types.get(symbols.create("Object")));
        return object;
    }

    public DeclClass getObject() {
        return object;
    }

    @Override
    public Boolean call() throws Exception {
        return this.compile();
    }
}
