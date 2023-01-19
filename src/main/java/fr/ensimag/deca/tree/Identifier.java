package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.arm.pseudocode.ArmProgram;
import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.arm.pseudocode.DValArm;
import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.ImmediateIntegerArm;
import fr.ensimag.arm.pseudocode.ImmediateStringArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.RegisterArm;
import fr.ensimag.arm.pseudocode.RegisterOffsetArm;
import fr.ensimag.arm.pseudocode.instructions.LDR;
import fr.ensimag.arm.pseudocode.instructions.MOV;
import fr.ensimag.arm.pseudocode.instructions.SWI;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	ExpDefinition def = localEnv.get(name);
    	if (def == null) {
    		throw new ContextualError("le type de la variable n'est pas déclaré", this.getLocation());
    	}
    	this.setDefinition(def);
    	this.setType(def.getType());
    	return this.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	try {
    		return compiler.environmentType.defOfType(name).getType();
    	} catch (Exception e) {
    		throw new ContextualError("type not declared in env", this.getLocation());
    	}
    	
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        if (this.getType().isInt()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
            compiler.addInstruction(new WINT());
        } else if (this.getType().isFloat()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
            if (hex) {
                compiler.addInstruction(new WFLOATX());
            }
            else {
                compiler.addInstruction(new WFLOAT());
            }
        }
        else{
            throw new DecacInternalError("Cannot print expression");
        }

    }
    
    
    @Override
    protected void codeGenPrintArm(DecacCompiler compiler, boolean hex){
        if (this.getType().isInt()) {
        	LabelArm lab = DecacCompiler.getLabel();
        	LabelArm lab2 = new LabelArm(DecacCompiler.getLabel().toString() + "toPrint");
        	//DecacCompiler.data.put(lab, null);
        	DecacCompiler.data.put(lab2, new ImmediateStringArm(DecacCompiler.data.get(lab).toString()));
            compiler.addInstruction(new LDR(GPRegisterArm.getR(2), lab2));
            compiler.addInstruction(new MOV(RegisterArm.getR(0), new ImmediateIntegerArm(4) ));
            compiler.addInstruction(new MOV(RegisterArm.getR(1), new ImmediateIntegerArm(1) ));
            compiler.addInstruction(new MOV(RegisterArm.getR(2), RegisterArm.getR(2) ));
            compiler.addInstruction(new MOV(RegisterArm.getR(3), new ImmediateIntegerArm(4) ));
	        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
        	
        	
        } else if (this.getType().isFloat()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
            if (hex) {
                compiler.addInstruction(new WFLOATX());
            }
            else {
                compiler.addInstruction(new WFLOAT());
            }
        }
        else{
            throw new DecacInternalError("Cannot print expression");
        }

    }


    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getFreeReg();
        DVal toLoad = getExpDefinition().getOperand();
        compiler.addInstruction(new LOAD(toLoad, registerToUse));
        compiler.useReg();
        return registerToUse;
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(getVariableDefinition().getOperand(), Register.R1)); //No need to use and free
        compiler.addInstruction(new PUSH(Register.R1));
    }


    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        compiler.addInstruction(new LOAD(getVariableDefinition().getOperand(), Register.R1));
        compiler.addInstruction(new CMP(0, Register.R1));
        if (b){
            compiler.addInstruction(new BNE(label));
        }
        else {
            compiler.addInstruction(new BEQ(label));
        }

    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        compiler.addInstruction(new LOAD(getVariableDefinition().getOperand(), Register.R1));
        compiler.addInstruction(new STORE(Register.R1, adr));
    }


	@Override
	protected void codeGenInitArm(DecacCompiler compiler, DAddrArm adr) {
		// TODO Auto-generated method stub
		//DecacCompiler.data.put(new LabelArm(getVariableDefinition().getOperand().toString()),new ImmediateIntegerArm(4) );
		
	}

	@Override
	protected void codeGenInstArm(DecacCompiler compiler, Label endIf) {
		// TODO Auto-generated method stub
		
	}

}
