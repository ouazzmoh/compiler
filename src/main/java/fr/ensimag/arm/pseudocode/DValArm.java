package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

/**
 * Operand of an ARM Instruction.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public abstract class DValArm extends OperandArm{
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	

    void display(PrintStream s){
    	s.println("		" + ArmProgram.getLabel(0) + ":");
    	s.println("		.ascii " + toString());
        s.println();
    }


}
