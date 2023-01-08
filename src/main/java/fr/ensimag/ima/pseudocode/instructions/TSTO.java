package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.ImmediateInteger;
import fr.ensimag.deca.tools.pseudocode.UnaryInstructionImmInt;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class TSTO extends UnaryInstructionImmInt {
    public TSTO(ImmediateInteger i) {
        super(i);
    }

    public TSTO(int i) {
        super(i);
    }
}
