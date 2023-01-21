package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue tree, AbstractExpr rightOperand) {
        super(tree, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	AbstractExpr exp = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t1);
    	exp.verifyExpr(compiler, localEnv, currentClass);
    	this.setRightOperand(exp);
    	this.setType(t1);
    	return t1;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, Label label, GPRegister thisReg){
        //Load right_operand(expr), left_operand(lvalue)
        //TODO: Left operand is a selection, shoould set the operand
        if (getLeftOperand().isIdent()){//For variable assignment and field assignment inside class
            AbstractIdentifier ident = (Identifier)getLeftOperand();
            if (ident.getExpDefinition().getOperand() == null){
                ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(),thisReg));
            }
        }
        this.getRightOperand().codeGenAssign(compiler, this.getLeftOperand());
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex, GPRegister thisReg){
        this.getRightOperand().codeGenPrint(compiler, hex, thisReg);
    }

}
