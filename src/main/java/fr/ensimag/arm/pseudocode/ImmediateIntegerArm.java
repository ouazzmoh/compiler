package fr.ensimag.arm.pseudocode;

/**
 * Immediate integer.
 * 
 * @author aitdriss
 * @date 10/01/2023
 */


public class ImmediateIntegerArm extends DValArm{
	private int value;

    public ImmediateIntegerArm(int value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    	//return "" + value;
    }
    
    @Override
    public String toStringSWI() {
        return " " + value;
    	//return "" + value;
    }

}
