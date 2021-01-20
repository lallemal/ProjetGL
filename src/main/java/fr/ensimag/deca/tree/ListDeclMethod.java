/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author louise
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod>{

    public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclMethod dm : getList()) {
            dm.verifyDeclMethod(compiler, currentClass);
        }
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclMethod dm : getList()) {
            dm.verifyDeclMethodBody(compiler, currentClass);
        }
    }
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
    
    public void codeGenListDeclMethod(DecacCompiler compiler) {
    	
    	Map<String, AbstractDeclMethod> noms = new HashMap<String, AbstractDeclMethod> ();
    	
    	for (AbstractDeclMethod i : getList()) {
    		if (noms.containsKey(i.getName().getName().getName())) {
    			Signature sig_fille = i.getName().getMethodDefinition().getSignature();
    			Signature sig_mere = (noms.get(i.getName().getName().getName())).getName().getMethodDefinition().getSignature();
    			if (sig_fille.sameSignature(sig_mere)) {
    				i.codeGenDeclMethodOverride(compiler, noms.get(i.getName().getName().getName()).getName().getMethodDefinition().getAddress());
    				// On ecrase la methode mere par la methode fille
    			}
    				
    			/*
    			 * premiere methode ..
    			if (i.equals(noms.get(i.getName().getName().getName()))) { // Override
    				i.codeGenDeclMethodOverride(compiler, noms.get(i.getName().getName().getName()).getName().getMethodDefinition().getAddress());
    				// On ecrase la methode mere par la methode fille
    			}*/
    		} else {
    			i.codeGenDeclMethod(compiler);
    			noms.put(i.getName().getName().getName(), i);
    		}	
    	}
    }
    
    public void codeGenListMethod(DecacCompiler compiler, String className) {
    	
    	for (AbstractDeclMethod i : getList()) {
    		i.codeGenMethod(compiler, className);
    	}
    	
    }

}
