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
		String r1 = getOperand2().toString();
		String r2 = getOperand3().toString();
		 s.println("#start of division algorithm");
		 s.println("substract:");
		 s.println("\t\tsubs "+ r0+ "," + r1 +" ," + r2);
		 s.println("\t\tadd "+ r0 + ", " + r0 + ", " + "#1");
		 s.println("\t\tBHI substract");
		 s.println("_substract:");
		 s.println("\t\tsubs "+ r1+ "," + r1 +" ," + r2);
		 s.println("\t\tadd "+ r0 + ", " + r0 + ", " + "#1");
		 s.println("\t\tBHI substract");
		 s.println("#end of division algorithm");
	 }

}
