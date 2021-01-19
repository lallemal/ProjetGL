package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getBool());
        return getType();
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(this.dval(), Register.getR(n)));
    }
          
    @Override
    protected DVal dval() {
    	return new ImmediateInteger(value ? 1 : 0);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	compiler.addInstruction(new LOAD((value ? 1 : 0), Register.getR(2)));
    	compiler.addInstruction(new STORE(Register.getR(2), address));
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        if (value) {
            if (evaluate) {
                compiler.addInstruction(new BRA(label));
            }
        } else {
            if (!evaluate) {
                compiler.addInstruction(new BRA(label));
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
