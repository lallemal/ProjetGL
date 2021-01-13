package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        // If it is not arithmetic operands
        if ((!type1.isInt() && !type1.isFloat()) || (!type2.isInt() && !type2.isFloat())) {
            // It can be class  or null with operator eq neq
            if ((getOperatorName().equals("==") || getOperatorName().equals("!=")) && ((!type1.isClass() && !type1.isNull())|| (!type2.isClass() && !type2.isNull()))) {
                // if not it is a context error
                throw new ContextualError(ContextualError.OP_BINARY_NOT_COMPATIBLE, getLocation());
            }
        } else {
            if (type1.isInt() && type2.isFloat()) {
                AbstractExpr conv = new ConvFloat(getLeftOperand());
                conv.verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(conv);
            }
            else if (type1.isFloat() && type2.isInt()) {
                AbstractExpr conv = new ConvFloat(getRightOperand());
                conv.verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(conv);
            }
        }
        setType(compiler.getBool());
        return compiler.getBool();
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        DVal opG = getLeftOperand().dval();
        getRightOperand().codeExp(compiler, 3);
        if (opG == null) {
            getLeftOperand().codeExp(compiler, 2);
            compiler.addInstruction(new CMP(Register.getR(2), Register.getR(3)));
        } else {
            compiler.addInstruction(new CMP(opG, Register.getR(3)));
        }
    }
}
