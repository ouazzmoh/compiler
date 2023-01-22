package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

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
        classes.verifyListClassMembers(compiler);
        
        //passe3 herite l'attribut synthetisé de 2
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // todo :Faire les commandes TSTO ADDSP ET BOV
        if(!this.getClasses().isEmpty()){
            //The first class will necessarily have object as superclass
            //STORE null, 1(GB)
            //STORE code.Object.equals, 2(GB)
            Label objectEqLabel = new Label("code.Object.equals");
            LabelOperand operandObjectLabel = new LabelOperand(objectEqLabel);
            compiler.addComment("Vtable construction for Object");
            compiler.addInstruction(new LOAD(new NullOperand(),Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(),Register.GB)));
            compiler.incOffset(1);
            compiler.addInstruction(new LOAD(operandObjectLabel, Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
            compiler.incOffset(1);
            //Construct vTable for clases
            classes.codeGenListVirtualTable(compiler);
        }
        compiler.addComment("Main program");
        compiler.getErrorsMap().put("err_stack_overflow", "Erreur: la pile est pleine");
        main.codeGenMain(compiler);

        compiler.addInstruction(new HALT());

        compiler.addComment("Generating code for classes: Fields initializations and methods");
        if (!classes.isEmpty()){
            classes.codeGenListFieldsMethods(compiler);
            compiler.addLabel(new Label("Code.Object.equals"));
            //TODO: Remove this from here
            compiler.addInstruction(new RTS());
        }

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
