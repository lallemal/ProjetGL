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
public class DeclField extends Tree{
    
    private Visibility visib;
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private AbstractInitialization init;
    
    public DeclField(Visibility visib, AbstractIdentifier type, AbstractIdentifier nom, AbstractInitialization init){
        this.visib = visib;
        this.type = type;
        this.name = nom;
        this.init = init;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(visib + " ");
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print(" ");
        init.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.println(prefix+ "Visibility: " + visib);
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        init.iter(f);
    }
    
}
