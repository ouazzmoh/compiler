package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){

        DVal value = getLeftOperand().codeGenSum(compiler);
        DVal register = getRightOperand().codeGenSum(compiler);

        compiler.addInstruction(new ADD(value, (GPRegister) register));
        //TODO: The register cast is ugly
        compiler.addInstruction(new STORE((GPRegister)register, adr));
    }


    @Override
    protected DVal codeGenSum(DecacCompiler compiler){
        DVal value = getLeftOperand().codeGenSum(compiler);
        DVal register = getRightOperand().codeGenSum(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new ADD(value, (GPRegister) register));
        return register;
    }

    @Override
    protected DVal codeGenSub(DecacCompiler compiler){
        DVal value = getLeftOperand().codeGenSub(compiler);
        DVal register = getRightOperand().codeGenSub(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new ADD(value, (GPRegister) register));
        return register;
    }
    //TODO: Prune some functions here if never used
    @Override
    protected DVal codeGenMul(DecacCompiler compiler){
        DVal value = getLeftOperand().codeGenMul(compiler);
        DVal register = getRightOperand().codeGenMul(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new ADD(value, (GPRegister) register));
        return register;
    }

    @Override
    protected DVal codeGenDiv(DecacCompiler compiler){
        DVal value = getLeftOperand().codeGenDiv(compiler);
        DVal register = getRightOperand().codeGenDiv(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new ADD(value, (GPRegister) register));
        return register;
    }

    @Override
    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
        DVal value = getLeftOperand().codeGenMul(compiler);
        DVal register = getRightOperand().codeGenMul(compiler);
        compiler.addInstruction(new ADD(value, (GPRegister) register));
        compiler.addInstruction(new STORE((GPRegister)register, identifier.getExpDefinition().getOperand()));
        compiler.getRegisterDescriptor().freeRegister((GPRegister)register);

    }
}
