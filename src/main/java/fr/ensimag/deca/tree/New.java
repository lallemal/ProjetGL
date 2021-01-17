package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class New extends AbstractExpr {

	private final AbstractIdentifier ident;
	
	public New(AbstractIdentifier operand) {
		Validate.notNull(operand);
	    this.ident = operand;
	}
   

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type type = ident.verifyType(compiler);
		if (!type.isClass()) {
			throw new ContextualError(ContextualError.TYPE_NOT_CLASS, getLocation());
		}
		return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("new " + ident.decompile() +"()");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		ident.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		ident.iter(f);
	}
}
