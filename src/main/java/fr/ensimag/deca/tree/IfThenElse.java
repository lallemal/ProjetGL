package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Full if/else if/else statement.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("If Else instruction");
        String label = "if_";
        String pos = getLocation().getLine() + "_" + getLocation().getPositionInLine();
        Label sinonLabel = new Label(label + "Sinon." + pos);
        Label finLabel = new Label(label + "Fin." + pos);
        if (!elseBranch.isEmpty()) {
            condition.codeGenBranch(compiler, false, sinonLabel);
        } else {
            condition.codeGenBranch(compiler, false, finLabel);
        }
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(finLabel));
        if (!elseBranch.isEmpty()) {
            compiler.addLabel(sinonLabel);
            elseBranch.codeGenListInst(compiler);
        }
        compiler.addLabel(finLabel);
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler, Label labelFin) {
    	compiler.addComment("If Else instruction");
        String label = "if_";
        String pos = getLocation().getLine() + "_" + getLocation().getPositionInLine();
        Label sinonLabel = new Label(label + "Sinon." + pos);
        Label finLabel = new Label(label + "Fin." + pos);
        if (!elseBranch.isEmpty()) {
            condition.codeGenBranch(compiler, false, sinonLabel);
        } else {
            condition.codeGenBranch(compiler, false, finLabel);
        }
        thenBranch.codeGenListInst(compiler, labelFin);
        compiler.addInstruction(new BRA(finLabel));
        if (!elseBranch.isEmpty()) {
            compiler.addLabel(sinonLabel);
            elseBranch.codeGenListInst(compiler, labelFin);
        }
        compiler.addLabel(finLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if ");
        condition.decompile(s);
        s.println(" {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("}");
        s.println("else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
