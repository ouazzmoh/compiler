package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

import fr.ensimag.ima.pseudocode.DAddr;

/**
 * STR : The STR instruction STR operation: stores
 *  the value found in a register to a memory address
 *
 * @author aitdriss
 * @date 17/01/2023
 */

public class STR extends BinaryInstructionArm{
	public STR(GPRegisterArm op1, OperandArm adr) {
		super(op1, adr);
	}
}