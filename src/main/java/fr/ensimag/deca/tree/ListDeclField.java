/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author louise
 */
public class ListDeclField extends TreeList<DeclField>{

    @Override
    public void decompile(IndentPrintStream s) {
        for (DeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
    
}
