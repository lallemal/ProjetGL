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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author louise
 */
public class ListParam extends TreeList<AbstractDeclParam> {
    
    public Signature verifyDeclMethod(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam p : getList()) {
            sig.add(p.verifyDeclParam(compiler));
        }
        return sig;
    }

    public void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localExp, ClassDefinition currentClass)
        throws ContextualError {
        for (AbstractDeclParam p : getList()) {
            p.verifyDeclParamBody(compiler, localExp, currentClass);
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
