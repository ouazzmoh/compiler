package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

/**
 *Line of code in an Arm Program
 *
 * @author ouazzmoh
 * @date 10/01/2023
 */
public class LineArm extends AbstractLineArm {


    private InstructionArm instruction;
//    private String comment;
//    private Label label;

    @Override
    void display(PrintStream s){
        //TODO: comments ..etc
        if (instruction != null){
            instruction.display(s);
        }
        s.println();
    }

    public InstructionArm getInstruction() {
        return instruction;
    }

    public void setInstruction(InstructionArm instruction) {
        this.instruction = instruction;
    }
}
