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
        // LOAD #value, R2
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        //true is #1 false is #0
        int valueToAdd;
        if (value){
            valueToAdd = 1;
        }
        else{
            valueToAdd = 0;
        }
        compiler.addInstruction(new LOAD(valueToAdd, registerToUse));
        //update register descriptor
        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(valueToAdd));

        compiler.addInstruction(new STORE(registerToUse, adr));
        compiler.getRegisterDescriptor().freeRegister(registerToUse);
    }

//    @Override
//    protected void codeGenInstWhile(DecacCompiler compiler,Label endWhile){
//        if(!this.value){
//            compiler.addInstruction(new BRA(endWhile));
//        }
//    }

//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifer){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        int valueToAdd;
//        if (value){
//            valueToAdd = 1;
//        }else{
//            valueToAdd = 0;
//        }
//        compiler.addInstruction(new LOAD(valueToAdd, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(valueToAdd));
//        compiler.addInstruction(new STORE(registerToUse, identifer.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister(registerToUse);
//    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        int toLoad = 0;
        if(value){
            toLoad = 1;
        }
        compiler.addInstruction(new LOAD(toLoad, registerToUse));
        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(toLoad));
        return registerToUse;
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        int toLoad = 0;
        if(value){
            toLoad = 1;
        }
        compiler.addInstruction(new LOAD(toLoad, registerToUse)); //No need to use and free
        compiler.addInstruction(new PUSH(registerToUse));
    }

    @Override
    protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
        GPRegister registerToUse = (GPRegister) codeGenLoad(compiler);
//        if (value){
//            compiler.addInstruction(new LOAD(1, registerToUse));
//        }
//        else{
//            compiler.addInstruction(new LOAD(0, registerToUse));
//        }

        compiler.addInstruction(new CMP(p, registerToUse));
        compiler.addInstruction(new BEQ(label));
    }

//    @Override
//    protected void codeGenIf(DecacCompiler compiler, Label label){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        if (value){
//            compiler.addInstruction(new LOAD(1, registerToUse));
//        }
//        else{
//            compiler.addInstruction(new LOAD(0, registerToUse));
//        }
//
//        compiler.addInstruction(new CMP(0, registerToUse));
//        compiler.addInstruction(new BEQ(label));
//    }

//    @Override
//    protected void codeGenOr(DecacCompiler compiler, Label label){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        if (value){
//            compiler.addInstruction(new LOAD(1, registerToUse));
//        }
//        else{
//            compiler.addInstruction(new LOAD(0, registerToUse));
//        }
//
//        compiler.addInstruction(new CMP(0, registerToUse));
//        compiler.addInstruction(new BEQ(label));
//    }




}


