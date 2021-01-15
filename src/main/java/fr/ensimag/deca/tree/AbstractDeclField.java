/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 *
 * @author louise
 */
public abstract class AbstractDeclField extends Tree{
    
     protected abstract void verifyDeclField(DecacCompiler compiler,
            ClassDefinition currentClass)
            throws ContextualError;

     protected abstract void verifyDeclFieldBody(DecacCompiler compiler,
                                             ClassDefinition currentClass)
             throws ContextualError;
}
