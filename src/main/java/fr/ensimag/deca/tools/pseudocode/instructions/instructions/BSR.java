package fr.ensimag.deca.tools.pseudocode.instructions;

import fr.ensimag.deca.tools.pseudocode.DVal;
import fr.ensimag.deca.tools.pseudocode.Label;
import fr.ensimag.deca.tools.pseudocode.LabelOperand;
import fr.ensimag.deca.tools.pseudocode.UnaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class BSR extends UnaryInstruction {

    public BSR(DVal operand) {
        super(operand);
    }
    
    public BSR(Label target) {
        super(new LabelOperand(target));
    }

}
