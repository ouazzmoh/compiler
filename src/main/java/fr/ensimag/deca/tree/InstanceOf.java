package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class InstanceOf extends AbstractExpr {
	private AbstractIdentifier type;
	private AbstractExpr expr;

	public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
		super();
		this.type = type;
		this.expr = expr;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = expr.verifyExpr(compiler, localEnv, currentClass);
		Type t2 = type.verifyType(compiler);
		if (t.isClassOrNull() && t2.isClass()) {
			return compiler.environmentType.BOOLEAN;
		}
		throw new ContextualError("can't perform an instance of", this.getLocation());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		expr.prettyPrint(s, prefix, false);
		type.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        expr.iter(f);
        type.iter(f);
	}

}
