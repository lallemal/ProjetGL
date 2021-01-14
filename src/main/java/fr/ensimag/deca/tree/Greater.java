package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        super.codeGenBranch(compiler, evaluate, label);
        if (evaluate) {
            compiler.addInstruction(new BLT(label));
        } else {
            compiler.addInstruction(new BGE(label));
        }
    }
}
