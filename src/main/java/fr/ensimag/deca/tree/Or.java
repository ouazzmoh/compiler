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
    protected int getP(){return 1;}

    protected DVal codeGenLoad(DecacCompiler compiler, Label label){
        GPRegister register = (GPRegister) compiler.getRegisterDescriptor().getFreeReg(); // reg where value is returned
        Label checkOrLeft = new Label("or.left.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        Label checkOrRight = new Label("or.right.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        Label trueOr = new Label("true.or.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        Label falseOr = new Label("false.or.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());

        compiler.addLabel(checkOrLeft);

        getLeftOperand().codeGenBeq(compiler, trueOr, checkOrRight);

        compiler.addLabel(checkOrRight);

        getRightOperand().codeGenBeq(compiler, trueOr, falseOr);

        compiler.addLabel(trueOr);
        compiler.addInstruction(new LOAD(1, register));
        compiler.addInstruction(new BRA(label));
        compiler.addLabel(falseOr);
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
        Label endOr = new Label("end.or.l" + getLocation().getLine() + ".c" +
                getLocation().getPositionInLine());
        return codeGenLoad(compiler, endOr);
    }

}
