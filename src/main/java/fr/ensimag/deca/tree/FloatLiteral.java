package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl24
 * @date 01/01/2023
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented"); 
    	this.setType(compiler.environmentType.FLOAT);
    	return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
     * Generate initialization code for a float variable
     * @param compiler
     * @param adr
     */
    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){

        // LOAD #value, R2
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        DVal valueToAdd = new ImmediateFloat(value);
        compiler.addInstruction(new LOAD(valueToAdd, registerToUse));
        //update register descriptor
        compiler.getRegisterDescriptor().useRegister(registerToUse, new ImmediateFloat(value));

        compiler.addInstruction(new STORE(registerToUse, adr));
        compiler.getRegisterDescriptor().freeRegister(registerToUse);

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new ImmediateFloat(value), Register.R1));
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        ImmediateFloat toLoad = new ImmediateFloat(value);
        compiler.addInstruction(new LOAD(toLoad, registerToUse));
        compiler.getRegisterDescriptor().useRegister(registerToUse, toLoad);
        return registerToUse;
    }

}
