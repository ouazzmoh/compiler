package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.arm.pseudocode.ArmProgram;
import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.arm.pseudocode.ImmediateIntegerArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	
    	AbstractExpr exp = expression.verifyRValue(compiler, localEnv, currentClass, t);
    	exp.verifyExpr(compiler, localEnv, currentClass);
    	this.setExpression(exp);
    	
    	/*
    	Type var = expression.verifyExpr(compiler, localEnv, currentClass);
    	if (!var.sameType(t)) {
    		throw new ContextualError("la variable et la valeur n'ont pas le meme type", this.expression.getLocation());
    	}*/
    	
    }


    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        if(getExpression()!=null){ // cas d'initialisation
            s.print(" = ");
            getExpression().decompile(s);
        }
        // else{ // cas epsilon (pas d'initialisation)
        //     decompile(s);
        // }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        expression.codeGenInit(compiler, adr);
    }
    
    @Override
    protected void codeGenInitArm(DecacCompiler compiler, DAddrArm adr){
    	//ArmProgram.data.(lab,new ImmediateIntegerArm(0));
        expression.codeGenInitArm(compiler, adr);
    }

}








