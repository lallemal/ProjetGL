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
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    private AbstractIdentifier name;
    private AbstractInitialization init;

    private static final Logger LOG = Logger.getLogger(DeclField.class);
    
    public DeclField(Visibility visib, AbstractIdentifier type, AbstractIdentifier nom, AbstractInitialization init) {
        Validate.notNull(visib);
        Validate.notNull(type);
        Validate.notNull(nom);
        Validate.notNull(init);
        this.visib = visib;
        this.type = type;
        this.name = nom;
        this.init = init;
    }
    
    public void codeGenField(DecacCompiler compiler, int i) {
    	init.codeGenField(compiler, type);
    	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1)); // Peut surement etre factorise dans ListDeclField
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(i, Register.R1)));
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(visib + " ");
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
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

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify DeclField " + name.getName().toString() + " for class " + currentClass.getType().getName().toString() + " : start");
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError(ContextualError.DECL_FIELD_VOID, getLocation());
        }
        SymbolTable.Symbol nom = name.getName();
        EnvironmentExp classEnv = currentClass.getMembers();
        if (classEnv.get(nom) != null && !classEnv.get(nom).isField()) {
            throw new ContextualError(ContextualError.FIELD_PARENT_NOT_FIELD, getLocation());
        }
        try {
            FieldDefinition fieldDef = new FieldDefinition(type, getLocation(), visib, currentClass, currentClass.getNumberOfFields());
            classEnv.declare(nom, fieldDef);
            name.setDefinition(fieldDef);
            currentClass.incNumberOfFields();
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(ContextualError.FIELD_ALREADY_DEFINED, getLocation());
        }

        LOG.debug("verify DeclField " + nom.getName().toString() + " for class " + currentClass.getType().getName().toString() + " : end");
    }


    @Override
    protected void verifyDeclFieldBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        init.verifyInitialization(compiler, type, currentClass.getMembers(), currentClass);
    }
}
