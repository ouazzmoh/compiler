package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	// il faut ajouter les autres operations de meme domaine
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=") | this.getOperatorName().equals("<=")
    			| this.getOperatorName().equals(">=") | this.getOperatorName().equals(">")
    			| this.getOperatorName().equals("<")) {
    			if (t1.sameType(t2) && (t1.isInt() | t1.isFloat())) {
    				this.setType(compiler.environmentType.BOOLEAN);
    				return this.getType();
    			}
    			if (t1.isInt() && t2.isFloat()) {
    				this.setType(compiler.environmentType.BOOLEAN);
					this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
    				return this.getType();
    				}
    			if (t1.isFloat() && t2.isInt()) {
    				this.setType(compiler.environmentType.BOOLEAN);
					this.setRightOperand(new ConvFloat(this.getRightOperand()));
    				return this.getType();    			}

    		}
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=")){
    		if (t1.sameType(t2) && (t1.isBoolean())) {
				this.setType(compiler.environmentType.BOOLEAN);
				return this.getType();    			
    		}
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands not permetted", this.getLocation());
    	}

	/**
	 * Initialize a boolean variable with the value of the operation
	 * @param compiler
	 * @param adr: memory adress of the variable
	 */
	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
		GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
		compiler.addInstruction(new CMP(opRight, opLeft));
		Label falseComp = new Label("falseComp");
		Label end = new Label("end");
		codeGenMnem(compiler, falseComp, true);

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

//	/**
//	 * Generates the mnemonic depending on the class
//	 * (BEQ, BLT, BGE, BGT, BLE, BNE)
//	 * @param compiler
//	 * @param label
//	 */
//	protected abstract void codeGenMnem(DecacCompiler compiler, Label label);


//	/**
//	 * Generates the opposing mnemonic of the class
//	 * e.g: for EQUALS generates BNE
//	 * @param compiler
//	 * @param label
//	 */
//	protected abstract void codeGenMnemOpp(DecacCompiler compiler, Label label);

//	/**
//	 * Generates the mnemonic to be used in the branching
//	 * For EQUALS, Not Equals : it generates the opposite (BNE, BEQ)
//	 * For inequality operations : it generates the corresponding to the class
//	 * @param compiler
//	 * @param label
//	 */
//	protected abstract void codeGenBranchMnem(DecacCompiler compiler, Label label);


	@Override
	protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
		codeGenBranchOpp(compiler, label);
	}


	/**
	 * Branch to the label if the comparison between the operands
	 * is not true-->useful for if statements
	 * @param compiler
	 * @param label
	 */
	protected void codeGenBranchOpp(DecacCompiler compiler, Label label){
		GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
		GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
		compiler.addInstruction(new CMP(opRight, opLeft));
		codeGenMnem(compiler, label, true);
	}

	/**
	 *Generates the mnemonic corresponding to the operation
	 * if opp is true, it generates the opposite mnemonic
	 * CMP R2, R3 (if R3 > R2, GT = true)
	 *
	 * It is advised to use CMP(rightOperand, leftOperand) to avoid confusion
	 * @param compiler
	 * @param label
	 * @param opp : if true we branch in the case the comparaison is false
	 *            if not we branch in the case the operation is false
	 */
	protected  void codeGenMnem(DecacCompiler compiler, Label label, boolean opp){
		if (getOperatorName().equals("<")){
			if (opp){
				compiler.addInstruction(new BGE(label));
			}else {
				compiler.addInstruction(new BLT(label));
			}
		}
		else if (getOperatorName().equals("<=")){
			if (opp){
				compiler.addInstruction(new BGT(label));
			}else {
				compiler.addInstruction(new BLE(label));
			}
		}
		else if (getOperatorName().equals(">")){
			if (opp){
				compiler.addInstruction(new BLE(label));
			}else {
				compiler.addInstruction(new BGT(label));
			}
		}
		else if (getOperatorName().equals(">=")){
			if (opp){
				compiler.addInstruction(new BLT(label));
			}else {
				compiler.addInstruction(new BGE(label));
			}
		}
		else if (getOperatorName().equals("==")){
			if (opp){
				compiler.addInstruction(new BNE(label));
			}else {
				compiler.addInstruction(new BEQ(label));
			}
		}
		else if (getOperatorName().equals("!=")){
			if (opp){
				compiler.addInstruction(new BEQ(label));
			}else {
				compiler.addInstruction(new BNE(label));
			}
		}
		else {
			throw new DecacInternalError("Comparaison operator shouldn't be parsed");
		}
	}


}
