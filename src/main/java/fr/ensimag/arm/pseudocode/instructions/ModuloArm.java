package fr.ensimag.arm.pseudocode.instructions;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.*;
import org.apache.commons.lang.Validate;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1//r2
* le ro doit etre initialisé à 0 avant d appeler la fct
* @author aitdriss
* @date 23/01/2023
*/

public class ModuloArm extends TernaryInstructionArm {
	public ModuloArm(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	 
	 @Override
		protected void display(PrintStream s){
		String r0 = getOperand1().toString();
		String r1 = getOperand1().toString();
		String r2 = getOperand1().toString();
		 s.println("substract:");
		 s.println("       subs r1,r1,r2");
		 s.println("       add r0, r0, #1");
		 s.println("       BHI substract");
		 s.println("_substract:");
		 s.println("           subs r1,r1,r2");
		 s.println("           add r0, r0, #1");
		 s.println("           BHI substract");
	 }

}
