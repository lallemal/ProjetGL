package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.deca.context.Type;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // TODO LeftOperand is lvalue 3.64 certainly lvalue class has to be completed
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
        if (type.isArray()) {
            getLeftOperand().setDimension(getRightOperand().getDimension());
        }

        setType(type);
        return type;
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	compiler.setRegistreUsed(2);
    	AbstractExpr e = this.getRightOperand(); 
        e.codeExp(compiler, 2);
    	if (this.getLeftOperand().isIdentifier()) {
	        Identifier x = (Identifier) this.getLeftOperand();
	        if (x.getExpDefinition().isField()) { // si jamais on a x = ... avec x un field, on est forcement dans une methode..
	        	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(3)));
	        	compiler.setRegistreUsed(3);
	        	int index = x.getFieldDefinition().getIndex()+1;
	        	compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(index, Register.getR(3))));
	        } else {
	        	compiler.addInstruction(new STORE(Register.getR(2), x.getExpDefinition().getOperand()));
	        }
    	} else if (this.getLeftOperand().isSelection()) {
    		Selection select = (Selection) this.getLeftOperand();
	        int index = select.getIdent().getFieldDefinition().getIndex()+1;
	        if (select.getExpr().isThis()) {
	        	compiler.setRegistreUsed(3);
	        	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(3)));
	        	if (!compiler.getCompilerOptions().isNoCheck()) {
	        		compiler.getLabelError().setErrorDereferencementNull(true);
	        		compiler.addInstruction(new CMP(new NullOperand(), Register.getR(3)));
	        		compiler.addInstruction(new BEQ(compiler.getLabelError().getLabelDereferencementNull()));
	        	}
	        	compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(index, Register.getR(3))));
	        }
	        else if (select.getExpr().isNew()) { // cas : (new A()).x = y;
	        	//nothing to do (new A() is going to disappear at the end of the line)
	        }
	        else if (select.getExpr().isIdentifier()) {
	        	Identifier a = (Identifier) select.getExpr();
	        	compiler.setRegistreUsed(2);
	        	compiler.addInstruction(new LOAD(a.getExpDefinition().getOperand(), Register.getR(3)));
	        	compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(index, Register.getR(3))));
	        }
    	} else if (this.getLeftOperand().isArraySelection()) {
			ArraySelection selectA = (ArraySelection) this.getLeftOperand();
			selectA.codeExp(compiler, 3, true); // true permet de Load l'adresse et non la valeur 
			compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(0, Register.getR(3))));
		}
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
