package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

public class IdentifierArray extends AbstractIdentifier{
	final private AbstractIdentifier varName;
	final private int length;

	private Definition definition;
	
	public IdentifierArray(AbstractIdentifier varName, int length) {
		this.varName = varName;
		this.length = length;
	}
	@Override
	public ClassDefinition getClassDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Definition getDefinition() {
	    return definition;
	}

	@Override
	public FieldDefinition getFieldDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodDefinition getMethodDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol getName() {
	    // TODO
		return null;
	}

	@Override
	public ExpDefinition getExpDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableDefinition getVariableDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefinition(Definition definition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type verifyType(DecacCompiler compiler) throws ContextualError {
	    Type baseType = varName.verifyType(compiler);
	    if (baseType.isVoid()) {
	    	throw new ContextualError(ContextualError.ARRAY_NEW_VOID, getLocation());
		}
	    ArrayType arrayType = new ArrayType(compiler.getSymbols().create(varName.getName().toString()+"_"+length), baseType, length);
		TypeDefinition typeDef = new TypeDefinition(arrayType, Location.BUILTIN);
		setDefinition(typeDef);
		setType(arrayType);
	    return arrayType;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Type verifyMethod(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
		// TODO Auto-generated method stub
		return null;
	}

}
