package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }


//    @Override
//    protected DVal codeGenLoad(DecacCompiler compiler, DVal opLeft, DVal opRight){
//        compiler.addInstruction(new MUL(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        return opRight;
//    }

    //    @Override
//    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//
//        compiler.addInstruction(new MUL(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        //TODO: The register cast is ugly
//        compiler.addInstruction(new STORE((GPRegister) opRight, adr));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opRight);
//    }
//
//
//    @Override
//    protected DVal codeGenLoad(DecacCompiler compiler){
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new MUL(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        return (GPRegister) opRight;
//    }

//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
//        DVal value = getLeftOperand().codeGenMul(compiler);
//        DVal register = getRightOperand().codeGenMul(compiler);
//        compiler.addInstruction(new MUL(value, (GPRegister) register));
//        compiler.addInstruction(new STORE((GPRegister)register, identifier.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister)register);
//
//    }


//    @Override
//    protected DVal codeGenSum(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenSum(compiler);
//        DVal register = getRightOperand().codeGenSum(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new MUL(value, (GPRegister) register));
//        return register;
//    }
//
//    @Override
//    protected DVal codeGenSub(DecacCompiler compiler){
//        DVal value = getRightOperand().codeGenSub(compiler);
//        DVal register = getLeftOperand().codeGenSub(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new MUL(value, (GPRegister) register));
//        return register;
//    }
//    @Override
//    protected DVal codeGenMul(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenMul(compiler);
//        DVal register = getRightOperand().codeGenMul(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new MUL(value, (GPRegister) register));
//        return register;
//    }
//
//    @Override
//    protected DVal codeGenDiv(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenDiv(compiler);
//        DVal register = getRightOperand().codeGenDiv(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new MUL(value, (GPRegister) register));
//        return register;
//    }

}
