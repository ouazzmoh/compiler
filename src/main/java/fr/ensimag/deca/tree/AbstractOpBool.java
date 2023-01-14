package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t1.sameType(t2) && t1.isBoolean()) {
    		if (this.getOperatorName().equals("&&") | this.getOperatorName().equals("||")) {
        		this.setType(compiler.environmentType.BOOLEAN);
        		return this.getType();    			
    		}
    	}
    	throw new ContextualError("types not permetted for" + this.getOperatorName(), this.getLocation());
    }

//	@Override
//	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//		Label lab1 = new Label("lab1");
//		Label lab2 = new Label("lab2");
//		Label end = new Label("end");
//		int p = this.getP(); //return 0 for AND; 1 for OR
//		getLeftOperand().codeGenBeq(compiler, lab1, p);
//		//LOAD 1 TO ADR AND BRA TO END
//		//LABEL FALSE AND.1
//		getRightOperand().codeGenBeq(compiler, lab1, p);
//		//LOAD 1 TO ADR AND BRA TO END
//		// LABEL FALSE AND.2
//		compiler.addInstruction(new BRA(lab2));
//		compiler.addLabel(lab1);
//		GPRegister reg =  compiler.getRegisterDescriptor().getFreeReg();
//		compiler.addInstruction(new LOAD(p,reg));
//		compiler.addInstruction(new STORE(reg, adr));
//		compiler.addInstruction(new BRA(end));
//		compiler.addLabel(lab2);
//		compiler.addInstruction(new LOAD(1-p,reg));
//		compiler.addInstruction(new STORE(reg, adr));
//		compiler.addLabel(end);
//	}

	@Override
	protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
		getLeftOperand().codeGenBeq(compiler, label, p);
		getRightOperand().codeGenBeq(compiler, label, p);
	}

	protected abstract int getP();

}
