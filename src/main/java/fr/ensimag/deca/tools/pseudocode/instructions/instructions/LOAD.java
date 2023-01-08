package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.deca.tools.pseudocode.DVal;
import fr.ensimag.deca.tools.pseudocode.GPRegister;
import fr.ensimag.deca.tools.pseudocode.ImmediateInteger;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

}
