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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class ListParam extends TreeList<DeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        int j = 0;
        for (DeclParam i: getList()){
            i.decompile(s);
            if (j != size()-1){
                s.print(", ");
            }
            j ++; 
        };
    }

    
}
