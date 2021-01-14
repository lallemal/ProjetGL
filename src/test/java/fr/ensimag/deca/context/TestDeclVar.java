package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDeclVar {

    final Type VOID = new VoidType(null);

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
    }

    /**
     * Mocking class extendings DeclVar to use verifydeclVar which was protected.
     */
    class DeclVarMock extends DeclVar {
        public DeclVarMock(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
            super(type, varName, initialization);
        }

        @Override
        public void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
            super.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }


    @Test
    public void voidDecl() throws  ContextualError {
        Identifier voidIdent = mock(Identifier.class);
        Identifier varName = new Identifier(compiler.getSymbols().create("x"));
        when(voidIdent.verifyType(compiler)).thenReturn(VOID);
        DeclVarMock t = new DeclVarMock(voidIdent, varName, new NoInitialization());
        assertThrows(ContextualError.class, ()->{t.verifyDeclVar(compiler, null, null);});

    }

}
