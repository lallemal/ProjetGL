package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class IdentifierMatrix extends AbstractIdentifier {

	AbstractIdentifier varName;
	final private int line;
	final private int column;
	
	public IdentifierMatrix(AbstractIdentifier varName, int line, int column) {
		this.varName = varName;
		this.line = line;
		this.column = column;
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
		// TODO Auto-generated method stub
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
