package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Cast extends AbstractUnaryExpr{
	private final AbstractIdentifier ident;
	public Cast(AbstractIdentifier ident, AbstractExpr expr) {
		super(expr);
		this.ident = ident;
	}
	@Override
	protected String getOperatorName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

}
