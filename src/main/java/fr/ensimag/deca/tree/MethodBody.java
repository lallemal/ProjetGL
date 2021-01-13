/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class MethodBody extends AbstractMethodBody {

    private ListDeclVar var;
    private ListInst inst;
    
    public MethodBody(ListDeclVar var, ListInst inst){
        this.var = var;
        this.inst = inst;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        field.decompile(s);
        s.println();
        method.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrintChildren(s, prefix);
        inst.prettyPrintChildren(s, prefix);
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
