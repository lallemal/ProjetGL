package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.RegisterOffsetRegister;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class ArraySelection extends AbstractLValue{

	final private AbstractIdentifier ident;
	final private ListExpr memory; 
	public ArraySelection(AbstractIdentifier ident, ListExpr memory) {
		Validate.notNull(ident);
		Validate.notNull(memory);
		this.ident = ident;
		this.memory = memory;
	}

	@Override
	protected void codeExp(DecacCompiler compiler, int n) {
		compiler.getLabelError().setErrorIndexOutOfRange(true);
	    compiler.addInstruction(new LOAD(ident.getExpDefinition().getOperand(), Register.getR(n)));
	    compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(n)), Register.getR(n+1)));
	    compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(n+2)));
	    compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.getR(n+3)));
		for (int i = memory.size() - 1;  i >= 0 ; i--) {
			AbstractExpr expr = memory.getList().get(i);
			expr.codeExp(compiler, n+4);
			compiler.addInstruction(new CMP(new RegisterOffset(i, Register.getR(n+1)), Register.getR(n+4)));
			compiler.addInstruction(new BGE(compiler.getLabelError().getLabelIndexOutOfRange()));
			compiler.addInstruction(new MUL(Register.getR(n+3), Register.getR(n+4)));
			compiler.addInstruction(new ADD(Register.getR(n+4), Register.getR(n+2)));
			compiler.addInstruction(new MUL(new RegisterOffset(i, Register.getR(n+1)), Register.getR(n+3)));
		}
		compiler.addInstruction(new LEA(new RegisterOffsetRegister(1, Register.getR(n), Register.getR(n+2)),Register.getR(n)));
	}


	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		codeExp(compiler, 1);
		if (getType().isInt()) {
			compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(1)), Register.R1));
			compiler.addInstruction(new WINT());

		}

	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type typeToCheck = ident.verifyExpr(compiler, localEnv, currentClass);
		if (!typeToCheck.isArray()) {
			throw new ContextualError(ContextualError.ARRAY_SELECTION_NOT, getLocation());
		}
		for (int i = 0; i < memory.size(); i++) {
			if (!memory.getList().get(i).verifyExpr(compiler, localEnv, currentClass).isInt()) {
				throw new ContextualError(ContextualError.ARRAY_INDEX_NOT_INT, getLocation());
			}
		}
		ArrayType arrayType = (ArrayType) typeToCheck;
		// Cant take a sub tab
		if (memory.size() != arrayType.getDimension()) {
			throw new ContextualError(ContextualError.ARRAY_SELECTION_MATCH_TYPE, getLocation());
		}
		if (memory.size() == arrayType.getDimension()) {
			setType(arrayType.getBaseType());
			return arrayType.getBaseType();
		} else {
			int diff = arrayType.getDimension() - memory.size();
			ArrayType newType = new ArrayType(compiler.getSymbols().create(arrayType.getBaseType().toString()+"_"+diff), arrayType.getBaseType(), diff);
			setType(newType);
			return newType;
		}
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
        memory.prettyPrint(s, prefix, false);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isArraySelection() {
	    return true;
	}
}
