package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;


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

    private ListExpr dimensions;

    public ListExpr getDimensions() {
        return dimensions;
    }

    public void setDimensions(ListExpr dimensions) {
        Validate.notNull(dimensions);
        this.dimensions = dimensions;
    }

    public ExpDefinition(Type type, Location location) {
        super(type, location);
        dimensions = new ListExpr();
    }

}
