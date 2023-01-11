package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){

        DVal value = getRightOperand().codeGenSub(compiler);
        DVal register = getLeftOperand().codeGenSub(compiler);

        compiler.addInstruction(new SUB(value, (GPRegister) register));
        //TODO: The register cast is ugly
        compiler.addInstruction(new STORE((GPRegister)register, adr));
    }


    @Override
    protected DVal codeGenSub(DecacCompiler compiler){
        DVal value = getRightOperand().codeGenSub(compiler);
        DVal register = getLeftOperand().codeGenSub(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new SUB(value, (GPRegister) register));
        return register;
    }

    @Override
    protected DVal codeGenSum(DecacCompiler compiler){
        DVal value = getRightOperand().codeGenSum(compiler);
        DVal register = getLeftOperand().codeGenSum(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new SUB(value, (GPRegister) register));
        return register;
    }

    @Override
    protected DVal codeGenMul(DecacCompiler compiler){
        DVal value = getRightOperand().codeGenMul(compiler);
        DVal register = getLeftOperand().codeGenMul(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new SUB(value, (GPRegister) register));
        return register;
    }

    @Override
    protected DVal codeGenDiv(DecacCompiler compiler){
        DVal value = getRightOperand().codeGenDiv(compiler);
        DVal register = getLeftOperand().codeGenDiv(compiler);
        //TODO: Remove Ugly Cast
        compiler.addInstruction(new SUB(value, (GPRegister) register));
        return register;
    }

}
