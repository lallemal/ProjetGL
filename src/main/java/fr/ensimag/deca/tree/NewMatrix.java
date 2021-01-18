package fr.ensimag.deca.tree;

import java.io.PrintStream;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;

public class NewMatrix extends AbstractInitialization {

	final private AbstractIdentifier type;
	final private int nbLine;
	final private AbstractInteger nbColumn;
	
	public NewMatrix(AbstractIdentifier type, int nbLine, AbstractInteger nbColumn) {
		this.type = type;
		this.nbLine= nbLine;
		this.nbColumn = nbColumn;
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
