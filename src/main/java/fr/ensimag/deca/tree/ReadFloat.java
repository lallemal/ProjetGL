package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

import java.io.PrintStream;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getFloat());
        return compiler.getFloat();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	compiler.setRegistreUsed(2);
        codeExp(compiler, 2);
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
        compiler.addInstruction(new RFLOAT());
        compiler.getLabelError().setErrorReadFloat(true);
        compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorRFLOAT()));
        compiler.addInstruction(new LOAD(Register.R1, Register.getR(n)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
