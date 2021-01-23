package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclVarArray extends AbstractDeclVar{
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    final private AbstractExpr length;
    
	public DeclVarArray(AbstractIdentifier type, AbstractExpr length, AbstractIdentifier varName, AbstractInitialization initialization) {
		this.type = type;
		this.length = length;
		this.varName = varName;
		this.initialization = initialization;
	}
	
	@Override
	protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void codeGenDecl(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decompile(IndentPrintStream s) {
		type.decompile(s);
                s.print("[");
                if (length != null){
                    length.decompile(s);
                }
                s.print("]");
                s.print(" ");
                varName.decompile(s);
                initialization.decompile(s);
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        length.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
                length.iter(f);
                varName.iter(f);
                initialization.iter(f);
		
	}

}
