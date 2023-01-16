package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.awt.peer.ComponentPeer;
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
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label, GPRegister register){
        if (!b){
            getLeftOperand().codeGenBranch(compiler, b, label, register);
            getRightOperand().codeGenBranch(compiler, b, label, register);
        }
        else {
            Label falseAnd = new Label("falseAnd.l" + getLocation().getLine() +
                    ".c" + getLocation().getPositionInLine());
            getLeftOperand().codeGenBranch(compiler, !b, falseAnd, register);
            getRightOperand().codeGenBranch(compiler, b, label, register);
            compiler.addLabel(falseAnd);
        }
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //boolean a = true && true;
        Label falseAnd = new Label("falseAnd"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endAnd = new Label("endAnd.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        if (compiler.getRegisterDescriptor().useLoad()){
            GPRegister register = compiler.getRegisterDescriptor().getFreeReg();
            codeGenBranch(compiler, false, falseAnd, register);
            //return 1 if true
            compiler.addInstruction(new LOAD(1, register));
            compiler.addInstruction(new BRA(endAnd));
            compiler.addLabel(falseAnd);
            //return 0 if false
            compiler.addInstruction(new LOAD(0, register));
            compiler.addInstruction(new BRA(endAnd));
            compiler.addLabel(endAnd);
            compiler.addInstruction(new STORE(register, adr));
        }
        else{
            //TODO: Push Pop
        }
    }
}
