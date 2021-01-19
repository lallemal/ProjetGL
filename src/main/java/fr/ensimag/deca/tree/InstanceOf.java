package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr{
	private final AbstractExpr expr;
	private final AbstractIdentifier ident;
	
	public InstanceOf(AbstractExpr expr, AbstractIdentifier ident) {
		Validate.notNull(expr);
		Validate.notNull(ident);
		this.expr = expr;
		this.ident = ident;
	}
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    Type type1 = expr.verifyExpr(compiler, localEnv, currentClass);
	    Type type2 = ident.verifyType(compiler);
	    if (!type2.isClass() || !type1.isClassOrNull()) {
	    	throw new ContextualError(ContextualError.INSTANCE_OF_IMPOSSIBLE, getLocation());
		}
	    setType(compiler.getBool());
	    return compiler.getBool();
	}

	@Override
	public void decompile(IndentPrintStream s) {
                s.print("(");
		expr.decompile(s);
                s.print(" instanceof ");
                ident.decompile(s);
                s.print(")");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expr.prettyPrint(s, prefix, false);
                ident.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		expr.iter(f);
                ident.iter(f);
		
	}

}
