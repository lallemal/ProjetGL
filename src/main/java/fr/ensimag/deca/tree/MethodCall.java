package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
	
	private AbstractExpr expr;
	private AbstractIdentifier ident;
	private ListExpr listExpr;

	public MethodCall(AbstractExpr expr, AbstractIdentifier ident, ListExpr listExpr) {
		Validate.notNull(expr);
		Validate.notNull(ident);
		Validate.notNull(listExpr);
		this.expr = expr;
		this.ident = ident;
		this.listExpr = listExpr;
	}
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type classType2 = expr.verifyExpr(compiler, localEnv, currentClass);
		if (!classType2.isClass()) {
			throw new ContextualError(ContextualError.CLASS_NOT_CLASS, getLocation());
		}
		ClassDefinition classDef = (ClassDefinition) compiler.getEnv_types().get(classType2.getName());
		Type methodType = ident.verifyMethod(compiler, classDef.getMembers());
		Signature sig = ident.getMethodDefinition().getSignature();
		listExpr.verifyRvalueStar(compiler, localEnv, currentClass, sig);
		setType(methodType);
		return methodType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
            expr.prettyPrint(s, prefix, false);
            ident.prettyPrint(s, prefix, false);
            listExpr.prettyPrintChildren(s, prefix);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

}
