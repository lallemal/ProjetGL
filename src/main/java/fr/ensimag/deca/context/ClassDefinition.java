package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.ListDeclMethod;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ClassDefinition extends TypeDefinition {

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    private Label labelInit;
    
    public void setLabelInit(Label label) {
    	labelInit = label;
    }
    
    public Label getLabelInit() {
    	return labelInit;
    }
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 
    private DAddr address;
    private ListDeclMethod methods;
    private boolean isClassObject = false;
    
    public void setIsClassObject(boolean b) {
    	isClassObject = b;
    }
    
    public boolean getIsClassObject() {
    	return isClassObject;
    }
    
    public void setMethods(ListDeclMethod methods) {
    	this.methods = methods;
    }
    
    public ListDeclMethod getMethods() {
    	return methods;
    }
    
    public void setAddress(DAddr address) {
    	this.address = address;
    }
    
    public DAddr getAddress() {
    	return address;
    }

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
        this.methods = new ListDeclMethod();
    }

}
