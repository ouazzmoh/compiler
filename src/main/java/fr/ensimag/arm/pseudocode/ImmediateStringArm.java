package fr.ensimag.arm.pseudocode;

import fr.ensimag.arm.pseudocode.OperandArm;

public class ImmediateStringArm extends DValArm{
	    private String value;
	    public ImmediateStringArm(String value) {
	        super();
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return "\"" + value.replace("\"", "\"\"") + "\"";
	    }
	}
