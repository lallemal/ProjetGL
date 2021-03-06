package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;


/**
 * Deca Identifier
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Identifier.class);
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Identifier Expr : start");
        if (localEnv.get(name) == null) {
            throw new ContextualError(ContextualError.IDENTIFIER_EXP_UNDEFINED + " " + name.toString(), getLocation());
        }
        ExpDefinition def = localEnv.get(name);
        setDefinition(def);
        if (!def.isField() && !def.isParam() && !def.isExpression()) {
            throw new ContextualError(ContextualError.LVALUE_IDENT_TYPE, getLocation());
        }
        setType(def.getType());
        LOG.debug("verify Identifier Expr : end");
        return def.getType();
    }
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
    	Type type = this.getType();
    	this.codeExp(compiler, 1);
    	if (type.isInt()) {
    		compiler.addInstruction(new WINT());
    	} else if (type.isFloat()) {
    		if (printHex) {
    			compiler.addInstruction(new WFLOATX());
    		} else {
    			compiler.addInstruction(new WFLOAT());
    		}
    	}
    }
    
    @Override
    protected DVal dval() {
    	return this.getExpDefinition().getOperand();
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	compiler.setRegistreUsed(2);
        codeExp(compiler, 2);
    }
    
    protected void codeExp(DecacCompiler compiler, int n) {
    	ExpDefinition def = this.getExpDefinition();
    	if (def.isField()) {
	    	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(n)));
			compiler.addInstruction(new LOAD(new RegisterOffset( ((FieldDefinition) def).getIndex()+1, Register.getR(n)), Register.getR(n)));
    	} else if (def.isParam()) {
    		int index = ((ParamDefinition) def).getIndex();
    		compiler.addInstruction(new LOAD(new RegisterOffset(-(3+index), Register.LB), Register.getR(n)));
    	} else {
    		compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.getR(n)));
    	}
    }
    
    @Override
    public boolean isIdentifier() {
    	return true;
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        if (compiler.getEnv_types().get(name) == null) {
            throw new ContextualError(ContextualError.IDENTIFIER_TYPE_UNDEFINED + " " + name.toString(), getLocation());
        }
        if (!compiler.getEnv_types().get(name).getNature().equals("type")) {
            throw new ContextualError(ContextualError.IDENTIFIER_TYPE_NOTTYPE, getLocation());
        }
        setDefinition(compiler.getEnv_types().get(name));
        setType(compiler.getEnv_types().get(name).getType());
        return compiler.getEnv_types().get(name).getType();
    }

    @Override
    public Type verifyMethod(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        ExpDefinition def = localEnv.get(this.name);
        if (def == null) {
            throw new ContextualError(ContextualError.IDENTIFIER_TYPE_UNDEFINED, getLocation());
        }
        if (!def.isMethod()) {
            throw new ContextualError(ContextualError.IDENTIFIER_NOT_METHOD, getLocation());
        }
        setDefinition(def);
        return def.getType();
    }


    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
}
