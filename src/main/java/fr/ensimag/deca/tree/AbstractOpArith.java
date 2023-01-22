package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.awt.peer.ComponentPeer;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

	protected String ovLabelInt = "err_ov_div_int";
	protected String ovLabel = "err_ov_arith";

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (this.getOperatorName().equals("+") | this.getOperatorName().equals("-") | this.getOperatorName().equals("*")
    			| this.getOperatorName().equals("/") ) {
    			if (t1.sameType(t2) && (t1.isInt() | t1.isFloat())) {
    				this.setType(t1);
    				return t1;
    			}
    			if (t1.isInt() && t2.isFloat()) {
    				this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
    				//this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, t2));
    				this.setType(t2);
    				return t2;
    			}
    			if (t1.isFloat() && t2.isInt()) {
    				this.setRightOperand(new ConvFloat(this.getRightOperand()));
   				
    				//this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t1));
    				this.setType(t1);
    				return t1;
    			}
    		}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands's type not permetted", this.getLocation());
    	}



	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){

		GPRegister result = (GPRegister) codeGenLoad(compiler);
		compiler.addInstruction(new STORE(result, adr));
		compiler.freeReg();
	}

//	@Override
//	protected DVal codeGenLoad(DecacCompiler compiler){
//		addArithErrors(compiler);
//		if (!compiler.useLoad() &&!(getRightOperand() instanceof IntLiteral
//				| getRightOperand() instanceof Identifier | getRightOperand() instanceof FloatLiteral)){
//			//Store left operand in register
//			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
//			compiler.addInstruction(new PUSH(opLeft));
//			compiler.freeReg();
//			GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
//			compiler.addInstruction(new POP(Register.R0));
//			codeGenOpMnem(compiler, opRight, Register.R0);
//			compiler.addInstruction(new LOAD(Register.R0, opRight)); //We store the value because R0 is going to be reused
//			return	opRight;
//		}
//		GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
//		//Evaluate right
//		DVal opRight;
//		//Using instanceof here allows us to optimize the assembly code generated
//		if (getRightOperand() instanceof IntLiteral){
//			opRight = new ImmediateInteger(((IntLiteral)getRightOperand()).getValue());
//		}
//
//		else if (getRightOperand() instanceof Identifier){
//			if (((Identifier)getRightOperand()).getExpDefinition().getOperand() == null){
//				//In this case the right operand is a field inside the class
//				GPRegister thisReg = compiler.getFreeReg();
//				compiler.useReg();
//				compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
//				int index = ((Identifier)getRightOperand()).getFieldDefinition().getIndex();
//				((Identifier)getRightOperand()).getExpDefinition().setOperand(new RegisterOffset(index, thisReg));
//				opRight = ((Identifier)getRightOperand()).getExpDefinition().getOperand();
//				//Set adress to null after using it
//				((Identifier) getRightOperand()).getExpDefinition().setOperand(null);
//				compiler.freeReg();
//			}
//			else {
//				opRight = ((Identifier)getRightOperand()).getExpDefinition().getOperand();
//			}
//		}
//		else if (getRightOperand() instanceof FloatLiteral){
//			opRight = new ImmediateFloat(((FloatLiteral)getRightOperand()).getValue());
//		}
//		else {
//			opRight = getRightOperand().codeGenLoad(compiler);
//			compiler.freeReg();
//		}
//		//Do the operation
//		codeGenOpMnem(compiler, opRight, opLeft);
//		return opLeft;
//	}

	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		addArithErrors(compiler);
		if (!compiler.useLoad() &&!(getRightOperand() instanceof IntLiteral
				| getRightOperand() instanceof Identifier | getRightOperand() instanceof FloatLiteral)){
			//Store left operand in register
			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
			compiler.addInstruction(new PUSH(opLeft));
			compiler.freeReg();
			GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
			compiler.addInstruction(new POP(Register.R0));
			codeGenOpMnem(compiler, opRight, Register.R0);
			compiler.addInstruction(new LOAD(Register.R0, opRight)); //We store the value because R0 is going to be reused
			return	opRight;
		}
		GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
		//Evaluate right
		DVal opRight;
		//Using instanceof here allows us to optimize the assembly code generated
		if (getRightOperand() instanceof IntLiteral){
			opRight = new ImmediateInteger(((IntLiteral)getRightOperand()).getValue());
		}
		else if (getRightOperand() instanceof Identifier){
			opRight = ((Identifier)getRightOperand()).getVariableDefinition().getOperand();
		}
		else if (getRightOperand() instanceof FloatLiteral){
			opRight = new ImmediateFloat(((FloatLiteral)getRightOperand()).getValue());
		}
		else {
			opRight = getRightOperand().codeGenLoad(compiler);
			compiler.freeReg();
		}
		//Do the operation
		codeGenOpMnem(compiler, opRight, opLeft);
		return opLeft;
	}





	/**
	 * Generate MNEM(dval1, dval2) corresponding to the operation
	 * It generates BOV after to catch the execution error
	 * @param compiler
	 * @param dval1
	 * @param dval2
	 */
	protected void codeGenOpMnem(DecacCompiler compiler,DVal dval1, GPRegister dval2){
		if (getOperatorName().equals("+")){
			compiler.addInstruction(new ADD(dval1, dval2));
			compiler.addInstruction(new BOV(new Label(ovLabel)));
		}
		else if (getOperatorName().equals("-")){
			compiler.addInstruction(new SUB(dval1, dval2));
			compiler.addInstruction(new BOV(new Label(ovLabel)));
		}
		else if (getOperatorName().equals("*")){
			compiler.addInstruction(new MUL(dval1, dval2));
			compiler.addInstruction(new BOV(new Label(ovLabel)));
		}
		else if (getOperatorName().equals("/")){
			Type typeLeft = this.getLeftOperand().getType();
			Type typeRight = this.getRightOperand().getType();
			if (typeLeft.isFloat() || typeRight.isFloat()){
				compiler.addInstruction(new DIV(dval1, dval2));
				compiler.addInstruction(new BOV(new Label(ovLabel)));
			} else if (typeLeft.isInt() && typeRight.isInt()) {
				compiler.addInstruction(new QUO(dval1, dval2));
				compiler.addInstruction(new BOV(new Label(ovLabelInt)));
			} else {
				throw new DecacInternalError("Operandes pour la division non valide");
			}
		}
		else if (getOperatorName().equals("%")){
			compiler.addInstruction(new REM(dval1, dval2));
			compiler.addInstruction(new BOV(new Label(ovLabelInt)));
		}
		else{
			throw new DecacInternalError("Error in parsing");
		}
	}




	/**
	 * Adds error labels for arithmetique operations
	 * err_ov_arith
	 * err_ov_div_int if the two operands are integers
	 * @param compiler
	 */
	protected void addArithErrors(DecacCompiler compiler){
//		Type typeLeft = this.getLeftOperand().getType();
		Type typeRight = this.getRightOperand().getType();
//		if (typeLeft.isInt() && typeLeft.isInt()){
//			compiler.addError(ovLabelInt, "Erreur : Division entiere par 0");
//		}
		compiler.addError(ovLabel, "Erreur: debordement arithmetique --> non codable ou division par 0.0");
	}



	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean hex){
		GPRegister reg = (GPRegister) codeGenLoad(compiler);
		compiler.addInstruction(new LOAD(reg, Register.R1));
		Type typeLeft = this.getLeftOperand().getType();
		Type typeRight = this.getRightOperand().getType();
		if (typeLeft.isInt() && typeLeft.isInt()){
			compiler.addInstruction(new WINT());
		}
		else {
			if (hex) {
				compiler.addInstruction(new WFLOATX());
			}
			else {
				compiler.addInstruction(new WFLOAT());
			}
		}
	}


}
