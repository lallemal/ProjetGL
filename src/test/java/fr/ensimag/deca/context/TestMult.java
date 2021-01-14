package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Multiply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestMult {
    final Type FLOAT = new FloatType(null);
    final Type INT = new IntType(null);
    final Type STRING = new StringType(null);
    final Type BOOL = new BooleanType(null);

    @Mock
    AbstractExpr intLeft;

    @Mock
    AbstractExpr intRight;

    @Mock
    AbstractExpr floatLeft;

    @Mock
    AbstractExpr floatRight;

    @Mock
    AbstractExpr string;

    @Mock
    AbstractExpr bool;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intLeft.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intRight.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatLeft.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatRight.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(string.verifyExpr(compiler, null, null)).thenReturn(STRING);
        when(bool.verifyExpr(compiler, null, null)).thenReturn(BOOL);
    }


    @Test
    public void testIntType() throws ContextualError {
        Multiply t = new Multiply(intLeft, intRight);
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
    }

    @Test
    public void testFloatType() throws  ContextualError {
        Multiply t = new Multiply(floatLeft, floatRight);
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());
    }

    @Test
    public void testIntFloatType() throws  ContextualError {
        Multiply t = new Multiply(intLeft, floatRight);
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());

        assertTrue(t.getLeftOperand() instanceof ConvFloat);
        assertFalse(t.getRightOperand() instanceof ConvFloat);
    }

    @Test
    public void testFloatIntType() throws  ContextualError {
        Multiply t = new Multiply(floatLeft, intRight);
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());

        assertFalse(t.getLeftOperand() instanceof ConvFloat);
        assertTrue(t.getRightOperand() instanceof ConvFloat);
    }

    @Test
    public void testStringThrow() throws ContextualError {
        Multiply t = new Multiply(intLeft, string);
        assertThrows(ContextualError.class, () -> {t.verifyExpr(compiler, null, null);});
    }

    @Test
    public void testBooleanThrow() throws ContextualError {
        Multiply t = new Multiply(intLeft, bool);
        assertThrows(ContextualError.class, () -> {t.verifyExpr(compiler, null, null);});
    }
}
