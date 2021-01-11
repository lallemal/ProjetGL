/* A manual test for the initial sketch of code generation included in 
 * students skeleton.
 * 
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 *
 * @author Ensimag
 * @date 01/01/2021
 */
public class ManualTestInitialGencode {
    
    public static AbstractProgram initTest1() {
        ListInst linst = new ListInst();
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(new ListDeclVar(),linst));
        ListExpr lexp1 = new ListExpr(), lexp2 = new ListExpr();
        linst.add(new Print(false,lexp1));
        linst.add(new Println(false,lexp2));
        lexp1.add(new StringLiteral("Hello "));
        lexp2.add(new StringLiteral("everybody !"));
        return source;
    }
    
    public static AbstractProgram initTest2() {
        ListInst linst = new ListInst();
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(new ListDeclVar(),linst));
        ListExpr lexp1 = new ListExpr(), lexp2 = new ListExpr(), lexp3 = new ListExpr();
        linst.add(new Print(false,lexp1));
        linst.add(new Print(false,lexp2));
        linst.add(new Print(true,lexp2));
        linst.add(new Println(false,lexp3));
        lexp1.add(new IntLiteral(10));
        lexp2.add(new FloatLiteral((float) 10.10));
        lexp3.add(new StringLiteral("Hello World test 2"));
        return source;
    }
    
    public static AbstractProgram initTest3() {
        ListInst linst = new ListInst();
        ListDeclVar lvar= new ListDeclVar();
        SymbolTable symbols = new SymbolTable();
        Symbol integer = symbols.create("int");
        Symbol x = symbols.create("x");
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(lvar,linst));
        lvar.add(new DeclVar(new Identifier(integer), new Identifier(x), new NoInitialization()));
        return source;
    }
    
    public static String gencodeSource(AbstractProgram source) {
        DecacCompiler compiler = new DecacCompiler(null,null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() {
        AbstractProgram source = initTest1();
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");        
        String result = gencodeSource(source);
        System.out.println(result);
        assert(result.equals(
                "; Main program\n" +
                "; Beginning of main function:\n" +
                "	WSTR \"Hello \"\n" +
                "	WSTR \"everybody !\"\n" +
                "	WNL\n" +
                "	HALT\n"));
    }    

    public static void test2() {
        AbstractProgram source = initTest2();
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");        
        String result = gencodeSource(source);
        System.out.println(result);
        assert(result.equals(
                "; Main program\n" +
                "; Beginning of main function:\n" +
                "	LOAD #10, R1\n" +
                "	WINT\n" +
                "	LOAD #0x1.433334p3, R1" +
                "	WFLOAT\n" +
                "	LOAD #0x1.433334p3, R1" +
                "	WFLOATX\n" +
                "	WSTR \"Hello World test 2\"\n" +
                "	WNL\n" +
                "	HALT\n"));
    }
    
    public static void test3() {
        AbstractProgram source = initTest3();
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");        
        String result = gencodeSource(source);
        System.out.println(result);
        assert(result.equals(
                "; Main program\n" +
                "; Beginning of main function:\n" +
                "	LOAD #0, R2\n" +
                "	STORE R2 1(GB)" +
                "	HALT\n"));
    }
        
    public static void main(String args[]) {
    	System.out.println("test3 :");
        test3();
    }
}
