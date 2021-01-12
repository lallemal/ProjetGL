package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl40
 * @date 01/01/2021
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        return compiler.getInt();
    	//throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected DVal dval() {
    	return new ImmediateInteger(value);
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(this.dval(), Register.getR(n)));
    }
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
         	int value = this.getValue();
        	compiler.addInstruction(new LOAD(value, Register.R1));
        	compiler.addInstruction(new WINT());
    }
    
    @Override
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	compiler.addInstruction(new LOAD(value, Register.getR(2)));
    	compiler.addInstruction(new STORE(Register.getR(2), address));
    }
    
    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
