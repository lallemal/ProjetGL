package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class New extends AbstractUnaryExpr{
	
	public New(AbstractExpr operand) {
		super(operand);
	}

	@Override
	protected String getOperatorName() {
		return "new";
	}
        
        @Override
        public void decompile(IndentPrintStream s){
            s.print("new ");
            this.getOperand().decompile(s);
        }

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

}
