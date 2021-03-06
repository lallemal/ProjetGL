package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl40
 * @date 01/01/2021
 */
public class NoInitialization extends AbstractInitialization {

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Nothing to Check : Nothing here.
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	//nothing to do
    }
    
    @Override
    protected void codeGenField(DecacCompiler compiler, AbstractIdentifier type) {
        Type typeField = type.getType();
        if (typeField.isInt() || typeField.isBoolean()) {
            compiler.addInstruction(new LOAD(0, Register.R0));
        }
        else if (typeField.isFloat()) {
            compiler.addInstruction(new LOAD((float) 0.0, Register.R0));
        } else {
    		compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
    	}
    }
    
    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
