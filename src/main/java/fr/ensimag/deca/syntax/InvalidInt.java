package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.RecognitionException;

public class InvalidInt extends DecaRecognitionException {
	public InvalidInt(AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
    
    }
    
 
    @Override
    public String getMessage() {
        return "Erreur de compilation: Int arrondi a l'infini"; 
    }
    
}
