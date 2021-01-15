package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class LabelError {

	private boolean errorDiv0;
	private Label div0;
	private boolean errorMod0;
	private Label mod0;
	private boolean errorReadInt;
	private Label errorRINT;
	private boolean errorReadFloat;
	private Label errorRFLOAT;
	private boolean errorConvFloat;
	private Label errorFLOAT;
	private boolean errorPilePleine;
	private Label pilePleine;
	
	public void setErrorDiv0(boolean b) {
		errorDiv0 = b;
	}
	
	public Label getLabelErrorDiv0() {
		return div0;
	}
	
	public void setErrorMod0(boolean b) {
		errorMod0 = b;
	}
	
	public Label getLabelErrorMod0() {
		return mod0;
	}
	
	public void setErrorReadInt(boolean b) {
		errorReadInt = b;
	}
	
	public Label getLabelErrorRINT() {
		return errorRINT;
	}
	
	public void setErrorReadFloat(boolean b) {
		errorReadFloat = b;
	}
	
	public Label getLabelErrorRFLOAT() {
		return errorRFLOAT;
	}
	
	public void setErrorConvFloat(boolean b) {
		errorConvFloat = b;
	}
	
	public Label getLabelErrorFLOAT() {
		return errorFLOAT;
	}
	
	public void setErrorPilePleine(boolean b) {
		errorPilePleine = b;
	}
	
	public Label getLabelPilePleine() {
		return pilePleine;
	}
	
	public LabelError() {
		errorDiv0 = false;
		div0 = new Label("division_par_zero");
		errorMod0 = false;
		mod0 = new Label("modulo_zero");
		errorReadInt = false;
		errorRINT = new Label("error_read_int");
		errorReadFloat = false;
		errorRFLOAT = new Label("error_read_float");
		errorConvFloat = false;
		errorFLOAT = new Label("error_conv_float");
		errorPilePleine = false;
		pilePleine = new Label("pile_pleine");
	}
	
	public void codeGenLabelError(DecacCompiler compiler) {
		if (errorDiv0) {
			addError(compiler, div0, "division par zero", "Erreur : division par zero impossible");
		}
		if (errorMod0) {
			addError(compiler, mod0, "modulo zero", "Erreur : reste entier par zero impossible");
		}
		if (errorReadInt) {
			addError(compiler, errorRINT, "lecture entier", "Erreur : un entier est attendu");
		}
		if (errorReadFloat) {
			addError(compiler, errorRFLOAT, "lecture flottant", "Erreur : un float est attendu");
		}
		if (errorConvFloat) {
			addError(compiler, errorFLOAT, "conversion flottant", "Erreur : V[dval] non codable sur un flottant");
		}
		if (errorPilePleine) {
			addError(compiler, pilePleine, "debordement de pile", "Erreur : debordement de pile");
		}
	}
	
	public void addErrorTitle(DecacCompiler compiler, String title) {
		compiler.addComment("----------------------------------------------");
		compiler.addComment("	Message d'erreur : " + title);
		compiler.addComment("----------------------------------------------");
	}
	
	public void addError(DecacCompiler compiler, Label label, String title, String erreur) {
		
		addErrorTitle(compiler, title);
		compiler.addLabel(label);
		compiler.addInstruction(new WSTR(erreur));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
	}
	
	
	
}
