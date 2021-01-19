package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Selection extends AbstractLValue{

	private AbstractExpr expr;
	private AbstractIdentifier ident;
	
	@Override
	public void codeExp(DecacCompiler compiler, int n) {
		expr.codeExp(compiler, n);
		//TODO
	}
	
	public Selection(AbstractExpr left, AbstractIdentifier right) {
		Validate.notNull(left);
		Validate.notNull(right);
		this.expr = left;
		this.ident = right;
	}
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    Type typeClass2 = expr.verifyExpr(compiler, localEnv, currentClass);
	    if (!typeClass2.isClass()) {
	    	throw new ContextualError(ContextualError.SELECTION_EXPR_NOT_CLASS, getLocation());
		}
	    EnvironmentExp classExp2 = ((ClassDefinition) compiler.getEnv_types().get(typeClass2.getName())).getMembers();
		Type fieldType = ident.verifyExpr(compiler, classExp2, currentClass);
		FieldDefinition fieldDef = ident.getFieldDefinition();
		// 3.65
		if (fieldDef.getVisibility() == Visibility.PUBLIC) {
			return fieldType;
		}
		// 3.66
		if (currentClass == null || !TypeOp.subType(compiler, currentClass.getType(), typeClass2)) {
			throw new ContextualError(ContextualError.CLASS_NOT_SUBCLASS_PROTECTED, getLocation());
		}
		if  (!TypeOp.subType(compiler, fieldDef.getContainingClass().getType(), currentClass.getType())) {
			throw new ContextualError(ContextualError.FIELD_NOT_OVERCLASS_PROTECTED, getLocation());
        }
		return fieldType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		expr.decompile(s);
                s.print(".");
                ident.decompile(s);
		
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
