/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author louise
 */
public class ListDeclField extends TreeList<AbstractDeclField>{
    
    public void verifyListDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField df : getList()) {
            df.verifyDeclField(compiler, currentClass);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
