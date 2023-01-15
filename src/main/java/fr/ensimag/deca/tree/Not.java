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
        //throw new UnsupportedOperationException("not yet implemented");
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
        compiler.getRegisterDescriptor().useRegister(valueReg, valueReg);
        return valueReg;
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        getOperand().codeGenPush(compiler);
        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new CMP(0, Register.R0));
        compiler.addInstruction(new SEQ(Register.R0));
    }

    @Override
    protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
        GPRegister valueReg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new CMP(new ImmediateInteger(p), valueReg));
        compiler.addInstruction(new BEQ(label));
    }

}
