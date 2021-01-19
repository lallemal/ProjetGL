package fr.ensimag.deca.tree;

import java.io.PrintStream;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;

public class NewArray extends AbstractExpr{

	final private AbstractIdentifier type;
	final private ListExpr memory;
	
	public NewArray(AbstractIdentifier type, ListExpr memory) {
		this.type = type;
		this.memory = memory;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, true);
		memory.prettyPrint(s, prefix, false);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	
}
