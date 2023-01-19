package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;

    private int stackSize;

    @Override
    public int getStackSize() {
        return stackSize;
    }

    @Override
    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public Main(ListDeclVar declVariables,
                ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
        this.stackSize = 0;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        //throw new UnsupportedOperationException("not yet implemented");
        EnvironmentExp localEnv = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, localEnv, null);
        Type returnType = new VoidType(null);
        insts.verifyListInst(compiler, localEnv, null, returnType);
        LOG.debug("verify Main: end");

    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        compiler.addComment("Beginning of main instructions:");
        compiler.addComment("Generating code for variable declaration");
        //TODO: GENERATE CODE FOR VARIABLE DECLARATION
        if (DecacCompiler.getIsArm() == true) {
        	declVariables.codeGenListDeclVariableArm(compiler);
            compiler.addComment("Generating code for instructions");
            insts.codeGenListInstArm(compiler);
        }
        else {
	        declVariables.codeGenListDeclVariable(compiler);
	        compiler.addComment("Generating code for instructions");
	        insts.codeGenListInst(compiler);
        }
        stackSize += declVariables.getList().size();
        if (stackSize != 0){
            compiler.addInstructionFirst(new BOV(new Label("err_stack_overflow")));
            compiler.addInstructionFirst(new TSTO(stackSize));
        }

    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
