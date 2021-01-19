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

import java.io.PrintStream;
import java.util.Iterator;


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

    public boolean equals(ListParam other) {
    	Iterator i = this.iterator();
    	Iterator j = other.iterator();

    	while (i.hasNext() && j.hasNext()) {
    		AbstractDeclParam pi = (AbstractDeclParam) i.next();
    		AbstractDeclParam pj = (AbstractDeclParam) j.next();
    		if (!(pi.equals(pj))) {
    			return false;
    		}
    	}
    		
    	return this.size() == other.size();
    } 
    
    public void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localExp, ClassDefinition currentClass)
        throws ContextualError {
    	int index = 0;
        for (AbstractDeclParam p : getList()) {
            p.verifyDeclParamBody(compiler, localExp, currentClass, index);
            index++;
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        int j = 0;
        for (AbstractDeclParam i: getList()){
            i.decompile(s);
            if (j != size()-1){
                s.print(", ");
            }
            j ++; 
        };
    }

    
}
