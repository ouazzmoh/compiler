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
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.BOOLEAN);
    	return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
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
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    /**
     * Generate instruction for boolean initialization
     * @param compiler
     * @param adr
     */
    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //true is #1 false is #0
        int valueToAdd;
        if (value){
            valueToAdd = 1;
        }
        else{
            valueToAdd = 0;
        }
        GPRegister reg = compiler.getFreeReg();
        //Implicit use and free for register

        compiler.addInstruction(new LOAD(valueToAdd, reg));
        compiler.addInstruction(new STORE(reg, adr));
    }


    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        GPRegister reg = compiler.getFreeReg();
        //Implicit use and free for register
        if (value){
            compiler.addInstruction(new LOAD(1, reg));
        }
        else {
            compiler.addInstruction(new LOAD(0, reg));
        }

        compiler.addInstruction(new CMP(0, reg));
        if (b){
            compiler.addInstruction(new BNE(label));
        }
        else {
            compiler.addInstruction(new BEQ(label));
        }
    }
    
    
    

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getFreeReg();
        int toLoad = 0;
        if(value){
            toLoad = 1;
        }
        compiler.addInstruction(new LOAD(toLoad, registerToUse));
        compiler.useReg();
        compiler.updateMaxRegisterUsed();
        return registerToUse;
    }

}


