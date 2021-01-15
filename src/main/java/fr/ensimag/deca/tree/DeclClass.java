package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
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
        this.parent = parent;
        this.field = field;
        this.method = method;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    public void verifyClass(DecacCompiler compiler) throws ContextualError {
        SymbolTable.Symbol name = ident.getName();
        LOG.debug("Verify Class : start " + name.toString());
        if (parent == null) {
            parent = new Identifier(compiler.getSymbols().create("Object"));
        }

        SymbolTable.Symbol parentName = parent.getName();
        TypeDefinition parentType = compiler.getEnv_types().get(parentName);
        if (parentType == null) {
            throw new ContextualError(ContextualError.PARENT_CLASS_NOT_DECLARED, getLocation());
        }
        if (!parentType.isClass()) {
            throw new ContextualError(ContextualError.PARENT_CLASS_NOT_CLASS, getLocation());
        }
        ClassType newClassType = new ClassType(name, getLocation(), (ClassDefinition)parentType);
        ClassDefinition newClassDef = new ClassDefinition(newClassType, getLocation(), (ClassDefinition) parentType);
        try {
            compiler.getEnv_types().declare(name, newClassDef);
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError(ContextualError.CLASS_ALREADY_DEFINED, getLocation());
        }
        LOG.debug("Verify Class : end " + name.toString());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        SymbolTable.Symbol name = ident.getName();
        LOG.debug("Verify Class Members : start " + name.toString());
        if (parent == null) {
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
        field.verifyListDeclField(compiler, classDef);
        method.verifyListDeclMethod(compiler, classDef);


        LOG.debug("Verify Class Members : end " + name.toString());
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
        // parent.iter(f);
        // field.iter(f);
        // method.iter(f);
    }

}
