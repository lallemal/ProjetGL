package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        if (!evaluate) {
            Label labelFin = new Label(label.toString() + "_fin_" + getLocation().getLine() + "_" + getLocation().getPositionInLine());
            getLeftOperand().codeGenBranch(compiler, true, labelFin);
            getRightOperand().codeGenBranch(compiler, false, label);
            compiler.addLabel(labelFin);
        } else {
            getLeftOperand().codeGenBranch(compiler, true, label);
            getRightOperand().codeGenBranch(compiler, true, label);
        }
    }
}
