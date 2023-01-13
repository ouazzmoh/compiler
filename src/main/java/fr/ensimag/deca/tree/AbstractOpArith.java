package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
		//Loads the result of the left operand
		DVal opLeft = getLeftOperand().codeGenLoad(compiler);
		//Loads the result of the right operand (must be a register)
		DVal opRight = getRightOperand().codeGenLoad(compiler);

		//Loads the result of the operation in the toStore (must be a register) (Used because we can store different
		// operands)
		DVal toStore = this.codeGenLoad(compiler, opLeft, opRight);

		//Storing the value of the operation in the memory
		compiler.addInstruction(new STORE((GPRegister) toStore, adr));
		//Updating the register descriptor
		compiler.getRegisterDescriptor().freeRegister((GPRegister) toStore);
	}


	/**
	 * Loads the result of the operations and returns it in a register (Useful for the recursive
	 * implementation of the operations
	 * @param compiler
	 * @param opLeft
	 * @param opRight
	 * @return
	 */
	protected DVal codeGenLoad(DecacCompiler compiler, DVal opLeft, DVal opRight){
		throw new UnsupportedOperationException("not implemented");
	}


	/**
	 * Does the operation between the two operands and returns the result in a register
	 * @param compiler
	 * @return register with the value of the operation
	 */
	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		//Loads the result of left operand
		DVal opLeft = getLeftOperand().codeGenLoad(compiler);
		//Loads the result of the right operand
		DVal opRight = getRightOperand().codeGenLoad(compiler);
		//Returns the register with the result
		return this.codeGenLoad(compiler, opLeft, opRight);
	}


}
