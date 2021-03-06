package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl40
 * @date 01/01/2021
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    private static final Logger LOG = Logger.getLogger(ListDeclVar.class);
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar i : getList()) {
            i.decompile(s);
            s.println();
        };
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ListDeclVar : start");
        for (AbstractDeclVar var : getList()) {
            var.verifyDeclVar(compiler, localEnv, currentClass);
        }
        LOG.debug("verify ListDeclVar : end");
    }
    
    public void codeGenListDecl(DecacCompiler compiler) {
        for (AbstractDeclVar d : getList()) {
            d.codeGenDecl(compiler);
        }
    }
    public void codeGenListDeclInMethod(DecacCompiler compiler) {
        compiler.resetKLB();
        for (AbstractDeclVar d : getList()) {
            d.codeGenDeclInMethod(compiler);
        }
    }

}
