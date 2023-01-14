package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
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
    protected int getP(){return 1;}

//    @Override
//    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//        Label falseLeft = new Label("lab1");
//        Label falseRight = new Label("lab2");
//        Label end = new Label("end");
//        GPRegister reg =  compiler.getRegisterDescriptor().getFreeReg();
//        int p = this.getP(); //return 0 for AND; 1 for OR
//        getLeftOperand().codeGenOr(compiler, falseLeft);
//        compiler.addInstruction(new LOAD(1, reg));
//        compiler.addInstruction(new STORE(reg, adr));
//        compiler.addInstruction(new BRA(end));
//        compiler.addLabel(falseLeft);
//        //LOAD 1 TO ADR AND BRA TO END
//        //LABEL FALSE AND.1
//        getRightOperand().codeGenOr(compiler, falseRight);
//        compiler.addInstruction(new LOAD(1, reg));
//        compiler.addInstruction(new STORE(reg, adr));
//        compiler.addInstruction(new BRA(end));
//        compiler.addLabel(falseRight);
//        //LOAD 1 TO ADR AND BRA TO END
//        // LABEL FALSE AND.2
//        compiler.addInstruction(new LOAD(0,reg));
//        compiler.addInstruction(new STORE(reg, adr));
//        compiler.addInstruction(new BRA(end));
//        compiler.addLabel(end);
//    }

}
