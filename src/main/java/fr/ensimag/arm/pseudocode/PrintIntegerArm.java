package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.arm.pseudocode.InstructionArm;

/**
* PrintIntegerArm: displays the instruction that print and integer 
* this algorithm takes the integer and uses multiple division to display
* the integer digit by digit
* ""R5 is where we load the adress of the integer""
* we need the label (lab ) of the integer (num in the example made) to print digits 
* 
* @author aitdriss
* @date 23/01/2023
*/

public class PrintIntegerArm extends InstructionArm {
	public LabelArm lab;
	public PrintIntegerArm() {
		//nothing to do
	}
	 @Override
	 public String getName() {
	        return this.getClass().getSimpleName().toLowerCase();
	    }

	@Override
	protected
    void displayOperands(PrintStream s) {
        s.print(" ");
	    }
	 
	 @Override
	    protected void display(PrintStream s){
	        s.println("	LDR r3, [R5] ");
	        s.println("	    mov r6, r3"); 
	        s.println("    mov r8, #0"); 
	        s.println("	    bl _compteur");
	        s.println("_compteur:");
	        s.println("	    ldr    r4,=0xcccccccd"); 
	        s.println("	    umull  r0,r1,r6,r4"); 
	        s.println("	    mov    r0,r1,lsr #3"); 
	        s.println("	    mov r6, r0"); 
	        s.println("	    cmp r0, #0"); 
	        s.println("	    beq _fct"); 
	        s.println("	    add r8, r8, #1"); 
	        s.println("	    b _compteur");
	        	    
	        s.println("_div10:");
	        s.println("	    cmp r5, #0");
	        s.println("	    mov r9, r0");
	        s.println("	    beq _mult");
	        s.println("	    ldr    r4,=0xcccccccd");
	        s.println("	    umull  r0,r1,r3,r4");
	        s.println("	    mov    r0,r1,lsr #3");
	        s.println("	    mov r3, r0");
	        s.println("	    sub r5, r5, #1");
	        s.println("	    b _div10");
	        
	        s.println("_mult:");
	        s.println("	    mov r2, R9");
	        s.println("	    mov r4, #10");
	        s.println("	    mul r9, r2, r4");
	        s.println("	    mov r4, r9");
	        s.println("	    add r5,#1");
	        s.println("	    cmp r5, r8");
	        s.println("	    beq _next");
	        s.println("	    mov r2, r9");
	        s.println("	    b _mult");
	        
	        s.println("_next: ");
	        s.println("	    add r0, r0, #48");
	        s.println("	    ldr r9, =" + lab.toString());
	        s.println("	    str r0, r9]");
	        s.println("	    mov r0, #1");
	        s.println("	    ldr r1, =" + lab.toString());
	        s.println("	    mov r2, #1");
	        s.println("	    mov r7, #4");
	        s.println("	    swi 0");
	        s.println("	    sub r3, r6,  r4");
	        s.println("	    sub r8, #1");
	        s.println("	    bl _fct");
	        s.println("_fct:");
	        s.println("	    mov r6, r3");
	        s.println("	    mov r5 , r8");
	        s.println("	    cmp r8, #0");
	        s.println("	    bgt _div10");
	        s.println("	    mov r0, r3");
	        s.println("	    add r0, r0, #48");
	        
	        s.println("	    ldr r9, =" + lab.toString());
	        s.println("	    str r0, [r9]");
	        s.println("	    mov r0, #1");
	        s.println("	    ldr r1, =" + lab.toString());
	        
	        s.println("	    mov r2, #1");
	        s.println("	    mov r7, #4");
	        s.println("	    swi 0");
	        s.println("	    mov r7, #1");
	        s.println("	    mov r0, #0");
	        s.println("	    swi 0");

	        s.println(" _end:");
	        s.println("	    mov r7, #1");
	        s.println("	    mov r0, #0");
	        s.println("	    swi 0");
	    }
	}