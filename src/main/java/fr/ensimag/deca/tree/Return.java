/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class Return extends AbstractInst {
    private final AbstractExpr operand;
    
    public Return(AbstractExpr operand) {
        this.operand = operand;
	}

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError(ContextualError.RETURN_VOID, getLocation());
        }
        operand.verifyRValue(compiler, localEnv, currentClass, returnType);

    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
    	//nothing to do, we need a labelFin 
    }
    
    public void codeGenInst(DecacCompiler compiler, Label labelFin) {
    	operand.codeExp(compiler, 2);
    	compiler.addInstruction(new LOAD(Register.getR(2), Register.R0));
        compiler.addInstruction(new BRA(labelFin));
    }
    
    @Override
    protected boolean isReturn() {
    	return true;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        operand.decompile(s);
        s.print(";");
    }

    @Override

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }
}
