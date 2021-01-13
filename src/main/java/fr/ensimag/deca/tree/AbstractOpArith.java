package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
            else if (type1.isFloat() && type2.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        Type returnType = TypeOp.arith(compiler, type1, type2);
        setType(returnType);
        LOG.debug("verify AbstractOpArith : end");
        return returnType;
    }


    @Override
    protected void codeExp(DecacCompiler compiler, int n) {
        AbstractExpr e1 = this.getLeftOperand();
        AbstractExpr e2 = this.getRightOperand();
        if (e2.dval() != null) {
            e1.codeExp(compiler, n);
            this.mnemo(compiler, e2.dval(), n);
        } else {
            if (compiler.getRmax() > n) {
                e1.codeExp(compiler, n);
                e2.codeExp(compiler, n+1);
                this.mnemo(compiler, Register.getR(n+1), n);
            } else {
                e1.codeExp(compiler, n);
                compiler.addInstruction(new PUSH(Register.getR(n)));
                e2.codeExp(compiler, n);
                compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                compiler.addInstruction(new POP(Register.getR(n)));
                this.mnemo(compiler, Register.R0, n);
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.codeExp(compiler, 2);
    }



    protected void mnemo(DecacCompiler compiler, DVal dval, int n) {

    }
}
