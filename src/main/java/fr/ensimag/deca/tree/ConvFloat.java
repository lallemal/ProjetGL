package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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
        setType(compiler.getFloat());
        return getType();
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	codeExp(compiler, 2);
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	this.getOperand().codeExp(compiler, n);
    	compiler.addInstruction(new FLOAT(Register.getR(n), Register.getR(n)));
    	compiler.getLabelError().setErrorConvFloat(true);
    	if (!compiler.getCompilerOptions().isNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getLabelError().getLabelErrorFLOAT()));
        }
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
