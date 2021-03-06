package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
	public void codeExp(DecacCompiler compiler, int n) {
		Type T = ident.getType();
		if (T.isInt()) {
			if (expression.getType().isInt()) {
				expression.codeExp(compiler, n);
			} else if (expression.getType().isFloat()) {
				expression.codeExp(compiler, n);
				compiler.getLabelError().setErrorConvInt(true);
				compiler.addInstruction(new INT(Register.getR(n), Register.getR(n)));
				if (!compiler.getCompilerOptions().isNoCheck()) {
					compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorINT()));
				}
			}
		} else if (T.isFloat()) {
			if (expression.getType().isFloat()) {
				expression.codeExp(compiler, n);
			} else if (expression.getType().isInt()) {
				expression.codeExp(compiler, n);
				compiler.getLabelError().setErrorConvFloat(true);
				compiler.addInstruction(new FLOAT(Register.getR(n), Register.getR(n)));
				if (!compiler.getCompilerOptions().isNoCheck()) {
					compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorFLOAT()));
				}
			}
		} else if (T.isBoolean()) {
			if (expression.getType().isBoolean()) {
				expression.codeExp(compiler, n);
			}
		} else if (T.isClass()) {
			if (expression.getType().isNull()) {
				expression.codeExp(compiler, n);
			} else if (expression.getType().isClass()) {
				
				InstanceOf testInstanceOf = new InstanceOf(expression, ident);
				compiler.getLabelError().setErrorCastFail(true);
				testInstanceOf.setLocation(getLocation());
				testInstanceOf.codeExp(compiler, n);
				compiler.addInstruction(new CMP(0, Register.getR(n)));
				compiler.addInstruction(new BEQ(compiler.getLabelError().getLabelCastFail()));
				expression.codeExp(compiler, n);
			}
		}
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
