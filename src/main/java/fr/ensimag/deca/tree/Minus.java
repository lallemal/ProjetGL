package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
	protected void mnemo(DecacCompiler compiler, DVal dval, int n) {
    	compiler.addInstruction(new SUB(dval, Register.getR(n)));
    	if (this.getType().isFloat()) {
    		if (!compiler.getCompilerOptions().isNoCheck()) {
    			compiler.getLabelError().setErrorCalcFloat(true);
    			compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorCalcFloat()));
    		}
    	}
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
