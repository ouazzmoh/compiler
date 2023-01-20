package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.ArmADD;
import fr.ensimag.arm.pseudocode.instructions.LDR;
import fr.ensimag.arm.pseudocode.instructions.LDRreg;
import fr.ensimag.arm.pseudocode.instructions.STR;
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


	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){

		addArithErrors(compiler);
		//Calculate the result
		GPRegister result = (GPRegister) codeGenLoad(compiler);
		//Store result
		compiler.addInstruction(new STORE(result, adr));
		compiler.freeReg();
	}


	/**
	 * Does the operation between the two operands and returns the result in a register
	 * @param compiler
	 * @return opLeft with the value of the operation
	 */
	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		addArithErrors(compiler);
		//Store left in register: If no freeing problems, we are sure there is at least one free register
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
		else {
		//testing if we can use LOAD or we should use PUSH/POP
			if (!compiler.useLoad()){
				compiler.addInstruction(new PUSH(opLeft));
				compiler.freeReg(); //free the left because it is pushed
				opRight = getRightOperand().codeGenLoad(compiler);
				compiler.addInstruction(new POP(Register.R0));
				opLeft = Register.R0;
			}
			else {
				opRight = getRightOperand().codeGenLoad(compiler);
				compiler.freeReg();
			}

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
	protected void codeGenOpMnem(DecacCompiler compiler, DVal dval1, GPRegister dval2){
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
		Type typeLeft = this.getLeftOperand().getType();
		Type typeRight = this.getRightOperand().getType();
		if (typeLeft.isInt() && typeLeft.isInt()){
			compiler.addError(ovLabelInt, "Erreur : Division entiere par 0");
		}
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



	@Override
	protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){

		GPRegisterArm result = (GPRegisterArm) codeGenLoadArm(compiler);
		//Store result
		compiler.addInstruction(new LDR(RegisterArm.R0, (LabelArm)adr));
		compiler.addInstruction(new STR(result, new RegisterOffsetArm(0, RegisterArm.R0)));
		compiler.freeRegArm();
	}


	/**
	 * Does the operation between the two operands and returns the result in a register
	 * @param compiler
	 * @return opLeft with the value of the operation
	 */
	@Override
	protected DValArm codeGenLoadArm(DecacCompiler compiler){
		//Store left in register: If no freeing problems, we are sure there is at least one free register
		GPRegisterArm opLeft = (GPRegisterArm) getLeftOperand().codeGenLoadArm(compiler);
		//Evaluate right
		GPRegisterArm opRight = (GPRegisterArm) getRightOperand().codeGenLoadArm(compiler);
		//Do the operation
		//TODO: Optimize for immediates
		compiler.addInstruction(new ArmADD(opLeft, opLeft, opRight));
		compiler.freeRegArm();
		return opLeft;

	}



}
