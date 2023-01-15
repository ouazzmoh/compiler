package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterDescriptor;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassType;
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
import fr.ensimag.deca.codegen.RegisterDescriptor;
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

//    @Override
//    public void codeGenInstWhile(DecacCompiler compiler,Label endWhile){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg(); //returns a free register
//        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, this.getExpDefinition().getOperand());
//        compiler.addInstruction(new CMP(new ImmediateInteger(0) , registerToUse));
//        compiler.addInstruction(new BEQ(endWhile));
//        //TODO: Free the register after use
//    }

//    @Override
//    protected DVal codeGenSum(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, this.getExpDefinition().getOperand());
//        return registerToUse;
//    }
//
//    @Override
//    protected DVal codeGenSub(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, this.getExpDefinition().getOperand());
//        return registerToUse;
//    }
//
//    @Override
//    protected DVal codeGenMul(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, this.getExpDefinition().getOperand());
//        return registerToUse;
//    }
//
//    //TODO: Factoriser le code plus
//    @Override
//    protected DVal codeGenDiv(DecacCompiler compiler){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), registerToUse));
//        compiler.getRegisterDescriptor().useRegister(registerToUse, this.getExpDefinition().getOperand());
//        return registerToUse;
//    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        DVal toLoad = getExpDefinition().getOperand();
        compiler.addInstruction(new LOAD(toLoad, registerToUse));
        compiler.getRegisterDescriptor().useRegister(registerToUse, toLoad);
        return registerToUse;
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(getVariableDefinition().getOperand(), registerToUse)); //No need to use and free
        compiler.addInstruction(new PUSH(registerToUse));
    }

//    @Override
//    protected void codeGenIf(DecacCompiler compiler, Label label){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), registerToUse));
//        compiler.addInstruction(new CMP(0, registerToUse));
//        compiler.addInstruction(new BEQ(label));
//
//    }

    //TODO: Turn getExpDefinition to getVarDefinition

//    @Override
//    protected void codeGenBeq(DecacCompiler compiler, Label label, int p){
//        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
//        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), registerToUse));
//        compiler.addInstruction(new CMP(p, registerToUse));
//        compiler.addInstruction(new BEQ(label));
//    }

    @Override
    protected void codeGenBeq(DecacCompiler compiler, Label label,Label end, int p){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), registerToUse));
        compiler.addInstruction(new CMP(p, registerToUse));
        compiler.addInstruction(new BEQ(label));
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        if (compiler.getRegisterDescriptor().useLoad()) {
            GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
            compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), registerToUse));
            compiler.addInstruction(new STORE(registerToUse, adr));
        }
        else {
            codeGenPush(compiler);
        }
    }

}
