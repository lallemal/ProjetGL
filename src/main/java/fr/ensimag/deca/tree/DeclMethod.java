/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    
    public boolean equals(DeclMethod other) {
    	boolean b1 = (type.getName().getName()).equals(other.getType().getName().getName());
    	boolean b2 = param.equals(other.getListParam());
    	return b1 && b2;
    }
    
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListParam param, AbstractMethodBody body){
        assert(type != null);
        assert(name != null);
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
    	Label label = new Label(name.getName().getName());
    	compiler.addLabel(label);
    	compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getKGB(), Register.GB)));
    	compiler.incrementKGB();
    }
    
    @Override
    public void codeGenDeclMethodOverride(DecacCompiler compiler, DAddr address) {
    	Label label = new Label(name.getName().getName());
    	compiler.addLabel(label);
    	compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, address));
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
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
