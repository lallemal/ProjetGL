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
public class DeclParam extends AbstractDeclParam{
    
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    private static final Logger LOG = Logger.getLogger(DeclParam.class);

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name){
        Validate.notNull(type);
        Validate.notNull(type);
        this.type = type;
        this.name = name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        LOG.debug("Verify Param " + name.getName() + " : start");
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError(ContextualError.PARAM_TYPE_VOID, getLocation());
        }
        LOG.debug("Verify Param " + name.getName() + " : end");
        return type;
    }

    @Override
    public void verifyDeclParamBody(DecacCompiler compiler, EnvironmentExp localExp, ClassDefinition currentClass) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        try {
            ParamDefinition paramDefinition = new ParamDefinition(type, getLocation());
            localExp.declare(this.name.getName(), paramDefinition);
            name.setDefinition(paramDefinition);
        } catch (EnvironmentExp.DoubleDefException e) {
           throw new ContextualError(ContextualError.PARAM_ALREADY_ENV_METHOD, getLocation());
        }
    }


}
