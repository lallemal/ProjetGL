package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class Null extends AbstractExpr{
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(n)));
	}
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    return new NullType(null);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null");	
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		//nothing to do 
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// nothing to do 
	}

}
