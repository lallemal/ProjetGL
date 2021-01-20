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
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author louise
 */
public abstract class AbstractMethodBody extends Tree {

    public abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpMethod,
                                          ClassDefinition currentClass, Type returnType)
            throws ContextualError;
    
    public abstract int codeGenBody(DecacCompiler compiler, Label labelFin);
    
    public abstract ListDeclVar getVar();
    
    public boolean isAsmBody() {
    	return false;
    }
}
