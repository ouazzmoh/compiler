package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

/**
 * ARM instruction
 *
 * @author ouazzmoh
 * @date 10/01/2023
 */
public abstract class InstructionArm {

    String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    abstract void displayOperands(PrintStream s);

    void display(PrintStream s){
        if (getName().equals("armadd")){
            s.print("		" + "add");
        }
        else if (getName().equals("ldrreg")){
            s.print("		" + "ldr");
        }
        else {
            s.print("		" + getName());
        }
        displayOperands(s);
    }
}
