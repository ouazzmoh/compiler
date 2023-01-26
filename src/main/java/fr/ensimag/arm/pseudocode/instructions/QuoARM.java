package fr.ensimag.arm.pseudocode.instructions;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.*;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1//r2
* Uses the register r9 for temporary calculations, it's value is restored at the end
* @author aitdriss
* @date 23/01/2023
*/

public class QuoARM extends TernaryInstructionArm {
	public QuoARM(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	 
	 @Override
		protected void display(PrintStream s){
		String r0 = getOperand1().toString();
		String r1 = getOperand2().toString();
		String r2 = getOperand3().toString();
		 s.println("#start of division algorithm");
		 s.println("\t\tpush {r9}");
		 s.println("\t\tmov r9 , #0");
		 s.println("substract:");
		 s.println("\t\tsubs "+ r1+ "," + r1 +" ," + r2);
		 s.println("\t\tadd r9, r9, #1");
		 s.println("\t\tBPL substract");
		 s.println("\t\tsub r9, r9, #1");
		 s.println("\t\tmov " + r0 + ", r9");
		 s.println("\t\tpop {r9}");
		 s.println("#end of division algorithm");
	 }

}
