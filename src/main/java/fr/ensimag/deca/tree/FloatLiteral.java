package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl40
 * @date 01/01/2021
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getFloat());
        return getType();
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
    	float value = ((FloatLiteral) this).getValue();
    	compiler.addInstruction(new LOAD(value, Register.R1));
    	if (printHex) {
    		compiler.addInstruction(new WFLOATX());
    	} else {
    		compiler.addInstruction(new WFLOAT());
    	}
    }
    
    @Override
    protected DVal dval() {
    	return new ImmediateFloat(value);
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(this.dval(), Register.getR(n)));
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.codeExp(compiler, 2);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	compiler.addInstruction(new LOAD(value, Register.getR(2)));
    	compiler.addInstruction(new STORE(Register.getR(2), address));
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
