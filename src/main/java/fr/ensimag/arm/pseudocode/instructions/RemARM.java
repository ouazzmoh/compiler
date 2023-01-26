package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

import java.io.PrintStream;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1%r2
* @author aitdriss
* @date 23/01/2023
*/

public class RemARM extends TernaryInstructionArm {
	public RemARM(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	 
	 @Override
		protected void display(PrintStream s){
		String r0 = getOperand1().toString();
		String r1 = getOperand2().toString();
		String r2 = getOperand3().toString();
		 s.println("#start of division algorithm to calculate the modulo");
		 s.println("substract:");
		 s.println("\t\tsubs "+ r1+ "," + r1 +" ," + r2);
		 s.println("\t\tBPL substract");
		 s.println("\t\tadd "+ r0+ "," + r1 +" ," + r2);
		 s.println("#end of division algorithm to calculate the modulo");
	 }

}
