package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.deca.DecacMain;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

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
        GPRegister reg = compiler.getFreeReg();
        compiler.addInstruction(new LOAD(value, reg));
        compiler.addInstruction(new STORE(reg, adr));
        //Implicit use and free
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler) {
        GPRegister registerToUse = compiler.getFreeReg();
        compiler.addInstruction(new LOAD(value, registerToUse));
        compiler.useReg();
        return registerToUse;
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        compiler.addInstruction(new LOAD(value, Register.R1));
        compiler.addInstruction(new WINT());
    }

    @Override
    protected void codeGenReturn(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(value, Register.R0));
    }


    /**
     * Generate initialization code for a integer variable for ARM by
     * adding it into our data
     * @param compiler
     * @param adr
     */
    @Override
    protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){
        compiler.addOperandData(adr);
        compiler.addInstruction(new MOV(RegisterArm.R0, new ImmediateIntegerArm(value)));
        compiler.addInstruction(new LDR(RegisterArm.R1, (LabelArm) adr));
        compiler.addInstruction(new STR(RegisterArm.R0, new RegisterOffsetArm(0, RegisterArm.R1)));

    }

    @Override
    protected DValArm codeGenLoadArm(DecacCompiler compiler){
        GPRegisterArm reg = compiler.getFreeRegArm();
        compiler.addInstruction(new MOV(reg, new ImmediateIntegerArm(value)));
        compiler.useRegArm();
        return reg;
    }



}
