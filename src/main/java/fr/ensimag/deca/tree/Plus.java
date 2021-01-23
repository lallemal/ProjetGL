package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
    	if (this.getType().isFloat()) {
    		if (!compiler.getCompilerOptions().isNoCheck()) {
    			compiler.getLabelError().setErrorCalcFloat(true);
    			compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorcalcFloat()));
    		}
    	}
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }
}
