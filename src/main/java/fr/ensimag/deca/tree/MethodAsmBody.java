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
public class MethodAsmBody extends AbstractMethodBody {

    private StringLiteral cont;
    
    public MethodAsmBody(StringLiteral cont){
        this.cont = cont;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        cont.decompile(s);
    }

 
    @Override
    protected void iterChildren(TreeFunction f) {
        cont.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        cont.prettyPrint(s, prefix, true);
    }
    
}
