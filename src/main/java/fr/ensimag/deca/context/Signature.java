package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl40
 * @date 01/01/2021
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public boolean sameSignature(Signature sig2) {
        if (size() != sig2.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!args.get(i).sameType(sig2.paramNumber(i))) {
                return false;
            }
        }
        return true;
    }

}
