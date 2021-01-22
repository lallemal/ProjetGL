package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * 
 * @author gl40
 * Classe permettant la gestion des erreurs dans le code assembleur.
 * Les booleens permettent de savoir si une erreur est envisageable (ie si une instruction a pu la provoquer)
 * En consequence on cree un label en fin de fichier assembleur grace à la methode codeGenLabelError appelee par
 * le compilateur.
 */
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
	private boolean errorConvInt;
	private Label errorINT;
	private boolean errorPilePleine;
	private Label pilePleine;
	private boolean errorTasPlein;
	private Label tasPlein;
	private boolean errorDereferencementNull;
	private Label dereferencementNull;
<<<<<<< HEAD
	private boolean errorIndexOutOfRange;
	private Label indexOutOfRange;
=======
	private boolean errorCastFail;
	private Label castFail;
	
	public void setAllTrue() {
		this.setErrorCastFail(true);
		this.setErrorDereferencementNull(true);
		this.setErrorTasPlein(true);
		this.setErrorPilePleine(true);
		this.setErrorConvInt(true);
		this.setErrorConvFloat(true);
		this.setErrorReadFloat(true);
		this.setErrorReadInt(true);
		this.setErrorMod0(true);
		this.setErrorDiv0(true);
	}
>>>>>>> objet-gencode
	
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
	
	public void setErrorConvInt(boolean b) {
		errorConvInt = b;
	}
	
	public Label getLabelErrorINT() {
		return errorINT;
	}
	
	public void setErrorPilePleine(boolean b) {
		errorPilePleine = b;
	}
	
	public Label getLabelPilePleine() {
		return pilePleine;
	}
	
	public void setErrorTasPlein(boolean b) {
		errorTasPlein = b;
	}
	
	public Label getLabelTasPlein() {
		return tasPlein;
	}
	
	public void setErrorDereferencementNull(boolean b) {
		errorDereferencementNull = b;
	}
	
	public Label getLabelDereferencementNull() {
		return dereferencementNull;
	}
	
	public void setErrorIndexOutOfRange(boolean b) {
		errorIndexOutOfRange = b;
	}
	
	public Label getLabelIndexOutOfRange() {
		return indexOutOfRange;

	public void setErrorCastFail(boolean b) {
		errorCastFail = b;
	}
	
	public Label getLabelCastFail() {
		return castFail;
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
		errorConvInt = false;
		errorINT = new Label("error_conv_int");
		errorPilePleine = false;
		pilePleine = new Label("pile_pleine");
		errorTasPlein = false;
		tasPlein = new Label("tas_plein");
		errorDereferencementNull = false;
		dereferencementNull = new Label("dereferencement_null");
		errorIndexOutOfRange = false;
		indexOutOfRange = new Label("index_out_of_range");
		errorCastFail = false;
		castFail = new Label("cast_fail");
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
		if (errorConvInt) {
			addError(compiler, errorINT, "conversion entier", "Erreur : V[dval] non codable sur un entier");
		}
		if (errorPilePleine) {
			addError(compiler, pilePleine, "debordement de pile", "Erreur : debordement de pile");
		}
		if (errorTasPlein) {
			addError(compiler, tasPlein, "debordement de tas", "Erreur : tas plein");
		}
		if (errorDereferencementNull) {
			addError(compiler, dereferencementNull, "dereferencement de null", "Erreur : dereferencement de null");
		}
		if (errorIndexOutOfRange) {
			addError(compiler, indexOutOfRange, "index out of range", "Erreur : index out of range");
		}
		if (errorCastFail) {
			addError(compiler, castFail, "cast impossible", "Erreur : cast impossible");
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
