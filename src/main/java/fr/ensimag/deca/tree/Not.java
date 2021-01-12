package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.Register;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	int i = compiler.getManageRegister().useFreeRegister();
    	evaluateRegister(compiler, this, i);
    	compiler.getManageRegister().freed(i);
    }
    
    @Override
    protected void evaluateRegister(DecacCompiler compiler, AbstractExpr e, int i) {
    	int i2 = compiler.getManageRegister().useFreeRegister();
    	compiler.addInstruction(new LOAD(1, Register.getR(i)));
    	evaluateRegister(compiler, this.getOperand(), i2);
    	compiler.getManageRegister().freed(i2);
    	compiler.addInstruction(new SUB(Register.getR(i2), Register.getR(i)));
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
