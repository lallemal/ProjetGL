package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

public class IdentifierArray extends AbstractIdentifier{
	final private AbstractIdentifier varName;
	final private int length;
	
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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
		return null;
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
