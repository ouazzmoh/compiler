package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.GPRegister;
import fr.ensimag.deca.tools.pseudocode.UnaryInstructionToReg;

/**
 *
 * @author Ensimag
 * @date 01/01/2023
 */
public class POP extends UnaryInstructionToReg {

    public POP(GPRegister op) {
        super(op);
    }

}