package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.ListDeclClass;

public class LabelClass {
	
	public void codeGenLabelClass(DecacCompiler compiler, ListDeclClass classes) {
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
		i.getField().codeGenField(compiler);		
	}
	
}
