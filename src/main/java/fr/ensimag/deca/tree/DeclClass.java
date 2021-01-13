package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl40
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {
        
    private AbstractIdentifier ident;
    private AbstractIdentifier parent;
    private ListDeclField field;
    private ListDeclMethod method;
    
    
    public DeclClass(AbstractIdentifier ident, AbstractIdentifier parent, ListDeclField field, ListDeclMethod method){
        this.ident = ident;
        this.parent = parent;
        this.field = field;
        this.method = method;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        ident.decompile(s);
        if (parent != null){
            s.print(" extends ");
            parent.decompile(s);
        }
        s.println(" {");
        if (body != null){
            s.indent();
            body.decompile(s);
            s.unindent();
        }
        s.print('}');
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
        if (parent != null){
            parent.prettyPrint(s, prefix, false);
        }
        if (field != null){
            field.prettyPrintChildren(s, prefix);
        }
        if (method != null){
            method.prettyPrintChildren(s, prefix);
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
        if (parent != null){
            parent.iter(f);
        }
        if (body != null){
            body.iterChildren(f);
        }
    }

}
