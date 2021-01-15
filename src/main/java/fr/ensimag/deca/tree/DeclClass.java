package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
        this.parent = parent; // null si classe Object
        this.field = field;
        this.method = method;
    }
    
    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
    	if (compiler.getKGB() == 1) { //Premiere classe a initialiser : Object
    		ident.getClassDefinition().setAddress(new RegisterOffset(compiler.getKGB(), Register.GB));
    		compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
    	} else {
	    	ident.getClassDefinition().setAddress(new RegisterOffset(compiler.getKGB(), Register.GB));
	    	compiler.addInstruction(new LEA(parent.getClassDefinition().getAddress(), Register.R0));    	
    	}
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getKGB(), Register.GB)));
    	compiler.incrementKGB();
    	
    	if (parent != null) {
	    	for (AbstractDeclMethod i : parent.getClassDefinition().getMethods().getList()) {
	    		ident.getClassDefinition().getMethods().add(i);
	    	}
    	}
    	for (AbstractDeclMethod i : method.getList()) {
    		ident.getClassDefinition().getMethods().add(i);
    	} 
    	ident.getClassDefinition().getMethods().codeGenListDeclMethod(compiler);
    	
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
        s.indent();
        if (field != null){
            field.decompile(s);
        }
        s.println();
        if (method != null){
            method.decompile(s);
        }
        s.unindent();
        s.print("}");
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
        if (field != null){
            field.iterChildren(f);
        }
        if (method != null){
            method.iterChildren(f);
        }
    }

}
