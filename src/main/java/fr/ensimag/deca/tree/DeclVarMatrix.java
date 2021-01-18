package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclVarMatrix extends AbstractDeclVar {

	final private AbstractIdentifier type;
	final private AbstractIdentifier varName;
    final private Initialization initialization;

    public DeclVarMatrix(AbstractIdentifier type, AbstractIdentifier varName, Initialization initialization) {
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
    
	@Override
	protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub

	}

	@Override
	protected void codeGenDecl(DecacCompiler compiler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub

	}

}
