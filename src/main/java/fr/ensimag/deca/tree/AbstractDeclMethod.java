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
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public abstract class AbstractDeclMethod extends Tree{
    
    protected abstract void verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
    
    public abstract void codeGenDeclMethod(DecacCompiler compiler);
    
    public abstract void codeGenDeclMethodOverride(DecacCompiler compiler, DAddr address);
    
    public abstract AbstractIdentifier getName();

}
