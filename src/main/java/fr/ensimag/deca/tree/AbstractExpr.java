package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }
    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    public ListExpr getDimension() {
        return dimension;
    }

    public void setDimension(ListExpr dimension) {
        Validate.notNull(dimension);
        this.dimension = dimension;
    }

    private ListExpr dimension = new ListExpr();

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        LOG.debug("verify AbstractExpr RValue : start");
        Type type2 = verifyExpr(compiler, localEnv, currentClass);
        if (!expectedType.isFloat() || !type2.isInt()) {
            if (!TypeOp.subType(compiler, type2, expectedType)) {
                if (expectedType.isArray() && type2.isArray()) {
                    ArrayType arrayType1 = (ArrayType) expectedType;
                    ArrayType arrayType2 = (ArrayType) type2;
                    if (arrayType2.getBaseType().isNoType() && arrayType2.getDimension() == -1) {
                        LOG.debug("verify AbstractExpr Rvalue : end");
                        return this;
                    }
                    /*if (arrayType1.getDimension() == arrayType2.getDimension() && arrayType1.getBaseType().isFloat() && arrayType2.getBaseType().isInt()) {
                        if (this instanceof ArrayLiteral) {
                            ArrayLiteral thisLiteral = (ArrayLiteral) this;
                            thisLiteral.convFloat(compiler, localEnv, currentClass);
                            verifyExpr(compiler, localEnv, currentClass);
                            LOG.debug("verify AbstractExpr Rvalue : end");
                            return this;
                        }
                    }*/
                    throw new ContextualError(ContextualError.ASSIGN_NOT_COMPATIBLE + " (" + expectedType.toString() + "," + type2.toString() + ")", getLocation());
                }
                throw new ContextualError(ContextualError.ASSIGN_NOT_COMPATIBLE + " (" + expectedType.toString() + "," + type2.toString() + ")", getLocation());
            }
        }
        else {
            AbstractExpr floatNew = new ConvFloat(this);
            floatNew.verifyExpr(compiler, localEnv, currentClass);
            LOG.debug("verify AbstractExpr Rvalue : end");
            return floatNew;
        }
        LOG.debug("verify AbstractExpr Rvalue : end");
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify AbstractExpr Inst : start");
        verifyExpr(compiler, localEnv, currentClass);
        LOG.debug("verify AbstractExpr Inst : end");
    }

    protected void verifyPrint(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify AbstractExpr Print : start");
        type = verifyExpr(compiler, localEnv, currentClass);
        if (!type.isInt() && !type.isFloat() && !type.isString()) {
            throw new ContextualError(ContextualError.PRINT_EXPR_NOT_COMPATIBLE + " (given type :" + type.toString() + ")", this.getLocation());
        }
        LOG.debug("verify AbstractExpr print : end");
    }
    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify AbstractExpr Condition : start");
        this.verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError(ContextualError.EXPR_CONDITION_NOT_BOOLEAN, getLocation());
        }
        LOG.debug("verify AbstractExpr Condition : end");
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */

    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
    	this.codeExp(compiler, 1);
    	if (this.getType().isInt()) {
    		compiler.addInstruction(new WINT());
    	} else if (this.getType().isFloat()) {
    		if (printHex) {
    			compiler.addInstruction(new WFLOATX());
    		} else {
    			compiler.addInstruction(new WFLOAT());
    		}
    	} else if (this.isSelection()) {
    		Selection s = (Selection) this;
    		if (s.getIdent().getType().isInt()) {
    			compiler.addInstruction(new WINT());
    		} else if (s.getIdent().getType().isFloat()) {
    			if (printHex) {
        			compiler.addInstruction(new WFLOATX());
        		} else {
        			compiler.addInstruction(new WFLOAT());
        		}
    		}
    	} else {
    		//nothing to do
    	}
    }
    
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	this.codeExp(compiler, 2);
    	compiler.addInstruction(new STORE(Register.getR(2), address));
    }
    
    protected void codeGenField(DecacCompiler compiler) {
    	this.codeExp(compiler, 0);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //nothing to do
    }
    
    protected DVal dval() {
    	return null;
    }
    protected void codeExp(DecacCompiler compiler, int n) {
    	//nothing to do
	}

    /**
     * Generate the code for the branchement to label of the expression if this one is evaluated as _evaluate_
     * Explication of all those methods is in poly-projet-Gl Partie 7.2 (page 220)
     * @param compiler      The compiler : contains addInstruction method
     * @param evaluate      Boolean of reference which has to be the same as the expression to do the branch
     * @param label         The label to branch
     */
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        // Nothing to do for most of expression
    }
    
    public boolean isIdentifier() {
    	return false;
    }
    
    public boolean isSelection() {
    	return false;
    }
    
    public boolean isThis() {
    	return false;
    }
    
    public boolean isNew() {
    	return false;
    }

    public boolean isArraySelection() {
        return false;
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
