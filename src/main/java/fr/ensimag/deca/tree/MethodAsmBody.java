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
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class MethodAsmBody extends AbstractMethodBody {

    private StringLiteral cont;
    
    public MethodAsmBody(StringLiteral cont){
        this.cont = cont;
    }
    
    public ListDeclVar getVar() {
    	return new ListDeclVar();
    }
    
    @Override
    public int codeGenBody(DecacCompiler compiler, Label labelFin) {
    	compiler.add(new InlinePortion(cont.getValue()));
    	return 0;
    }
    
    @Override
    public boolean isAsmBody() {
    	return true;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        cont.decompile(s);
    }

 
    @Override
    protected void iterChildren(TreeFunction f) {
        cont.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        cont.prettyPrint(s, prefix, true);
    }

    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpMethod, ClassDefinition currentClass, Type returnType) throws ContextualError {

    }
}
