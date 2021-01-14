package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type.isInt() && !type.isFloat()) {
            throw new ContextualError(ContextualError.OP_UNARY_NOT_COMPATIBLE, getLocation());
        }
        setType(type);
        return type;
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	codeExp(compiler, 2);
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
    	compiler.addInstruction(new LOAD(0, Register.getR(n)));
    	if (n==compiler.getRmax()) {
    		this.getOperand().codeExp(compiler, 0);
    		compiler.addInstruction(new SUB(Register.R0, Register.getR(n)));
    	} else {
    		this.getOperand().codeExp(compiler, n+1);
    		compiler.addInstruction(new SUB(Register.getR(n+1), Register.getR(n)));
    	}
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
