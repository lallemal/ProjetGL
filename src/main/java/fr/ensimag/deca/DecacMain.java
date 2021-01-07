package fr.ensimag.deca;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import java.util.concurrent.*;
/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
    	
        // example log4j message.
        
        //Test pas ouf pour voir git 
    
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        
        try {
            options.parseArgs(args);
            
        } catch (CLIException e) {
        	
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
        	System.out.println("groupe 8 gl40");
            //throw new UnsupportedOperationException("decac -b not yet implemented");
        	// sort après affichage de la banniere
        	System.exit(1);
        }
        
        if (options.getSourceFiles().isEmpty()) {
        	options.displayUsage();
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
        }
        if (options.getParallel()) {
        	for (File source: options.getSourceFiles()) {
        		 DecacCompiler compiler = new DecacCompiler(options, source);
        	}
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
   
    }
    
}
