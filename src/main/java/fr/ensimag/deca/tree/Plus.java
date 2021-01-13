package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
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
	protected void mnemo(DecacCompiler compiler, DVal dval, int n) {
    	compiler.addInstruction(new ADD(dval, Register.getR(n)));
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }
}
