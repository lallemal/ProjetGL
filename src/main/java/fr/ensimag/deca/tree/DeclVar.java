package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * @author gl40
 * @date 01/01/2021
 */
public class DeclVar extends AbstractDeclVar {

    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclVar : start " + varName.decompile() );
        // verify if the type is correct and return it
        Type typeToCheck = this.type.verifyType(compiler);
        // raise error in case of void type for the variable
        if (typeToCheck.isVoid()) {
            throw new ContextualError(ContextualError.DECL_VAR_VOID, getLocation());
        }
        SymbolTable.Symbol name = varName.getName();
        // Check the possibility of the initialization : compatibility between the two members
        initialization.verifyInitialization(compiler, typeToCheck, localEnv, currentClass);
        // Do the union : reject and raise error if it already exists in this env
        try {
            VariableDefinition varDef = new VariableDefinition(typeToCheck, getLocation());
            localEnv.declare(name, varDef);
            this.varName.setDefinition(varDef);
            if (varDef.getType().isArray()) {
                varDef.setDimensions(initialization.getDimension());
            }
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(ContextualError.DEFINITION_ALREADY_IN_ENV, getLocation());
        }
        LOG.debug("Verify DeclVar : end");
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
    	varName.getExpDefinition().setOperand(new RegisterOffset(compiler.getKGB(), Register.GB));
    	compiler.incrementKGB();
    	compiler.incrementKSP();
    	initialization.codeGenDecl(compiler, varName.getExpDefinition().getOperand());
    }

    @Override
    protected void codeGenDeclInMethod(DecacCompiler compiler) {
        varName.getExpDefinition().setOperand(new RegisterOffset(compiler.getkLB(), Register.LB));
        compiler.incrementKSP();
        compiler.incrementKLB();
        initialization.codeGenDecl(compiler, varName.getExpDefinition().getOperand());
    }
    @Override
    public void decompile(IndentPrintStream s) {
    	type.decompile(s);
        s.print(" " + varName.getName());
        initialization.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
