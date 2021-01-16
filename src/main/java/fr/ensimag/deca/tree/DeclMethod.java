/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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
    
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListParam param, AbstractMethodBody body){
        Validate.notNull(type);
        Validate.notNull(name);
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
            classExp.declare(name.getName(), new MethodDefinition(type, getLocation(), sig, currentClass.getNumberOfMethods()));
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
