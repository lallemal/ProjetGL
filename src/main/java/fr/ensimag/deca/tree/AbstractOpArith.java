package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.apache.log4j.Logger;


/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify AbstractOpArith : start");
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if ((!type1.isInt() && !type1.isFloat()) || (!type2.isInt() && !type2.isFloat())) {
            throw new ContextualError(ContextualError.OP_BINARY_NOT_COMPATIBLE, getLocation());
        } else {
            if (type1.isInt() && type2.isFloat()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
            }
            else if (type1.isFloat() && type2.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
            }
        }
        Type returnType = TypeOp.arith(compiler, type1, type2);
        setType(returnType);
        LOG.debug("verify AbstractOpArith : end");
        return returnType;
    }
}
