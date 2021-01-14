package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type1.isInt() && !type2.isInt()) {
            throw new ContextualError(ContextualError.OP_BINARY_NOT_COMPATIBLE, getLocation());
        }
        setType(type1);
        return type1;
    }

    protected void mnemo(DecacCompiler compiler, DVal dval, int n) {
    	compiler.getLabelError().setErrorMod0(true);
    	compiler.addInstruction(new LOAD(dval, Register.R0));
    	compiler.addInstruction(new CMP(0, Register.R0));
	    compiler.addInstruction(new BEQ(compiler.getLabelError().getLabelErrorMod0()));
	    compiler.addInstruction(new REM(dval, Register.getR(n)));
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
