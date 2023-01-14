package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.logging.Logger;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected int getP(){return 0;}


//    @Override
    protected DVal codeGenLoad(DecacCompiler compiler, Label label){
        GPRegister register = (GPRegister) compiler.getRegisterDescriptor().getFreeReg(); // reg where value is returned
        Label checkAnd = new Label("check.and.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        Label falseAnd = new Label("false.and.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());

        compiler.addLabel(checkAnd);

        getLeftOperand().codeGenBeq(compiler, falseAnd, 0);
        getRightOperand().codeGenBeq(compiler, falseAnd, 0);

        compiler.addInstruction(new LOAD(1, register));
        compiler.addInstruction(new BRA(label));

        compiler.addLabel(falseAnd);

        compiler.addInstruction(new LOAD(0, register));
        compiler.addInstruction(new BRA(label));

        compiler.getRegisterDescriptor().useRegister(register, new ImmediateInteger(1)); //TODO remove value from reg

        compiler.addLabel(label);
        return register;
    }
//TODO: Remove the value of the register from the reg descriptor

//TODO: Empty main init
    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        Label endAnd = new Label("end.and.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        return codeGenLoad(compiler, endAnd);
    }

    @Override
    protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
        Label falseAnd = new Label("false.and.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());

        getLeftOperand().codeGenBeq(compiler, falseAnd, p);


        getRightOperand().codeGenBeq(compiler, falseAnd, p);
        compiler.addInstruction(new BRA(label));
    }

    @Override
    protected void codeGenBeq(DecacCompiler compiler, Label trueOr, Label checkRight){
        Label falseAnd = new Label("false.and.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
//        Label checkAndLeft = new Label("and.left.l" + getLocation().getLine() + ".c" +
//                getLocation().getPositionInLine());
//        Label checkAndRight = new Label("and.right.l" + getLocation().getLine() + ".c" +
//                getLocation().getPositionInLine());
//
//        compiler.addLabel(checkAndLeft);
//        getLeftOperand().codeGenBeq(compiler, falseAnd, 0);
//        compiler.addLabel(checkAndRight);
//        getRightOperand().codeGenBeq(compiler, falseAnd, 0);

        codeGenBeq(compiler, trueOr, checkRight, falseAnd);

        compiler.addInstruction(new BRA(trueOr));

        compiler.addLabel(falseAnd);

        compiler.addInstruction(new BRA(checkRight));




    }

    protected void codeGenBeq(DecacCompiler compiler, Label trueOr, Label checkRight, Label falseAnd){
        Label checkAndLeft = new Label("and.left.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        Label checkAndRight = new Label("and.right.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        compiler.addLabel(checkAndLeft);
        getLeftOperand().codeGenBeq(compiler, falseAnd, 0);
        compiler.addLabel(checkAndRight);
        getRightOperand().codeGenBeq(compiler, falseAnd, 0);





    }


}
