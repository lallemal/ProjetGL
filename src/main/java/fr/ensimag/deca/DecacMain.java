package fr.ensimag.deca;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        	System.exit(0);
        }
        
        if (options.getSourceFiles().isEmpty()) {
        	options.displayUsage();
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
        }
        if (options.getParallel()) {
            ExecutorService executor = Executors.newFixedThreadPool(java.lang.Runtime.getRuntime().availableProcessors());
            ArrayList<Future<Boolean>> futures = new ArrayList<>();
        	for (File source: options.getSourceFiles()) {
        		 Future<Boolean> future = executor.submit(new DecacCompiler(options, source));
        		 futures.add(future);
        	}
        	for (Future<Boolean> future : futures) {
                try {
                    if (future.get()) {
                        error = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    error = true;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    error = true;
                }
            }
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
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
