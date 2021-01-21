package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class New extends AbstractExpr {

	private final AbstractIdentifier ident;
	
	public New(AbstractIdentifier operand) {
		Validate.notNull(operand);
	    this.ident = operand;
	}
	
	public AbstractIdentifier getIdent() {
		return ident;
	}
	
	@Override
	protected void codeExp(DecacCompiler compiler, int n) {
		compiler.getLabelError().setErrorTasPlein(true);
		int nbField = ident.getClassDefinition().getNumberOfFields();
		compiler.addInstruction(new NEW(new ImmediateInteger(nbField+1+ident.getClassDefinition().getSuperClass().getNumberOfFields()), Register.getR(n)));
		compiler.addInstruction(new BOV(compiler.getLabelError().getLabelTasPlein()));
		compiler.addInstruction(new LEA(ident.getClassDefinition().getAddress(), Register.R1));
		compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(0, Register.getR(n))));
		compiler.incrementKSP();
		compiler.addInstruction(new PUSH(Register.getR(n)));
		compiler.incrementKSP(2);
		compiler.addInstruction(new BSR(ident.getClassDefinition().getLabelInit()));
		compiler.decrementKSP(2);
		compiler.decrementKSP();
		compiler.addInstruction(new POP(Register.getR(n)));
	}
	
	@Override
    public boolean isNew() {
    	return true;
    }

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type type = ident.verifyType(compiler);
		if (!type.isClass()) {
			throw new ContextualError(ContextualError.TYPE_NOT_CLASS, getLocation());
		}
		setType(type);
		return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("(new " + ident.decompile() +"())");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		ident.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		ident.iter(f);
	}
}
