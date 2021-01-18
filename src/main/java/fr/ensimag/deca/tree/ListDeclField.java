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
import fr.ensimag.ima.pseudocode.instructions.RTS;

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

    public void verifyListDeclFieldBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField df : getList()) {
            df.verifyDeclFieldBody(compiler, currentClass);
        }
    }
    
    public void codeGenField(DecacCompiler compiler) {
    	for (AbstractDeclField i : getList()) {
    		i.codeGenField(compiler);
    	}
    	compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

}
