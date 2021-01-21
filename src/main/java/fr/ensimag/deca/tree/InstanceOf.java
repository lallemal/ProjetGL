package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr{
	private final AbstractExpr expr;
	private final AbstractIdentifier ident;
	
	public InstanceOf(AbstractExpr expr, AbstractIdentifier ident) {
		Validate.notNull(expr);
		Validate.notNull(ident);
		this.expr = expr;
		this.ident = ident;
	}
	
	@Override
	public void codeExp(DecacCompiler compiler, int n) {
		if (expr.isNew()) {
			New e = (New) expr;
			ClassType classe = (ClassType) e.getIdent().getType();
			boolean subClass = classe.isSubClassOf(((ClassType) ident.getType()));
			compiler.addInstruction(new LOAD(subClass?1:0, Register.getR(n)));
		} else if (expr.getType().isNull()) {
			compiler.addInstruction(new LOAD(1, Register.getR(n)));
		} 
		else {
			Label fin_false = new Label("fin_false_"+getLocation().getLine()+"_"+getLocation().getPositionInLine());
			Label fin_true = new Label("fin_true_"+getLocation().getLine()+"_"+getLocation().getPositionInLine());
			Label fin = new Label("fin_"+getLocation().getLine()+"_"+getLocation().getPositionInLine());
			Label boucle = new Label("while_instanceOf_"+getLocation().getLine()+"_"+getLocation().getPositionInLine());
			
			DAddr rightAddress = ident.getClassDefinition().getAddress();
			if (compiler.getRmax() > n) {
				compiler.addInstruction(new LEA(rightAddress, Register.getR(n+1)));
			} else {
				
			}
			if (expr.isIdentifier()) {
				Identifier a = (Identifier) expr;
				DAddr leftAddress = a.getExpDefinition().getOperand();
				compiler.addInstruction(new LEA(leftAddress, Register.getR(n)));
			} else {
				expr.codeExp(compiler, n);
			}
			
			compiler.addLabel(boucle);
			compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(n)), Register.getR(n)));
			compiler.addInstruction(new CMP(new NullOperand(), Register.getR(n)));
			compiler.addInstruction(new BEQ(fin_false));
			compiler.addInstruction(new CMP(Register.getR(n), Register.getR(n+1)));
			compiler.addInstruction(new BEQ(fin_true));
			compiler.addInstruction(new BRA(boucle));
			compiler.addLabel(fin_false);
			compiler.addInstruction(new LOAD(0, Register.getR(n)));
			compiler.addInstruction(new BRA(fin));
			compiler.addLabel(fin_true);
			compiler.addInstruction(new LOAD(1, Register.getR(n)));
			compiler.addLabel(fin);
		}
	}
	
	@Override
	protected void codeGenBranch(DecacCompiler compiler, boolean evaluate, Label label) {
        this.codeExp(compiler, 2);
        compiler.addInstruction(new CMP(0, Register.getR(2)));
        if (evaluate) {
            compiler.addInstruction(new BNE(label));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
    }
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
	    Type type1 = expr.verifyExpr(compiler, localEnv, currentClass);
	    Type type2 = ident.verifyType(compiler);
	    if (!type2.isClass() || !type1.isClassOrNull()) {
	    	throw new ContextualError(ContextualError.INSTANCE_OF_IMPOSSIBLE, getLocation());
		}
	    setType(compiler.getBool());
	    return compiler.getBool();
	}

	@Override
	public void decompile(IndentPrintStream s) {
                s.print("(");
		expr.decompile(s);
                s.print(" instanceof ");
                ident.decompile(s);
                s.print(")");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expr.prettyPrint(s, prefix, false);
                ident.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		expr.iter(f);
                ident.iter(f);
		
	}

}
