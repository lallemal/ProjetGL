package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;

public class NewArray extends AbstractInitialization{

	final private AbstractIdentifier type;
	final private int length;
	
	public NewArray(AbstractIdentifier type, int length) {
		this.type = type;
		this.length = length;
	}

	@Override
	protected void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv,
			ClassDefinition currentClass) throws ContextualError {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
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
