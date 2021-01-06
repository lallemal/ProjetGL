package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentType {
    EnvironmentType parentEnvironment;
    Map<Symbol, TypeDefinition> map;

    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        map = new HashMap<>();
    }

    public EnvironmentType getParentEnvironment() {
        return parentEnvironment;
    }



    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the TypeDefinition linked to key if exists or null otherwise
     * @param key   Symbol to find the TypeDefinition
     * @return      null if key is not in the Map or the TypeDefinition linked to key
     */
    public TypeDefinition get(Symbol key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    /**
     * Define a new association between a Symbol and a TypeDefinition. If it already exists, throw DoubleDefException
     * @param key                   The Symbol to linked
     * @param typeDef               The TypeDefinition
     * @throws DoubleDefException   The exception raised if the key already exists
     */
    public void declare(Symbol key, TypeDefinition typeDef) throws DoubleDefException {
        if (map.containsKey(key)) {
            throw new DoubleDefException();
        }
        map.put(key, typeDef);

    }

}
