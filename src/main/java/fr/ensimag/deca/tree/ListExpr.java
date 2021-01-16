package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        int j = 0;
        for (AbstractExpr i: getList()){
            i.decompile(s);
            if (j != size()-1){
                s.print(", ");
            }
            j ++; 
        };
    }

    public void verifyRvalueStar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Signature sig) throws ContextualError {
        if (sig.size() == 0) {
            return;
        }
        if (sig.size() != getList().size()) {
            throw new ContextualError(ContextualError.SIG_LIST_EXPR_NOT_MATCH, getLocation());
        }
        for (int i = 0; i < sig.size(); i++) {
            this.getList().get(i).verifyRValue(compiler, localEnv, currentClass, sig.paramNumber(i));
        }
    }
}
