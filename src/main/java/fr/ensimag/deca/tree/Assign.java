package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

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

    public Assign(AbstractExpr tree, AbstractExpr rightOperand) {
        super(tree, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	AbstractExpr exp = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t1);
    	this.setType(t1);
    	return t1;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
