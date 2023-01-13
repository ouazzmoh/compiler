package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacMain;
import fr.ensimag.deca.codegen.RegisterDescriptor;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.ADD;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl24
 * @date 01/01/2023
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.INT);
    	return this.getType();
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    /**
     * Generate initialization code for a integer variable
     * @param compiler
     * @param adr
     */
    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){

        // LOAD #value, R2
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(value, registerToUse));
        //update register descriptor
        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));

        compiler.addInstruction(new STORE(registerToUse, adr));
        compiler.getRegisterDescriptor().freeRegister(registerToUse);

    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(value, registerToUse));
        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
        return registerToUse;
    }

//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifer){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(value, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
//        compiler.addInstruction(new STORE(registerToUse, identifer.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister(registerToUse);
//    }

//    @Override
//    protected DVal codeGenSum(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(value, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
//        return registerToUse;
//    }

//    @Override
//    protected DVal codeGenSub(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(value, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
//        return registerToUse;
//    }
//
//    @Override
//    protected DVal codeGenMul(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(value, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
//        return registerToUse;
//    }
//
//
//    @Override
//    protected DVal codeGenDiv(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(value, registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateInteger(value));
//        return registerToUse;
//    }






}
