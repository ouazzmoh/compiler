package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.BinaryInstructionArm;
import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;

/**
 * STR : The STR instruction STR operation: stores
 *  the value found in a register to a memory address
 *
 * @author aitdriss
 * @date 17/01/2023
 */

public class LDRreg extends BinaryInstructionArm{
	public LDRreg(GPRegisterArm op1, OperandArm adr) {
		super(op1, adr);
	}

	@Override
	public String getName(){
		return "ldr";
	}

//	protected void displayOperands(PrintStream s) {
//		s.print(" ");
//		s.print(this.getOperand1());
//		s.print(", [");
//		s.print(this.getOperand2());
//		s.print("]");
//	}
}