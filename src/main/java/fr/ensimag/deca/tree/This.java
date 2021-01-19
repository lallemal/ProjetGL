package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class This extends AbstractExpr {
	
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(n)));
	}
    
    @Override
    public boolean isThis() {
    	return true;
    }

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    if (currentClass == null) {
	    	throw new ContextualError(ContextualError.THIS_CLASS_NULL, getLocation());
		}
	    Type type = currentClass.getType();
	    setType(type);
	    return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("this");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix){
		//Nothing to do
	}

	@Override
	protected void iterChildren(TreeFunction f) {
	    // Nothing to do
	}

}
