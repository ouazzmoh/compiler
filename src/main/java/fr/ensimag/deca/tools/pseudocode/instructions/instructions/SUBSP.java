package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.ImmediateInteger;
import fr.ensimag.deca.tools.pseudocode.UnaryInstructionImmInt;

/**
 *
 * @author Ensimag
 * @date 01/01/2023
 */
public class SUBSP extends UnaryInstructionImmInt {

    public SUBSP(ImmediateInteger operand) {
        super(operand);
    }

    public SUBSP(int i) {
        super(i);
    }

}
