package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.DeclClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestDeclClass {


    @Mock
    AbstractIdentifier ident;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(ident.getName()).thenReturn(compiler.getSymbols().create("A"));
    }


    @Test
    public void TestDeclNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            DeclClass newClass = new DeclClass(null, null, null, null);
        });
    }

    @Test
    public void TestObjectParent() throws ContextualError {
        DeclClass newClass = new DeclClass(ident, null, null, null);
        newClass.verifyClass(compiler);
        assertTrue(compiler.getEnv_types().get(ident.getName()).isClass());
        ClassDefinition classDef = (ClassDefinition)compiler.getEnv_types().get(ident.getName());
        ClassDefinition parentDef = classDef.getSuperClass();
        assertTrue(parentDef.getType().getName().toString().equals("Object"));
    }

    @Test
    public void TestClassNature() throws  ContextualError {
        DeclClass newClass = new DeclClass(ident, null, null, null);
        newClass.verifyClass(compiler);
        assertTrue(compiler.getEnv_types().get(ident.getName()).getNature().equals("class"));
    }
}
