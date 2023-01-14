package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;
import java.io.UncheckedIOException;

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
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new DecacInternalError("expression cannot be printed");
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Generate assembly code for the initialization of the expression
     * @param compiler
     * @param adr
     */
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        throw new DecacInternalError("expression cannot be initialized");
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
     * To generate the code
     * CMP p, expression
     * BEQ label
     * @param compiler
     * @param label
     * @param p = 0 (false) or 1 (true)
     */
    protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
        throw new DecacInternalError("expression cannot be compared");
    }

    /**
     * Loads the value of the expression in a register and removes it
     * We need to update the register descriptor after no longer using it
     * @param compiler
     * @return register
     */
    protected DVal codeGenLoad(DecacCompiler compiler){
        throw new DecacInternalError("Cannot load the expression");
    }


    //    protected void codeGenInstWhile(DecacCompiler compiler,Label endWhile){
//        throw new UnsupportedOperationException("not yet implemented");
//    }

//    protected void codeGenSum(DecacCompiler compiler){};

//    protected DVal codeGenSum(DecacCompiler compiler){
//        throw new UnsupportedOperationException("not yet implemented");
//    }
//
//    protected DVal codeGenSub(DecacCompiler compiler){
//        throw new UnsupportedOperationException("not yet implemented");
//    }
//
//    protected DVal codeGenMul(DecacCompiler compiler){
//        throw new UnsupportedOperationException("not yet implemented");
////        return new NullOperand();
//    }

//    protected DVal codeGenDiv(DecacCompiler compiler){
//        throw new UnsupportedOperationException("not yet implemented");
////        return new NullOperand();
//    }




//    protected void codeGenAnd(DecacCompiler compiler, Label l){
//        throw new UnsupportedOperationException("not yet implemented");
//    }
//
    protected void codeGenBeq(DecacCompiler compiler, Label l1, Label l2){
        throw new UnsupportedOperationException("not yet implemented");
    }

//    protected void codeGenIf(DecacCompiler compiler, Label label){
//        throw new UnsupportedOperationException("not yet implemented");
//    }


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
}
