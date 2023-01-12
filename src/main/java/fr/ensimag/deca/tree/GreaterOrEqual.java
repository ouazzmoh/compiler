package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Operator "x >= y"
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        DVal opLeft = getLeftOperand().codeGenLoad(compiler);
        //TODO: Add a try catch for GPRegister
        GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler); //load right, r1
        compiler.addInstruction(new CMP(opLeft, opRight));// load left, r2
        Label falseComp = new Label("falseComp");
        Label end = new Label("end");
        compiler.addInstruction(new BLT(falseComp)); // bne neq

        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(1, registerToUse)); // load 1, r
        compiler.addInstruction(new STORE(registerToUse, adr)); // store r, adr
        //No need to update registerDescriptor because we load and store
        compiler.addInstruction(new BRA(end)); // bra end
        compiler.addLabel(falseComp); // neq :
        compiler.addInstruction(new LOAD(0, registerToUse)); // load 0, r
        compiler.addInstruction(new STORE(registerToUse, adr)); // store r, adr
        compiler.addLabel(end); // end:
    }

}
