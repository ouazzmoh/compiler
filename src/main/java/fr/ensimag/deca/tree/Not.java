package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t.isBoolean()) {
    		this.setType(t);
    		return t;
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands's type not permetted", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister valueReg = (GPRegister) getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new CMP(0, valueReg));
        compiler.addInstruction(new SEQ(valueReg));
        return valueReg;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        getOperand().codeGenBranch(compiler, !b, label);
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //boolean a = true && true;
        Label falseNot = new Label("falseNot"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endNot = new Label("endNot.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        codeGenBranch(compiler, false, falseNot);
        GPRegister reg = compiler.getFreeReg();
        //Implicit use and free of register

        //return 1 if true
        compiler.addInstruction(new LOAD(1, reg));
        compiler.addInstruction(new BRA(endNot));
        compiler.addLabel(falseNot);
        //return 0 if false
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addInstruction(new BRA(endNot));
        compiler.addLabel(endNot);
        compiler.addInstruction(new STORE(reg, adr));
    }

}
