package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void mnemo(DecacCompiler compiler, DVal dval, int n) {
    	compiler.addInstruction(new MUL(dval, Register.getR(n)));
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}
