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


    static public boolean subType(DecacCompiler compiler, Type type1, Type type2) {
        if (type1.isNull()) {
            return true;
        }
        if (type1.sameType(type2)) {
            return true;
        }
        if (type1.isClass() && type2.getName().toString().equals("Object")) {
            return true;
        }
        if (type1.isClass()) {
            ClassType classType1 = (ClassType) type1;
            TypeDefinition classExpDef = compiler.getEnv_types().get(classType1.getName());
            ClassDefinition classDef = (ClassDefinition) classExpDef;
            ClassDefinition superDef = classDef.getSuperClass();
            if (subType(compiler, superDef.getType(), type2)) {
                return true;
            }
        }
        if (!type1.isClass() || !type2.isClass()) {
            return false;
        }
        ClassType classType1 = (ClassType) type1;
        TypeDefinition classExpDef = compiler.getEnv_types().get(classType1.getName());
        ClassDefinition classDef = (ClassDefinition) classExpDef;
        ClassType classType2 = (ClassType) type2;
        if (classType1.isSubClassOf(classType2)) {
            return true;
        }
        return false;
    }


    static public boolean assignComp(DecacCompiler compiler, Type type1, Type type2) {
        if (type1.isFloat() && type2.isInt()) {
            return true;
        }
        return subType(compiler, type1, type2);
    }


    static public boolean castComp(DecacCompiler compiler, Type type1, Type type2) {
        return   (!type1.isVoid()) && (assignComp(compiler, type1, type2) || assignComp(compiler, type2, type1));
    }
}
