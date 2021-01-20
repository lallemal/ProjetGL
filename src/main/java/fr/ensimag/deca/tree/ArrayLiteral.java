package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public class ArrayLiteral extends AbstractExpr{

	final private ListExpr elements;
	
	public ArrayLiteral(ListExpr elements) {
		this.elements = elements;
	}
	
	private static boolean sameListDim(ListExpr l1, ListExpr l2) {
		if (l1.size() != l2.size()) {
			return false;
		}
		if (l1.size() == 0 && l2.size() == 0) {
			return true;
		}
		for (int i = 0; i < l1.size(); i++) {
			AbstractExpr e1, e2;
			e1 = l1.getList().get(i);
			e2 = l2.getList().get(i);
			if (!(e1 instanceof IntLiteral) || !(e2 instanceof  IntLiteral)) {
				return false;
			}
			IntLiteral i1, i2;
			i1 = (IntLiteral) e1;
			i2 = (IntLiteral) e2;
			if (i1.getValue() != i2.getValue()) {
				return false;
			}

		}
		return true;
	}
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		if (elements.getList().size() == 0) {
			SymbolTable.Symbol empty = compiler.getSymbols().create("emptyArray");
		    ArrayType emptyArrayType = new ArrayType(empty, new NoType(empty), -1);
		    setType(emptyArrayType);
		    return (emptyArrayType);
		}
		Type type = elements.getList().get(0).verifyExpr(compiler, localEnv, currentClass);
		Type returnType = type;
		int dim = 0;
		ListExpr dimension = elements.getList().get(0).getDimension();
		if (type.isArray()) {
			dim = ((ArrayType)type).getDimension();
		}
		for (int i = 1; i < elements.size(); i++) {
			Type otherType = elements.getList().get(i).verifyExpr(compiler,localEnv, currentClass);
		    if (!TypeOp.hasParent(compiler, type, otherType)) {
		    	throw new ContextualError(ContextualError.ARRAY_LITERAL_NOT_SAME, getLocation());
			} else {
				returnType = TypeOp.possibleParent;
			}
		    if (!sameListDim(dimension, elements.getList().get(i).getDimension())) {
		    	throw new ContextualError(ContextualError.ARRAY_LITERAL_NOT_SAME, getLocation());
			}
		}
		dimension.addFirst(new IntLiteral(elements.size()));
		setDimension(dimension);
		if (type.isArray()) {
			ArrayType arrayType = (ArrayType) type;
			returnType = arrayType.getBaseType();
		}
		ArrayType returnFinalType = new ArrayType(compiler.getSymbols().create(returnType.toString() + "_" + (dim+1)), returnType,dim+1);
		setType(returnFinalType);
		return returnFinalType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		
		elements.prettyPrint(s, prefix, false);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	public void convFloat(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
		for (int i = 0; i < elements.size(); i++) {
			AbstractExpr expr = elements.getList().get(i);
			Type type = expr.verifyExpr(compiler, localEnv, currentClass);
			if (type.isInt()) {
				elements.getList().set(i, new ConvFloat(expr));
				elements.getList().get(i).verifyExpr(compiler, localEnv, currentClass);
			} else {
				ArrayLiteral sous = (ArrayLiteral) expr;
				sous.convFloat(compiler, localEnv, currentClass);
			}
		}
	}

}
