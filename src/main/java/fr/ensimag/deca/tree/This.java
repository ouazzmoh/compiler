package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class This extends AbstractExpr {
	

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		if (currentClass == null) {
			throw new ContextualError("impossible d'appeler this dans main", this.getLocation());
		}
		this.setType(currentClass.getType());
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do

	}
	
    @Override
    String prettyPrintNode() {
        return "this ";
    }

}
