package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacMain;
import fr.ensimag.deca.context.Type;
import fr.ensimag.arm.pseudocode.ArmProgram;
import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.arm.pseudocode.DValArm;
import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.ImmediateIntegerArm;
import fr.ensimag.arm.pseudocode.ImmediateStringArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.RegisterArm;
import fr.ensimag.arm.pseudocode.instructions.LDR;
import fr.ensimag.arm.pseudocode.instructions.MOV;
import fr.ensimag.arm.pseudocode.instructions.STR;
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
 * Integer literal
 *
 * @author gl24
 * @date 01/01/2023
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }
    
    public String getValueString() {
    	return ""+this.value;
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
	        compiler.addInstruction(new LOAD(value, Register.R1));
	        compiler.addInstruction(new STORE(Register.R1, adr));
    }
    
    /**
     * Generate initialization code for a integer variable for ARM by
     * adding it into our data
     * @param compiler
     * @param adr
     */
    @Override
    protected void codeGenInitArm(DecacCompiler compiler, DAddrArm adr){
    	DecacCompiler.data.put(new LabelArm("Variable" + this.getLocation().getLine()), new ImmediateIntegerArm(this.value));
    }
    
    
    @Override
    protected DVal codeGenLoad(DecacCompiler compiler) {
        GPRegister registerToUse = compiler.getFreeReg();
        compiler.addInstruction(new LOAD(value, registerToUse));
        compiler.useReg();
        return registerToUse;
    }








    @Override
    protected void codeGenPush(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(value, Register.R1));
        compiler.addInstruction(new PUSH(Register.R1));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
	        compiler.addInstruction(new LOAD(value, Register.R1));
	        compiler.addInstruction(new WINT());
    }
    
    @Override
    protected void codeGenPrintArm(DecacCompiler compiler, boolean hex){
    	LabelArm lab2 = new LabelArm( " Variable " + this.getLocation().getLine() + " (" + this.getLocation().getPositionInLine() + ")" + "toprint");
    	DecacCompiler.data.put(lab2, new ImmediateStringArm(getValueString()));
    	compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
        compiler.addInstruction(new MOV(RegisterArm.getR(1), new ImmediateIntegerArm(1)));
        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab2));
        int l = DecacCompiler.data.get(lab2).toString().length();
        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(l-2) ));
        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
        }

	@Override
	protected void codeGenInstArm(DecacCompiler compiler, Label endIf) {
		// TODO Auto-generated method stub
		
	}
 
    	
}
