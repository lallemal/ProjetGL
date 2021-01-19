package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;

public class ArrayType extends Type {
    private Type baseType;
    private int dimension;

    public ArrayType(SymbolTable.Symbol name, Type baseType, int dimension) throws ContextualError {
        super(name);
        this.baseType = baseType;
        if (dimension == 0) {
            // Location is not ok
            throw new ContextualError(ContextualError.ARRAY_DIM_NULL, Location.BUILTIN);
        }
        this.dimension = dimension;
    }

    public Type getBaseType() {
        return baseType;
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        if (!otherType.isArray()) {
            return false;
        }
        ArrayType otherTypeArray = (ArrayType) otherType;
        return otherTypeArray.getBaseType().sameType(baseType) && otherTypeArray.getDimension() == dimension;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
