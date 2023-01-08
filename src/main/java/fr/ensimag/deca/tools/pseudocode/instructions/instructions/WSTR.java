package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.ImmediateString;
import fr.ensimag.deca.tools.pseudocode.UnaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class WSTR extends UnaryInstruction {
    public WSTR(ImmediateString op) {
        super(op);
    }
    
    public WSTR(String message) {
        super(new ImmediateString(message));
    }
    
}
