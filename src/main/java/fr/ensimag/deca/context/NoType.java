package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

public class NoType extends Type {
    public NoType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isNoType() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isNoType();

    }
}
