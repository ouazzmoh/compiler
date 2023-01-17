package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

/**
 * MOV : The MOV instruction moves data bytes between the two specified operands.
 * The byte specified by the second operand is copied to the location specified 
 * by the first operand
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class MOV extends BinaryInstructionArm{
	public MOV(GPRegisterArm op1, DValArm op2) {
		super(op1, op2);
	}
}
