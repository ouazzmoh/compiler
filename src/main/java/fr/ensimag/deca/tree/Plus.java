package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;


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
    protected DVal codeGenLoad(DecacCompiler compiler, DVal opLeft, DVal opRight){
        compiler.addInstruction(new ADD(opLeft, (GPRegister) opRight));
        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
        return opRight;
    }

//    @Override
//    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//
//        compiler.addInstruction(new ADD(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        //todo: try catch for type register
//        compiler.addInstruction(new STORE((GPRegister) opRight, adr));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister)opRight);
//    }
//
//    @Override
//    protected DVal codeGenLoad(DecacCompiler compiler){
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new ADD(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        return (GPRegister) opRight;
//    }



//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
//        DVal value = getLeftOperand().codeGenMul(compiler);
//        DVal register = getRightOperand().codeGenMul(compiler);
//        compiler.addInstruction(new ADD(value, (GPRegister) register));
//        compiler.addInstruction(new STORE((GPRegister)register, identifier.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister)register);
//
//    }

//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
//
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//
//        compiler.addInstruction(new ADD(opLeft, (GPRegister) opRight));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//        //todo: try catch for type register
//        compiler.addInstruction(new STORE((GPRegister) opRight, identifier.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister)opRight);
//    }




//    @Override
//    protected DVal codeGenSum(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenSum(compiler);
//        DVal register = getRightOperand().codeGenSum(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new ADD(value, (GPRegister) register));
//        return register;
//    }

//    @Override
//    protected DVal codeGenSub(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenSub(compiler);
//        DVal register = getRightOperand().codeGenSub(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new ADD(value, (GPRegister) register));
//        return register;
//    }
//    //TODO: Prune some functions here if never used
//    @Override
//    protected DVal codeGenMul(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenMul(compiler);
//        DVal register = getRightOperand().codeGenMul(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new ADD(value, (GPRegister) register));
//        return register;
//    }
//
//    @Override
//    protected DVal codeGenDiv(DecacCompiler compiler){
//        DVal value = getLeftOperand().codeGenDiv(compiler);
//        DVal register = getRightOperand().codeGenDiv(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new ADD(value, (GPRegister) register));
//        return register;
//    }
}
