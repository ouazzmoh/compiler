package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        Label falseLab = new Label("falseRes");
        compiler.addLabel(new Label("line" + getLeftOperand().getLocation().getLine() +
                "col"+getLeftOperand().getLocation().getPositionInLine()));
        getLeftOperand().codeGenAnd(compiler, falseLab);
        compiler.addLabel(new Label("line" + getRightOperand().getLocation().getLine() +
                "col"+getRightOperand().getLocation().getPositionInLine()));
        getRightOperand().codeGenAnd(compiler, falseLab);

    }

    @Override
    protected void codeGenAnd(DecacCompiler compiler, Label label){
        compiler.addLabel(new Label("line" + getLeftOperand().getLocation().getLine() +
                "col"+getLeftOperand().getLocation().getPositionInLine()));
        getLeftOperand().codeGenAnd(compiler, label);
        compiler.addLabel(new Label("line" + getRightOperand().getLocation().getLine() +
                "col"+getRightOperand().getLocation().getPositionInLine()));
        getRightOperand().codeGenAnd(compiler, label);
    }




}
