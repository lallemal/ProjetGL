package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

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
    protected void codeGenInst(DecacCompiler compiler) {
		codeExp(compiler, 2);
    }
	
	@Override
	protected void codeExp(DecacCompiler compiler, int n) {
		if (expr.isIdentifier()) {
			Identifier a = (Identifier) expr;
			int nbParam = ident.getMethodDefinition().getSignature().size();
			compiler.getLabelError().setErrorPilePleine(true);
			compiler.addInstruction(new TSTO(nbParam+1));
			compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
			compiler.incrementKSP(nbParam+1);
			compiler.addInstruction(new ADDSP(nbParam+1));
			compiler.addInstruction(new LOAD(a.getExpDefinition().getOperand(), Register.getR(n)));
			compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(0, Register.SP)));
			int index = 1;
			for (AbstractExpr e : listExpr.getList()) {
				e.codeExp(compiler, n);
				compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(-index, Register.SP)));
				index++;
			}
			compiler.getLabelError().setErrorDereferencementNull(true);
			compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.getR(n)));
			compiler.addInstruction(new CMP(new NullOperand(), Register.getR(n)));
			compiler.addInstruction(new BEQ(compiler.getLabelError().getLabelDereferencementNull()));
			compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(n)), Register.getR(n)));
			compiler.incrementKSP(2);
			compiler.addInstruction(new BSR(new RegisterOffset(ident.getMethodDefinition().getIndex()+1, Register.getR(n))));
			compiler.decrementKSP(2);
			compiler.addInstruction(new SUBSP(nbParam+1));
			compiler.decrementKSP(nbParam+1);
		}
		compiler.addInstruction(new LOAD(Register.R0, Register.getR(n)));
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
		expr.decompile(s);
                s.print(".");
                ident.decompile(s);
                s.print("(");
                listExpr.decompile(s);
                s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
            expr.prettyPrint(s, prefix, false);
            ident.prettyPrint(s, prefix, false);
            listExpr.prettyPrintChildren(s, prefix);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		expr.iter(f);
                ident.iter(f);
                listExpr.iterChildren(f);
	}

}
