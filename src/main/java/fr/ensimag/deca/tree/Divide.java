package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


    @Override
    protected DVal codeGenLoad(DecacCompiler compiler, DVal opLeft, DVal opRight){

        Type typeLeft = this.getLeftOperand().getType();
        Type typeRight = this.getRightOperand().getType();

        if (typeLeft.isFloat() || typeRight.isFloat()){
            compiler.addInstruction(new DIV(opRight, (GPRegister) opLeft));
        } else if (typeLeft.isInt() && typeRight.isInt()) {
            compiler.addInstruction(new QUO(opRight, (GPRegister) opLeft));
        } else {
            throw new DecacInternalError("Operandes pour la division non valide");
        }
        compiler.getRegisterDescriptor().freeRegister((GPRegister) opRight);
        return opLeft;
    }

//    @Override
//    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//
//        compiler.addInstruction(new QUO(opRight, (GPRegister) opLeft));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opRight);
//        //TODO: The register cast is ugly
//        compiler.addInstruction(new STORE((GPRegister)opLeft, adr));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
//    }
//
//    @Override
//    protected DVal codeGenLoad(DecacCompiler compiler){
//        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
//        DVal opRight = getRightOperand().codeGenLoad(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new QUO(opRight, (GPRegister) opLeft));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister) opRight);
//        return (GPRegister) opLeft;
//    }
//
//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
//        this.codeGenInit(compiler, identifier.getExpDefinition().getOperand());
//    }

//    @Override
//    protected DVal codeGenDiv(DecacCompiler compiler){
//        DVal value = getRightOperand().codeGenDiv(compiler);
//        DVal register = getLeftOperand().codeGenDiv(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new QUO(value, (GPRegister) register));
//        return register;
//    }
//
//    //TODO: Prune some functions here
//    @Override
//    protected DVal codeGenMul(DecacCompiler compiler){
//        DVal value = getRightOperand().codeGenMul(compiler);
//        DVal register = getLeftOperand().codeGenMul(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new QUO(value, (GPRegister) register));
//        return register;
//    }
//
//    @Override
//    protected DVal codeGenSum(DecacCompiler compiler){
//        DVal value = getRightOperand().codeGenSum(compiler);
//        DVal register = getLeftOperand().codeGenSum(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new QUO(value, (GPRegister) register));
//        return register;
//    }
//
//    @Override
//    protected DVal codeGenSub(DecacCompiler compiler){
//        DVal value = getRightOperand().codeGenSub(compiler);
//        DVal register = getLeftOperand().codeGenSub(compiler);
//        //TODO: Remove Ugly Cast
//        compiler.addInstruction(new QUO(value, (GPRegister) register));
//        return register;
//    }

//    @Override
//    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
//        DVal opLeft = getRightOperand().codeGenLoad(compiler);
//        DVal o = getLeftOperand().codeGenLoad(compiler);
//        compiler.addInstruction(new QUO(value, (GPRegister) register));
//        compiler.addInstruction(new STORE((GPRegister)register, identifier.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister((GPRegister)register);
//
//    }

}
