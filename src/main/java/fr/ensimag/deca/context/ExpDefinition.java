package fr.ensimag.deca.context;

import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class ExpDefinition extends Definition {

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }
    
    public void setOperandArm(DAddrArm operandArm) {
        this.operandArm = operandArm;
    }

    public DAddrArm getOperandArm() {
		return operandArm;
	}

	public DAddr getOperand() {
        return operand;
    }
    private DAddr operand;
    private DAddrArm operandArm;

    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }

}
