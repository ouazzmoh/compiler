package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.arm.pseudocode.ArmProgram;
import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.arm.pseudocode.DValArm;
import fr.ensimag.arm.pseudocode.ImmediateIntegerArm;
import fr.ensimag.arm.pseudocode.ImmediateStringArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.RegisterArm;
import fr.ensimag.arm.pseudocode.instructions.LDR;
import fr.ensimag.arm.pseudocode.instructions.MOV;
import fr.ensimag.arm.pseudocode.instructions.SWI;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * String literal
 *
 * @author gl24
 * @date 01/01/2023
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.STRING);
    	return this.getType();
    }

//    @Override
//    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
//    	if (compiler.isArm == true) {
//	    	LabelArm lab = new LabelArm("message1");
//			DecacCompiler.data.put(lab,(DValArm)(new ImmediateStringArm(value.toString())));
//			compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
//	        compiler.addInstruction(new MOV(RegisterArm.getR(1), new ImmediateIntegerArm(1)));
//	        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab));
//	        int l = lab.toString().length();
//	        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(l-2) ));
//	        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
//    	}
//    	else {
//        compiler.addInstruction(new WSTR(new ImmediateString(value)));
//    }
//    }
//
//    /**
//     * Generate code to println the expression
//     * @param compiler
//     */
//    protected void codeGenPrintln(DecacCompiler compiler, boolean hex) {
//        //throw new DecacInternalError("expression cannot be printed")
//    	if (compiler.getIsArm() == true) {
//	    	LabelArm lab = new LabelArm("message1");
//			DecacCompiler.data.put(lab,(DValArm)(new ImmediateStringArm(this.toString() + "\n")));
//			compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
//	        compiler.addInstruction(new MOV(RegisterArm.getR(1), new ImmediateIntegerArm(1)));
//	        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab));
//	        int l = lab.toString().length();
//	        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(l-2) ));
//	        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
//    	}
//		else {
//			compiler.addInstruction(new WSTR(new ImmediateString(value + "\n")));
//		}
//
//
//    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print('"' + value + '"');
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }


	@Override
	protected void codeGenPrintArm(DecacCompiler compiler, boolean hex) {
		LabelArm lab = new LabelArm("message1");
		compiler.data.put(lab,(DValArm)(new ImmediateStringArm(value + "\n")));
		compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab));
        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(value.length() + 1) ));
        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
		
	}

	@Override
	protected void codeGenInstArm(DecacCompiler compiler, Label endIf) {
		// TODO Auto-generated method stub
		
	}

}
