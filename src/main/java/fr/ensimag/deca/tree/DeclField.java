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
    
    //private Visibility visib;
    private AbstractIdentifier type;
    private AbstractIdentifier nom;
    private AbstractInitialization init;
    
    public DeclField(AbstractIdentifier type, AbstractIdentifier nom, AbstractInitialization init){
        //this.visib = visib;
        this.type = type;
        this.nom = nom;
        this.init = init;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //s.print(visib);
        type.prettyPrint(s, prefix, false);
        nom.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
