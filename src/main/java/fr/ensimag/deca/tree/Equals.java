package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        super.codeGenBranch(compiler, evaluate, label);
        if (evaluate) {
            compiler.addInstruction(new BEQ(label));
        } else {
            compiler.addInstruction(new BNE(label));
        }
    }
}
