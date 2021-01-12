package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        if (evaluate) {
            Label labelFin = new Label(label.toString() + "_fin.n");
            getLeftOperand().codeGenBranch(compiler, false, labelFin);
            getRightOperand().codeGenBranch(compiler, true, label);
            compiler.addLabel(labelFin);
        } else {
            getLeftOperand().codeGenBranch(compiler, false, label);
            getRightOperand().codeGenBranch(compiler, false, label);
        }
    }
}
