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
        var.decompile(s);
        inst.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrintChildren(s, prefix);
        inst.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (var != null){
            var.iterChildren(f);
        }
        if (inst != null){
            inst.iterChildren(f);
        }
    }
    
    
    
}
