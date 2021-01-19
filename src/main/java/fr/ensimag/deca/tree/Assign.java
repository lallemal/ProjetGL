package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.context.Type;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // TODO LeftOperand is lvalue 3.64 certainly lvalue class has to be completed
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
        if (type.isArray()) {
            getLeftOperand().setDimension(getRightOperand().getDimension());
        }

        setType(type);
        return type;
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Identifier x = (Identifier) this.getLeftOperand();
        AbstractExpr e = this.getRightOperand();
        e.codeExp(compiler, 2);
        compiler.addInstruction(new STORE(Register.getR(2), x.getExpDefinition().getOperand()));
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
