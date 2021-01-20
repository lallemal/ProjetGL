package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.RecognitionException;

public class InvalidFloatSmall extends DecaRecognitionException {
	public InvalidFloatSmall(AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
    }
    
    @Override
    public String getMessage() {
        return "Erreur de compilation: flottant arrondi 0";
    }
    
}
