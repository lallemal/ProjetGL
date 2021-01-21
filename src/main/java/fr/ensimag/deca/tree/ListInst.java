package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import org.apache.log4j.Logger;

/**
 * 
 * @author gl40
 * @date 01/01/2021
 */
public class ListInst extends TreeList<AbstractInst> {

    private static final Logger LOG = Logger.getLogger(Main.class);
    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("Verify ListInst : start");
        for (AbstractInst i : getList()){
            // TODO faire Return
            i.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        LOG.debug("Verify ListInst : stop");
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler);
        }
    }
    
    public void codeGenListInst(DecacCompiler compiler, Label labelFin) {
        for (AbstractInst i : getList()) {
        	if (i.isReturn()) {
        		((Return) i).codeGenInst(compiler, labelFin);
        	} else {
        		i.codeGenInst(compiler);
        	}
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
