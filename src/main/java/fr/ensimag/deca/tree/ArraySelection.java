package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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
		if (memory.size() != arrayType.getDimension()) {
			throw new ContextualError(ContextualError.ARRAY_SELECTION_MATCH_TYPE, getLocation());
		}
		setType(arrayType.getBaseType());
		return arrayType.getBaseType();
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

}
