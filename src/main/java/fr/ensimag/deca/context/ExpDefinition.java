package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.ArrayList;


/**
 * Definition associated to identifier in expressions.
 *
 * @author gl40
 * @date 01/01/2021
 */
public abstract class ExpDefinition extends Definition {

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public DAddr getOperand() {
        return operand;
    }
    
    private DAddr operand;

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }
    public int getDimension() {
        return dimensions.size();
    }

    public int getDimIndex(int index)  {
        return dimensions.get(index);
    }

    private ArrayList<Integer> dimensions;

    public ExpDefinition(Type type, Location location) {
        super(type, location);
        dimensions = new ArrayList<>();
    }

}
