package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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

    private final Logger LOG = Logger.getLogger(DeclClass.class);
    
    
    public DeclClass(AbstractIdentifier ident, AbstractIdentifier parent, ListDeclField field, ListDeclMethod method){
        Validate.notNull(ident);
        this.ident = ident;
        this.parent = parent; // null si classe Object
        this.field = field;
        this.method = method;
    }
    
    public AbstractIdentifier getIdent() {
    	return ident;
    }
    
    public ListDeclMethod getMethod() {
    	return method;
    }
    
    public ListDeclField getField() {
    	return field;
    }
    
    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
    	
    	if (compiler.getKGB() == 1) { //Premiere classe a initialiser : Object
    		DeclClass object = compiler.getObject();
    		object.getIdent().getClassDefinition().setLabelInit(new Label("init.Object"));
    		compiler.addComment("Construction de la table des methodes de Object");
    		compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
    		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getKGB(), Register.GB)));
        	compiler.incrementKGB();
        	compiler.incrementKSP();
        	object.getMethod().codeGenListDeclMethod(compiler);        	
    	}
    	ident.getClassDefinition().setLabelInit(new Label("init."+ident.getName().getName()));
    	ident.getClassDefinition().setAddress(new RegisterOffset(compiler.getKGB(), Register.GB));
    	compiler.addComment("Construction de la table des methodes de " + ident.getName().getName());
    	compiler.addInstruction(new LEA(parent.getClassDefinition().getAddress(), Register.R0));   
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getKGB(), Register.GB)));
    	compiler.incrementKGB();
    	compiler.incrementKSP();
    	
    	for (AbstractDeclMethod i : parent.getClassDefinition().getMethods().getList()) {
	    	ident.getClassDefinition().getMethods().add(i);
	    }
    	for (AbstractDeclMethod i : method.getList()) {
    		ident.getClassDefinition().getMethods().add(i);
    		Label label = new Label("code."+ident.getName().getName()+"."+i.getName().getName().getName());
    		i.getName().getMethodDefinition().setLabel(label);
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
    public void verifyClass(DecacCompiler compiler) throws ContextualError {
        SymbolTable.Symbol name = ident.getName();
        LOG.debug("Verify Class : start " + name.toString());
        if (!ident.getName().toString().equals("Object") && parent == null) {
            parent = new Identifier(compiler.getSymbols().create("Object"));
            parent.setDefinition(compiler.getEnv_types().get(parent.getName()));
        }

        SymbolTable.Symbol parentName = parent.getName();
        TypeDefinition parentType = compiler.getEnv_types().get(parentName);
        if (parentType == null) {
            throw new ContextualError(ContextualError.PARENT_CLASS_NOT_DECLARED, getLocation());
        }
        if (!parentType.isClass()) {
            throw new ContextualError(ContextualError.PARENT_CLASS_NOT_CLASS, getLocation());
        }
        parent.setDefinition(parentType);
        ClassType newClassType = new ClassType(name, getLocation(), (ClassDefinition)parentType);
        ClassDefinition newClassDef = new ClassDefinition(newClassType, getLocation(), (ClassDefinition) parentType);
        try {
            compiler.getEnv_types().declare(name, newClassDef);
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError(ContextualError.CLASS_ALREADY_DEFINED, getLocation());
        }
        // setting a definition for a ident of class
        ident.setDefinition(newClassDef);
        LOG.debug("Verify Class : end " + name.toString());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        SymbolTable.Symbol name = ident.getName();
        LOG.debug("Verify Class Members : start " + name.toString());
        if (!name.toString().equals("Object") &&   parent == null) {
            throw new DecacInternalError("Parent class is not set which is impossible at this state of the compilation checking");
        }
        // Check if super is in env type
        TypeDefinition classTypeDef = compiler.getEnv_types().get(name);
        if (classTypeDef == null) {
            throw new ContextualError(ContextualError.CLASS_NOT_IN_ENV, getLocation());
        }
        if (!classTypeDef.isClass()) {
            throw new ContextualError(ContextualError.CLASS_NOT_CLASS, getLocation());
        }
        ClassDefinition classDef = (ClassDefinition) classTypeDef;
        // Change classDef with definition of Methods and Fields
        if (parent != null) {
        	ident.getClassDefinition().setNumberOfFields(parent.getClassDefinition().getNumberOfFields());
        	ident.getClassDefinition().setNumberOfMethods(parent.getClassDefinition().getNumberOfMethods());
        }
        field.verifyListDeclField(compiler, classDef);
        method.verifyListDeclMethod(compiler, classDef);


        LOG.debug("Verify Class Members : end " + name.toString());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        SymbolTable.Symbol className = ident.getName();
        TypeDefinition classType = compiler.getEnv_types().get(className);
        // Useless if precedent turn was successful but double checking is never bad
        if (classType == null) {
            throw new ContextualError(ContextualError.CLASS_NOT_IN_ENV, getLocation());
        }
        if (!classType.isClass()) {
            throw new ContextualError(ContextualError.CLASS_NOT_CLASS, getLocation());
        }
        ClassDefinition classDef = (ClassDefinition) classType;
        field.verifyListDeclFieldBody(compiler, classDef);
        method.verifyListDeclMethodBody(compiler, classDef);
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
