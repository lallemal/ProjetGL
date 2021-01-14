package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Divide extends AbstractOpArith {
	
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    protected void mnemo(DecacCompiler compiler, DVal dval, int n) {
    	compiler.getLabelError().setErrorDiv0(true);
    	compiler.addInstruction(new LOAD(dval, Register.R0));
    	if (this.getRightOperand().getType().isFloat()) {
	    	compiler.addInstruction(new DIV(dval, Register.getR(n)));
    	} else if (this.getRightOperand().getType().isInt()) {
	    	compiler.addInstruction(new QUO(dval, Register.getR(n)));
    	}
    	compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorDiv0()));
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
