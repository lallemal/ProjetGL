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

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class MethodBody extends AbstractMethodBody {

    private ListDeclVar var;
    private ListInst inst;
    
    public MethodBody(ListDeclVar var, ListInst inst){
        this.var = var;
        this.inst = inst;
    }
    
    public ListDeclVar getVar() {
    	return var;
    }
    
    @Override
    public int codeGenBody(DecacCompiler compiler, Label labelFin) {
    	compiler.resetRegistreUsed();
    	var.codeGenListDeclInMethod(compiler);
    	inst.codeGenListInst(compiler, labelFin);
    	if (compiler.nbRegistreUsed() == 0) {
    		return ((var.size()>0)?1:0);
    	} else {
    		return compiler.nbRegistreUsed();
    	}
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        var.decompile(s);
        inst.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrintChildren(s, prefix);
        inst.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (var != null){
            var.iterChildren(f);
        }
        if (inst != null){
            inst.iterChildren(f);
        }
    }


    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpMethod, ClassDefinition currentClass, Type returnType) throws ContextualError {
        var.verifyListDeclVariable(compiler, envExpMethod, currentClass);
        inst.verifyListInst(compiler, envExpMethod, currentClass, returnType);

    }
}
