package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.*;

/**
 * Blt : goes to the bloc of instructions specified if the cmp
 * instruction before specifies that there is a inequality as r1<=r2.
 * "blt _next" exemple
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmBlt extends UnaryInstructionArm{
	public ArmBlt(OperandArm op1) {
		super(op1);
	}
}