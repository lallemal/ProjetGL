package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type1.isBoolean() || !type2.isBoolean()) {
            throw new ContextualError(ContextualError.OP_BINARY_NOT_COMPATIBLE, getLocation());
        }
        setType(type1);
        return type1;
    }


    /**
     * This method evaluates the expression of boolean operations and put it in register n if available.
     * It is conceptually the same as a IfThenElse code with thenBranch which LOAD Rn at true and elseBranch which LOAD Rn at false.
     * @param compiler      The compiler to add Instruction
     * @param n             The index of free minimal Register index.
     */
    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
        String label = "boolOp_";
        String pos = getLocation().getLine() + "_" + getLocation().getPositionInLine();
        Label sinonLabel = new Label(label + "False." + pos);
        Label finLabel = new Label(label + "Fin." + pos);
        this.codeGenBranch(compiler, false, sinonLabel);
        compiler.addInstruction(new LOAD(1, Register.getR(n)));
        compiler.addInstruction(new BRA(finLabel));
        compiler.addLabel(sinonLabel);
        compiler.addInstruction(new LOAD(0, Register.getR(n)));
        compiler.addLabel(finLabel);
    }
}
