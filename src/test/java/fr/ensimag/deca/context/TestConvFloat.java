package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TestConvFloat {
    final Type FLOAT = new FloatType(null);
    final Type INT = new IntType(null);
    final Type STRING = new StringType(null);
    final Type BOOL = new BooleanType(null);

    DecacCompiler compiler;

    @Mock
    AbstractExpr stringValue;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(stringValue.verifyExpr(compiler, null, null)).thenReturn(STRING);

    }


    @Test
    public void throwNotInt() {
        ConvFloat t = new ConvFloat(stringValue);
        assertThrows(ContextualError.class, ()->{t.verifyExpr(compiler, null, null);});
    }

}
