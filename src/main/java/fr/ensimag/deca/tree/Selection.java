package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Selection extends AbstractLValue{

	private AbstractExpr left;
	private AbstractExpr right;
	
	public Selection(AbstractExpr left, AbstractExpr right) {
		this.left = left;
		this.right = right;
	}
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		left.decompile(s);
                s.print(".");
                right.decompile(s);
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		left.prettyPrint(s, prefix, false);
                right.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		left.iter(f);
                right.iter(f);
		
	}

}
