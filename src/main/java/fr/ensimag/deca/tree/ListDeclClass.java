package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
    	/**
    	if (this.size() > 0) {
    		AbstractIdentifier ident = new Identifier(compiler.getSymbols().create("Object"));
    		AbstractIdentifier type = new Identifier(compiler.getSymbols().create("boolean"));
    		AbstractIdentifier nom = new Identifier(compiler.getSymbols().create("equals"));
    		ListParam param = new ListParam();
    		Identifier other = new Identifier(compiler.getSymbols().create("other"));
    		param.add(new DeclParam(new Identifier(compiler.getSymbols().create("Object")), other));
    		ListDeclVar var = new ListDeclVar();
    		ListInst inst = new ListInst();
    		AbstractExpr exp = new Equals(new This(), other);
    		Return ret = new Return(exp);
    		inst.add(ret);
    		AbstractMethodBody body = new MethodBody(var, inst);
    		AbstractDeclMethod equals = new DeclMethod(type, nom, param, body);
    		ListDeclMethod listMethod = new ListDeclMethod();
    		listMethod.add(equals);
    		this.add(new DeclClass(ident, null, new ListDeclField(), listMethod));
    	}*/
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers : end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody : end");
    }
    
    /**
     * Step C
     */
    public void codeGenDeclClass(DecacCompiler compiler) {
    	for (AbstractDeclClass i : getList()) {
            i.codeGenDeclClass(compiler);
        }
    }


}
