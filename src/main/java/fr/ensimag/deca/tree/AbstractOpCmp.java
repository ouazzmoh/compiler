package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	// il faut ajouter les autres operations de meme domaine
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=") | this.getOperatorName().equals("<=")
    			| this.getOperatorName().equals(">=") | this.getOperatorName().equals(">")
    			| this.getOperatorName().equals("<")) {
    			if (t1.sameType(t2) && (t1.isInt() | t1.isFloat())) {
    				this.setType(compiler.environmentType.BOOLEAN);
    				return this.getType();
    			}
    			if (t1.isInt() && t2.isFloat()) {
    				this.setType(compiler.environmentType.BOOLEAN);
    				return this.getType();
    				}
    			if (t1.isFloat() && t2.isInt()) {
    				this.setType(compiler.environmentType.BOOLEAN);
    				return this.getType();    			}
    		}
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=")){
    		if (t1.sameType(t2) && (t1.isBoolean())) {
				this.setType(compiler.environmentType.BOOLEAN);
				return this.getType();    			
    		}
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands not permetted", this.getLocation());
    	}
    	
    }
