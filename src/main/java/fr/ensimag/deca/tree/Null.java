package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Null extends AbstractExpr {

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
    	this.setType(compiler.environmentType.NULL);
    	return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null ");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
		
	}

}
