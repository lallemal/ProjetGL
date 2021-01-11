package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl40
 * @date 01/01/2021
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass)
            throws ContextualError {
        Type baseType = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!baseType.isInt()) {
            throw new ContextualError(ContextualError.CONV_FLOAT_OPERAND_NOT_INT, getLocation());
        }
        return compiler.getFloat();
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
