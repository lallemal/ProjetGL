/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author louise
 */
public class DeclField extends AbstractDeclField{
    
    private Visibility visib;
    private AbstractIdentifier type;
    private AbstractIdentifier nom;
    private AbstractInitialization init;

    private static final Logger LOG = Logger.getLogger(DeclField.class);
    
    public DeclField(Visibility visib, AbstractIdentifier type, AbstractIdentifier nom, AbstractInitialization init) {
        Validate.notNull(visib);
        Validate.notNull(type);
        Validate.notNull(nom);
        Validate.notNull(init);
        this.visib = visib;
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
        s.println(prefix+ "Visibility: " + visib);
        type.prettyPrint(s, prefix, false);
        nom.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify DeclField " + nom.getName().toString() + " for class " + currentClass.getType().getName().toString() + " : start");
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError(ContextualError.DECL_FIELD_VOID, getLocation());
        }
        SymbolTable.Symbol name = nom.getName();
        EnvironmentExp classEnv = currentClass.getMembers();
        if (classEnv.get(name) != null && !classEnv.get(name).isField()) {
            throw new ContextualError(ContextualError.FIELD_PARENT_NOT_FIELD, getLocation());
        }
        try {
            classEnv.declare(name, new FieldDefinition(type, getLocation(), visib, currentClass, currentClass.getNumberOfFields()));
            currentClass.incNumberOfFields();
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(ContextualError.FIELD_ALREADY_DEFINED, getLocation());
        }

        LOG.debug("verify DeclField " + nom.getName().toString() + " for class " + currentClass.getType().getName().toString() + " : end");
    }
    
}
