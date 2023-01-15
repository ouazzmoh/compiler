package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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


	/**
	 * Initialization of a variable via an arithmetic operation
	 * @param compiler
	 * @param adr
	 */
	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		//Todo: optimize with loading the left directly in some cases
		//If the registers are full we use the PUSH POP instructions
		addArithErrors(compiler);
		if (!compiler.getRegisterDescriptor().useLoad()){codeGenInitPush(compiler, adr);}
		else{
			//Loads the result of the left operand
			DVal opLeft = getLeftOperand().codeGenLoad(compiler);

			//Loads the result of the right operand (must be a register)
			DVal opRight = getRightOperand().codeGenLoad(compiler);
			codeGenOpMnem(compiler, opRight, (GPRegister)opLeft);

			//Storing the value of the operation in the memory
			compiler.addInstruction(new STORE((GPRegister) opLeft, adr));
			//Updating the register descriptor
			compiler.getRegisterDescriptor().freeRegister((GPRegister) opLeft);
		}


	}

	protected void codeGenInitPush(DecacCompiler compiler, DAddr adr) {
			//Loads the result of the left operand
			getLeftOperand().codeGenPush(compiler);
			DVal opRight = getRightOperand().codeGenLoad(compiler);
			//Loads the result of the operation in the toStore (must be a register) (Used because we can store different
			// operands)
			compiler.addInstruction(new POP(Register.R0));

			codeGenOpMnem(compiler, opRight, Register.R0);
			//Storing the value of the operation in the memory
			compiler.addInstruction(new STORE(Register.R0, adr));
	}

//	protected void codeGenBov(DecacCompiler compiler){
//		Type typeLeft = this.getLeftOperand().getType();
//		Type typeRight = this.getRightOperand().getType();
//		if (getOperatorName().equals("/") && typeLeft.isInt() && typeRight.isInt()){
//			compiler.addInstruction(new BOV(new Label(ovLabelInt)));
//		}
//		else {
//			compiler.addInstruction(new BOV(new Label(ovLabel)));
//		}
//	}

//	/**
//	 * Loads the result of the operations and returns it in a register (Useful for the recursive
//	 * implementation of the operations
//	 * @param compiler
//	 * @param opLeft
//	 * @param opRight
//	 * @return
//	 */
//	protected abstract DVal codeGenLoad(DecacCompiler compiler, DVal opLeft, DVal opRight);


	/**
	 * Does the operation between the two operands and returns the result in a register
	 * @param compiler
	 * @return opLeft with the value of the operation
	 */
	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		addArithErrors(compiler);
		//Loads the result of left operand
		DVal opLeft = getLeftOperand().codeGenLoad(compiler);
		//Loads the result of the right operand
		DVal opRight = getRightOperand().codeGenLoad(compiler);
		//Returns the register with the result
		codeGenOpMnem(compiler, opRight, (GPRegister)opLeft);
		compiler.getRegisterDescriptor().freeRegister((GPRegister) opRight);
		return opLeft;

	}

	@Override
	protected void codeGenPush(DecacCompiler compiler){
		addArithErrors(compiler);
		GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
		getLeftOperand().codeGenPush(compiler);
		DVal opRight = getRightOperand().codeGenLoad(compiler);

		compiler.addInstruction(new POP(Register.R0));
		codeGenOpMnem(compiler, opRight, Register.R0);
		compiler.getRegisterDescriptor().freeRegister((GPRegister)opRight);
		compiler.addInstruction(new PUSH(Register.R0));
	}

	/**
	 * Generate MNEM(dval1, dval2) corresponding to the operation
	 * It generates BOV after to catch the execution error
	 * @param compiler
	 * @param dval1
	 * @param dval2
	 */
	protected void codeGenOpMnem(DecacCompiler compiler, DVal dval1, DVal dval2){
		if (getOperatorName().equals("+")){
			compiler.addInstruction(new ADD(dval1, (GPRegister)dval2));
			compiler.addInstruction(new BOV(new Label(ovLabelInt)));
		}
		else if (getOperatorName().equals("-")){
			compiler.addInstruction(new SUB(dval1, (GPRegister)dval2));
			compiler.addInstruction(new BOV(new Label(ovLabel)));
		}
		else if (getOperatorName().equals("*")){
			compiler.addInstruction(new MUL(dval1, (GPRegister)dval2));
			compiler.addInstruction(new BOV(new Label(ovLabel)));
		}
		else if (getOperatorName().equals("/")){
			Type typeLeft = this.getLeftOperand().getType();
			Type typeRight = this.getRightOperand().getType();
			if (typeLeft.isFloat() || typeRight.isFloat()){
				compiler.addInstruction(new DIV(dval1, (GPRegister)dval2));
				compiler.addInstruction(new BOV(new Label(ovLabel)));
			} else if (typeLeft.isInt() && typeRight.isInt()) {
				compiler.addInstruction(new QUO(dval1, (GPRegister)dval2));
				compiler.addInstruction(new BOV(new Label(ovLabelInt)));
			} else {
				throw new DecacInternalError("Operandes pour la division non valide");
			}
		}
		else if (getOperatorName().equals("%")){
			compiler.addInstruction(new REM(dval1, (GPRegister)dval2));
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
		Type typeLeft = this.getLeftOperand().getType();
		Type typeRight = this.getRightOperand().getType();
		if (typeLeft.isInt() && typeLeft.isInt()){
			compiler.addError(ovLabelInt, "Erreur : Division entiere par 0");
		}
		compiler.addError(ovLabel, "Erreur : Debordement de pile non codable ou division par 0.0");
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
			compiler.addInstruction(new WFLOAT());
		}
	}


}
