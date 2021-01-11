package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
                setLeftOperand(new ConvFloat(getLeftOperand()));
            }
            else if (type1.isFloat() && type2.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
            }
        }
        setType(compiler.getBool());
        return compiler.getBool();
    }


}
