package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.arm.pseudocode.InstructionArm;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1//r2
* le ro doit etre initialisé à 0 avant d appeler la fct
* @author aitdriss
* @date 23/01/2023
*/

public class ModuloArm extends InstructionArm {
	public ModuloArm() {
		//nothing to do
	}
	
	@Override
	protected
    void displayOperands(PrintStream s) {
	    }
	 
	 @Override
	    protected void display(PrintStream s){
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
