package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label, GPRegister register){
        if (!b){
            Label endOr = new Label("endOr.l" + getLocation().getLine() +
                    ".c" + getLocation().getPositionInLine());
            getLeftOperand().codeGenBranch(compiler, !b, endOr, register);
            getRightOperand().codeGenBranch(compiler, b, label, register);
            compiler.addLabel(endOr);
        }
        else {
            getLeftOperand().codeGenBranch(compiler, b, label, register);
            getRightOperand().codeGenBranch(compiler, b, label, register);
        }
    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //boolean a = true && true;
        Label trueOr = new Label("trueOr"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endOr = new Label("endOr.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        if (compiler.getRegisterDescriptor().useLoad()){
            GPRegister register = compiler.getRegisterDescriptor().getFreeReg();
            codeGenBranch(compiler, true, trueOr, register);
            //return 0 if false
            compiler.addInstruction(new LOAD(0, register));
            compiler.addInstruction(new BRA(endOr));
            compiler.addLabel(trueOr);
            //return 1 if true
            compiler.addInstruction(new LOAD(1, register));
            compiler.addInstruction(new BRA(endOr));
            compiler.addLabel(endOr);
            compiler.addInstruction(new STORE(register, adr));
        }
        else{
            //TODO: Push Pop
        }
    }

}
