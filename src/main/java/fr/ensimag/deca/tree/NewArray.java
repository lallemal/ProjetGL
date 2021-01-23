package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class NewArray extends AbstractExpr{

	final private AbstractIdentifier type;
	final private ListExpr memory;

	private static final Logger LOG = Logger.getLogger(NewArray.class);
	
	public NewArray(AbstractIdentifier type, ListExpr memory) {
		this.type = type;
		this.memory = memory;
	}
	
	protected void codeExp(DecacCompiler compiler, int n) {
	    //  CAN BE FACTORIZE : NOT ENOUGH TIME
	    compiler.getLabelError().setErrorTasPlein(true);
	    compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.getR(n)));
	    for (AbstractExpr expr : memory.getList()) {
	    	if (n + 1 <= compiler.getRmax()) {
				expr.codeExp(compiler, n + 1);
				compiler.setRegistreUsed(n+1);
				compiler.addInstruction(new MUL(Register.getR(n + 1), Register.getR(n)));
			} else {
	    	    compiler.addInstruction(new TSTO(new ImmediateInteger(1)));
	    	    if (!compiler.getCompilerOptions().isNoCheck()) {
	    	    	compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
				}
	    		compiler.addInstruction(new PUSH(Register.getR(n)));
				expr.codeExp(compiler, n);
				compiler.addInstruction(new MUL(new RegisterOffset(0, Register.SP), Register.getR(n)));
				compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
			}
		}
	    compiler.addInstruction(new ADD(new ImmediateInteger(1), Register.getR(n)));
	    compiler.addInstruction(new NEW(Register.getR(n), Register.getR(n)));
	    compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
	    // TSTO :
		if (n + 2 <= compiler.getRmax()) {
		    compiler.setRegistreUsed(n+1);
		    compiler.setRegistreUsed(n+2);
			compiler.addInstruction(new NEW(memory.size(), Register.getR(n + 1)));
			compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
			int i = 0;
			for (AbstractExpr expr : memory.getList()) {
				expr.codeExp(compiler, n + 2);
				compiler.addInstruction(new STORE(Register.getR(n + 2), new RegisterOffset(i, Register.getR(n + 1))));
				i++;
			}

			compiler.addInstruction(new STORE(Register.getR(n + 1), new RegisterOffset(0, Register.getR(n))));
		} else {
			if (n + 1 <= compiler.getRmax()) {
				compiler.addInstruction(new TSTO(new ImmediateInteger(1)));
				if (!compiler.getCompilerOptions().isNoCheck()) {
					compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
				}
				compiler.setRegistreUsed(n+1);
				compiler.addInstruction(new NEW(memory.size(), Register.getR(n + 1)));
				compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
				compiler.addInstruction(new PUSH(Register.getR(n+1)));
				int i = 0;
				for (AbstractExpr expr : memory.getList()) {
					expr.codeExp(compiler, n + 1);
					compiler.addInstruction(new POP(Register.R0));
					compiler.addInstruction(new STORE(Register.getR(n + 1), new RegisterOffset(i, Register.R0)));
					compiler.addInstruction(new PUSH(Register.R0));
					i++;
				}
				compiler.addInstruction(new POP(Register.getR(n+1)));
				compiler.addInstruction(new STORE(Register.getR(n + 1), new RegisterOffset(0, Register.getR(n))));
			} else {
				compiler.addInstruction(new TSTO(new ImmediateInteger(2)));
				if (!compiler.getCompilerOptions().isNoCheck()) {
					compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
				}
				compiler.addInstruction(new PUSH(Register.getR(n)));
				compiler.addInstruction(new NEW(memory.size(), Register.getR(n)));
				compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
				compiler.addInstruction(new PUSH(Register.getR(n)));
				int i = 0;
				for (AbstractExpr expr : memory.getList()) {
					expr.codeExp(compiler, n);
					compiler.addInstruction(new POP(Register.R0));
					compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(i, Register.R0)));
					compiler.addInstruction(new PUSH(Register.R0));
					i++;
				}

				compiler.addInstruction(new POP(Register.R0));
				compiler.addInstruction(new POP(Register.getR(n)));
				compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.getR(n))));
			}
		}
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    Type type = this.type.verifyType(compiler);
	    if (type.isVoid()) {
	    	throw new ContextualError(ContextualError.ARRAY_NEW_VOID, getLocation());
		}
	    boolean stop = false;
		for (int i = 0; i < memory.size(); i++) {
		    Type otherType = memory.getList().get(i).verifyExpr(compiler, localEnv, currentClass);
			if (!stop && otherType.isNoType()) {
				stop = true;
			}
			if (stop && !otherType.isNoType()) {
				throw new ContextualError(ContextualError.ARRAY_NO_THEN, getLocation());
			}
			if (!otherType.isNoType() && !otherType.isInt()) {
				throw new ContextualError(ContextualError.ARRAY_DIM_GIVEN, getLocation());
			}
		}
	    ArrayType arrayType = new ArrayType(compiler.getSymbols().create(type.toString() + "_" + memory.size()), type, memory.size());
	    setType(arrayType);
	    return arrayType;
	}

	@Override
	public AbstractExpr verifyRValue(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type expectedType) throws ContextualError {
	    LOG.debug("Verify RValue Array : start");
	    Type type2 = verifyExpr(compiler, localEnv, currentClass);
	    if (!expectedType.sameType(type2)) {
	    	throw new ContextualError(ContextualError.ASSIGN_NOT_COMPATIBLE, getLocation());
		}
		setDimension(memory);


	    LOG.debug("Verify RValue Array : end");
		return this;
	}

	@Override
	public void decompile(IndentPrintStream s) {
                s.print("new ");
		type.decompile(s);
                s.print("[");
                memory.decompile(s);
                s.print("]");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, true);
		memory.prettyPrint(s, prefix, false);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
                memory.iterChildren(f);
		
	}

	
}
