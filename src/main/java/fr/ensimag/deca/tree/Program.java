package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.Line;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        // Passe 1 : Initialize classes & their definitions
        classes.verifyListClass(compiler);

        // Passe 2 :
        classes.verifyListClassMembers(compiler);

        // Passe 3
        // Descente vers classe inutile pour sans objet
        classes.verifyListClassBody(compiler);
        // Pour Main
        main.verifyMain(compiler);
        // LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
    	compiler.addComment("----------------------------------------------");
		compiler.addComment("	Construction des tables des methodes");
		compiler.addComment("----------------------------------------------");
        classes.codeGenDeclClass(compiler);
        compiler.addComment("----------------------------------------------");
		compiler.addComment("		Main program ");
		compiler.addComment("----------------------------------------------");
        main.codeGenMain(compiler);
        if (compiler.getMaxSP()>1) {
        	compiler.addFirst(new Line(new ADDSP(compiler.getKGB()-1)));
        	compiler.getLabelError().setErrorPilePleine(true);
        	if (!compiler.getCompilerOptions().isNoCheck()) {
	        	compiler.addFirst(new Line(new BOV(compiler.getLabelError().getLabelPilePleine())));
	        	compiler.addFirst(new Line(new TSTO(compiler.getMaxSP()-1)));
        	}
        }
        compiler.addInstruction(new HALT());
        compiler.getLabelClass().codeGenLabelClass(compiler, classes);
        compiler.getLabelError().codeGenLabelError(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
