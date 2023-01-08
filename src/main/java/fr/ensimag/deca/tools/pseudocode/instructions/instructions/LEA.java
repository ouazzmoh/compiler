package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.BinaryInstructionDAddrToReg;
import fr.ensimag.deca.tools.pseudocode.DAddr;
import fr.ensimag.deca.tools.pseudocode.GPRegister;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class LEA extends BinaryInstructionDAddrToReg {

    public LEA(DAddr op1, GPRegister op2) {
        super(op1, op2);
    }

}
