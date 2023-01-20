package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.arm.pseudocode.ImmediateIntegerArm;
import fr.ensimag.arm.pseudocode.ImmediateStringArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.RegisterArm;
import fr.ensimag.arm.pseudocode.instructions.LDR;
import fr.ensimag.arm.pseudocode.instructions.MOV;
import fr.ensimag.arm.pseudocode.instructions.SWI;
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
        compiler.addInstruction(new LOAD(valueToAdd, Register.R1));
        compiler.addInstruction(new STORE(Register.R1, adr));
    }


    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        if (value){
            compiler.addInstruction(new LOAD(1, Register.R1));
        }
        else {
            compiler.addInstruction(new LOAD(0, Register.R1));
        }

        compiler.addInstruction(new CMP(0, Register.R1));
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
        return registerToUse;
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        int toLoad = 0;
        if(value){
            toLoad = 1;
        }
        compiler.addInstruction(new LOAD(toLoad, Register.R1));
        compiler.addInstruction(new PUSH(Register.R1));
    }

    

//	@Override
//	protected void codeGenInstArm(DecacCompiler compiler, Label endIf) {
//		// TODO Auto-generated method stub
//
//
//	}

}


