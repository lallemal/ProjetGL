package fr.ensimag.deca.context;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 * 
 * @author Ensimag
 * @date 01/01/2021
 */
public class ManualTestContextNoDebug {
    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.FATAL);
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        parser.setDecacCompiler(compiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        if (prog == null) {
            System.exit(1);
            return; // Unreachable, but silents a warning.
        }
        try {
            prog.checkAllLocations();
            prog.verifyProgram(compiler);
            //prog.checkAllDecorations();
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }
        prog.prettyPrint(System.out);
    }
}