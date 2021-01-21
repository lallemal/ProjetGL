package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class LabelClass {
	
	public void codeGenLabelClass(DecacCompiler compiler, ListDeclClass classes) {
		if (classes.size() > 0) {
			codeGenLabelDeclClass(compiler, compiler.getObject());
		}
		for (AbstractDeclClass i : classes.getList()) {
            codeGenLabelDeclClass(compiler, i);
        }
	}
	
	public void addClassTitle(DecacCompiler compiler, String className) {
		compiler.addComment("----------------------------------------------");
		compiler.addComment("	Classe : " + className);
		compiler.addComment("----------------------------------------------");
	}
	
	public void codeGenLabelDeclClass(DecacCompiler compiler, AbstractDeclClass i) {
		String className = i.getIdent().getName().getName();
		addClassTitle(compiler, className);
		compiler.addComment("---------- Initialisation des champs de "+className);
		Label labelInit = i.getIdent().getClassDefinition().getLabelInit();
		compiler.addLabel(labelInit);
		if (!i.getIdent().getClassDefinition().getIsClassObject() && !i.getIdent().getClassDefinition().getSuperClass().getIsClassObject()) {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
			compiler.getLabelError().setErrorPilePleine(true);
	    	compiler.addInstruction(new TSTO(1));
	    	if (!compiler.getCompilerOptions().isNoCheck()) {
	    		compiler.addInstruction(new BOV(compiler.getLabelError().getLabelPilePleine()));
	    	}
	    	compiler.addInstruction(new ADDSP(1));
	    	compiler.addInstruction(new PUSH(Register.R0));
			compiler.addInstruction(new BSR(i.getIdent().getClassDefinition().getSuperClass().getLabelInit()));
			compiler.addInstruction(new SUBSP(1));
		}
		i.getField().codeGenField(compiler);
		i.getMethod().codeGenListMethod(compiler, className);
	}
	
}
