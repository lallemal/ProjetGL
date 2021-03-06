package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;



    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }
    
    @Override
    protected void codeGenDecl(DecacCompiler compiler, DAddr address) {
    	expression.codeGenDecl(compiler, address);
    }
    
    @Override
    protected void codeGenField(DecacCompiler compiler, AbstractIdentifier type) {
    	expression.codeGenField(compiler);
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        setExpression(expression.verifyRValue(compiler, localEnv, currentClass, t));
        setDimension(expression.getDimension());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("=");
        expression.decompileInst(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
