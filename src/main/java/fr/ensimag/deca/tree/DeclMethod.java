/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class DeclMethod extends AbstractDeclMethod {
    
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListParam param;
    private AbstractMethodBody body;
    
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListParam param, AbstractMethodBody body){
        assert(type != null);
        assert(name != null);
        this.type = type;
        this.name = name;
        this.param = param;
        this.body = body;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
       type.prettyPrint(s, prefix, false);
       name.prettyPrint(s, prefix, false);
       if (param != null){
           param.prettyPrintChildren(s, prefix);
       }
       if (body != null){
           body.prettyPrintChildren(s, prefix);
       }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
