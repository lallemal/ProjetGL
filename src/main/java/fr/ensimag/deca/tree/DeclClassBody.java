/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.DeclMethod;

/**
 *
 * @author louise
 */
import java.io.PrintStream;/**
 *
 * @author louise
 */
public class DeclClassBody extends Tree {
    
    private ListDeclMethod method;
    private ListDeclField field;
    
    public DeclClassBody(ListDeclMethod method, ListDeclField field){
        this.method = method;
        this.field = field;
    }
    

    @Override
    public void decompile(IndentPrintStream s) {
        field.decompile(s);
        s.println();
        method.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        field.prettyPrintChildren(s, prefix);
        method.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (field != null){
            field.iterChildren(f);
        }
        if (method != null){
            method.iterChildren(f);
        }
    }
    
}
