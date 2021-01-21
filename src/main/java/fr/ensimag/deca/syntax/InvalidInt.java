package fr.ensimag.deca.syntax;
import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidInt extends DecaRecognitionException {


    public InvalidInt(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    
 
    @Override
    public String getMessage() {
        return "Erreur de compilation: Int arrondi a l'infini"; 
    }
    
}
