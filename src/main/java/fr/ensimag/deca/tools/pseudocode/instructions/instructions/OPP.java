package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.deca.tools.pseudocode.DVal;
import fr.ensimag.deca.tools.pseudocode.GPRegister;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class OPP extends BinaryInstructionDValToReg {
    public OPP(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
}