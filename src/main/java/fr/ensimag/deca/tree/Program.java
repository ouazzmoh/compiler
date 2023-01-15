package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.HALT;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        //throw new UnsupportedOperationException("not yet implemented");
        // LOG.debug("verify program: end");
        
        classes.verifyListClass(compiler);
        
        // passe2 on hérite env_typesr
        classes.verifyListClassBody(compiler);
        
        //passe3 herite l'attribut synthetisé de 2
        classes.verifyListClassMembers(compiler);
        main.verifyMain(compiler);
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Main program");
        compiler.getErrorsMap().put("err_stack_overflow", "Erreur: la pile est pleine");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.addComment("Generating code for errors");
        Iterator<Map.Entry<String, String>> it = compiler.getErrorsMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> couple = it.next();
            compiler.addLabel(new Label(couple.getKey()));
            compiler.addInstruction(new WSTR(couple.getValue()));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            it.remove();
        }


    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
