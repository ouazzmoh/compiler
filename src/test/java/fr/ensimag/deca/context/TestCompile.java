package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.CLIException;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.Plus;

public class TestCompile {
    DecacCompiler compiler;
    PrintStream out = System.out;
    CompilerOptions options = new CompilerOptions();

    
    
    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        String s = "src/test/deca/syntax/valid/afficher_Hello.deca";
        File currSource = new File(s);
        compiler = new DecacCompiler(options, currSource);
    }    
    
    @Test
    public void testoptionp() throws ContextualError, CLIException {
        // check the result
        //options.Settings("-p", 0);
        compiler.compile();
    }

}


