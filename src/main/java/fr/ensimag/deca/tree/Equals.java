package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    protected void codeGenMnem(DecacCompiler compiler, Label label){
        compiler.addInstruction(new BEQ(label));
    }

    protected void codeGenMnemOpp(DecacCompiler compiler, Label label){
        compiler.addInstruction(new BNE(label));
    }
    
}
