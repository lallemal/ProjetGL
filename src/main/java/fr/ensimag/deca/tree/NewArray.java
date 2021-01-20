package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, true);
		memory.prettyPrint(s, prefix, false);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	
}
