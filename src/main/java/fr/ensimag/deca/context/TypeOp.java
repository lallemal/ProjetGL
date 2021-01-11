package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;

public class TypeOp {
    static public Type arith(DecacCompiler compiler, Type type1, Type type2) {
        if (type1.isInt() && type2.isInt()) {
            return compiler.getInt();
        }
        else {
            return compiler.getFloat();
        }
    }
}
