package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.RecognitionException;

public class InvalidFloatBig extends DecaRecognitionException {
	public InvalidFloatBig(AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
    }
    
    @Override
    public String getMessage() {
        return "Erreur de compilation: flottant arrondi a l'infini";
    }
    
}
