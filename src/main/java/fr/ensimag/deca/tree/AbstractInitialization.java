package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractInitialization extends Tree {
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
    
    protected abstract void codeGenDecl(DecacCompiler compiler, DAddr address);
    
    protected abstract void codeGenField(DecacCompiler compiler, AbstractIdentifier type);

    public ListExpr getDimension() {
        return dimension;
    }

    public void setDimension(ListExpr dimension) {
        this.dimension = dimension;
    }

    private ListExpr dimension = new ListExpr();
}
