package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
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
    	evaluateRegister(compiler, this.getLeftOperand(), i);
    	evaluateRegister(compiler, this.getRightOperand(), i2);
    	compiler.getManageRegister().freed(i2);
    	compiler.addInstruction(new ADD(Register.getR(i2), Register.getR(i)));
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }
}
