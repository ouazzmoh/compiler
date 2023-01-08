package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.GPRegister;
import fr.ensimag.deca.tools.pseudocode.UnaryInstructionToReg;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class SHL extends UnaryInstructionToReg {
    public SHL(GPRegister op1) {
        super(op1);
    }
}
