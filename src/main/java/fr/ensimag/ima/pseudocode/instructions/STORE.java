package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.BinaryInstruction;
import fr.ensimag.deca.tools.pseudocode.DAddr;
import fr.ensimag.deca.tools.pseudocode.Register;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class STORE extends BinaryInstruction {
    public STORE(Register op1, DAddr op2) {
        super(op1, op2);
    }
}
