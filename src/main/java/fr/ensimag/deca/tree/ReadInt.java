package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.INT);
    	return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        compiler.addInstruction(new RINT());
        super.codeGenInit(compiler, adr);
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        compiler.addInstruction(new RINT());
        return super.codeGenLoad(compiler);
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        compiler.addInstruction(new RINT());
        super.codeGenPush(compiler);
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        compiler.addInstruction(new WINT());
    }

}
