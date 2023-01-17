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
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t = this.verifyExpr(compiler, localEnv, currentClass);
    	if (t.sameType(expectedType)) {
    		return this;
    	}
    	if (expectedType.isFloat() && t.isInt()) {
    		return new ConvFloat(this);
    	}
    	throw new ContextualError("Assignment Error", this.getLocation());
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.verifyExpr(compiler, localEnv, currentClass);
    	
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t = this.verifyExpr(compiler, localEnv, currentClass);
    	if (!t.isBoolean()) {
    		throw new ContextualError("not boolean", this.getLocation());
    	}
    	this.setType(t);
    }

    /**
     * Generate code to print the expression
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
    	//throw new DecacInternalError("expression cannot be printed");
        
    }
    

    /**
     * Generate the code corresponding to the instruction
     * @param compiler
     * @param endIf : useful to store the endIf label in if instructions
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler, Label endIf) {
        throw new UnsupportedOperationException("no available code generation for this instruction");
    }

    /**
     * Generate assembly code for the initialization of the expression
     * @param compiler
     * @param adr
     */
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        throw new DecacInternalError("Shouldn't be initialized");
    }

    /**
     * Generate assignment code for the expression
     * @param compiler
     * @param identifier
     */
    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
        this.codeGenInit(compiler, identifier.getExpDefinition().getOperand());
    }


    /**
     * Loads the value of the expression in a register and sets it as used (increments the current register)
     * We need to update the register descriptor after no longer using it
     * @param compiler
     * @return register
     */
    protected DVal codeGenLoad(DecacCompiler compiler){
        throw new DecacInternalError("Cannot load the expression");
    }


    /**
     * Push the value of the expression in the temporary memory
     * Use POP(Rm) later to retrieve the value in the Rm register
     * @param compiler
     */
    protected void codeGenPush(DecacCompiler compiler){
        throw new DecacInternalError("Cannot push the expression");
    }


    /**
     * Generate branching operations
     * branch to this label if the exprBool == b
     * @param compiler
     * @param b : true or false/ compare the exprBool to this
     * @param label :
     */
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        throw new DecacInternalError("Expression cannot be used for boolean expressions");
    }



    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

	protected void codeGenPrintArm(DecacCompiler compiler, boolean hex) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Generate assembly code for the initialization of the expression
     * @param compiler
     * @param adr
     */
    protected void codeGenInitArm(DecacCompiler compiler, DAddrArm adr){
        throw new DecacInternalError("Shouldn't be initialized");
    }

}
