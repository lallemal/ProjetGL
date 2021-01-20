package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Cast extends AbstractExpr{

	private final AbstractIdentifier ident;
	private final AbstractExpr expression;
	public Cast(AbstractIdentifier ident, AbstractExpr expr) {
	    Validate.notNull(ident);
		Validate.notNull(expr);
		this.expression = expr;
		this.ident = ident;
	}


	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    Type type = ident.verifyType(compiler);
	    Type type2 = expression.verifyExpr(compiler, localEnv, currentClass);
	    if (!TypeOp.castComp(compiler, type2, type)) {
	    	throw new ContextualError(ContextualError.CAST_INCOMPATIBLE, getLocation());
		}
	    setType(type);
	    return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
                s.print("(");
                ident.decompile(s);
                s.print(")(");
                expression.decompile(s);
                s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
                ident.prettyPrint(s, prefix, false);
                expression.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
                ident.iter(f);
                expression.iter(f);
	}
}
