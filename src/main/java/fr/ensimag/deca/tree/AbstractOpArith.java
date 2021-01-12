package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl40
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.codeExp(compiler, 2);
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
    
    public abstract void mnemo(DecacCompiler compiler, DVal dval, int n);
}
