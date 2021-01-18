/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(DeclMethod.class);
    
    public boolean equals(DeclMethod other) {
    	boolean b1 = (type.getName().getName()).equals(other.getType().getName().getName());
    	boolean b2 = param.equals(other.getListParam());
    	return b1 && b2;
    }
    
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListParam param, AbstractMethodBody body){
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
        this.param = param;
        this.body = body;
    }
    
    public AbstractIdentifier getType() {
    	return type;
    }
    
    public AbstractIdentifier getName() {
    	return name;
    }
    
    public ListParam getListParam() {
    	return param;
    }
    
    @Override
    public void codeGenDeclMethod(DecacCompiler compiler) {
    	Label label = name.getMethodDefinition().getLabel();
    	compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getKGB(), Register.GB)));
    	compiler.incrementKGB();
    }
    
    @Override
    public void codeGenDeclMethodOverride(DecacCompiler compiler, DAddr address) {
    	Label label = name.getMethodDefinition().getLabel();
    	compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, address));
    }
    
    public void codeGenMethod(DecacCompiler compiler, String className) {
    	compiler.addComment("---------- Initialisation de la methode de "+name.getName().getName());
    	
    	compiler.addComment("sauvegarde des registres");
    	
    	
    	compiler.addComment("instructions");
    	Label labelFin = new Label("fin."+className+"."+name.getName().getName());
    	body.codeGenBody(compiler, labelFin);
    	// Fin : on verifie quil y a eu return si ce nest pas une void fonction
    	if (!type.getName().getName().equals("void")) {
    		compiler.addInstruction(new WSTR("Erreur : sortie de la methode "+className+"."+name.getName().getName()+" sans return"));
    		compiler.addInstruction(new WNL());
    		compiler.addInstruction(new ERROR());
    	}
    	
    	compiler.addLabel(labelFin);
    	for (AbstractDeclVar i : body.getVar().getList()) {
    		compiler.decrementKGB();
    	}
    	
    	compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        if (param != null){
            param.decompile(s);
        }
        s.println(") {");
        s.indent();
        if (body != null){
            body.decompile(s);
        }
        s.unindent();
        s.println("}");
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
        type.iter(f);
        name.iter(f);
        if (param != null){
            param.iterChildren(f);
        }
        if (body != null){
            body.iterChildren(f);
        }
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("Verify method " + name.getName().toString() + " for class " + currentClass.getType().getName() + " : start");
        Type type = this.type.verifyType(compiler);
        Signature sig = this.param.verifyDeclMethod(compiler);
        EnvironmentExp classExp = currentClass.getMembers();

        // If the method exists in super env
        if (classExp.get(name.getName()) != null) {
            ExpDefinition expMethodSuper = classExp.get(name.getName());
            if (!expMethodSuper.isMethod()) {
                throw new ContextualError(ContextualError.METHOD_SAME_IDENT_NOT_METHOD, getLocation());
            }
            MethodDefinition methodDef = (MethodDefinition) expMethodSuper;
            Signature sig2 = methodDef.getSignature();
            Type type2 = methodDef.getType();
            if (!sig.sameSignature(sig2)) {
                throw new ContextualError(ContextualError.METHOD_REDEF_NOT_SAME_SIG, getLocation());
            }
            if (!TypeOp.subType(compiler, type, type2)) {
                throw new ContextualError(ContextualError.METHOD_RETURN_TYPE_NOT_SUBTYPE_SUPER_METHOD, getLocation());
            }
        }
        try {
            MethodDefinition methodDefinition = new MethodDefinition(type, getLocation(), sig, currentClass.getNumberOfMethods());
            classExp.declare(name.getName(), methodDefinition);
            name.setDefinition(methodDefinition);
            currentClass.incNumberOfMethods();
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(ContextualError.METHOD_ALREADY_DEFINED_ENV, getLocation());
        }

        LOG.debug("Verify method " + name.getName().toString() + " for class " + currentClass.getType().getName() + " : end");

    }

    @Override
    public void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Type returnType = this.type.verifyType(compiler);
        // The parent is the class Env which is the class of the method (for now)
        EnvironmentExp envExpMethod = new EnvironmentExp(currentClass.getMembers());
        this.param.verifyDeclMethodBody(compiler, envExpMethod, currentClass);
        // TODO
        this.body.verifyMethodBody(compiler, envExpMethod, currentClass, returnType);
    }


}
