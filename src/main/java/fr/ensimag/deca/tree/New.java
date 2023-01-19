package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class New extends AbstractExpr {
	private AbstractIdentifier ident;
	
	public New(AbstractIdentifier ident) {
		super();
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = ident.verifyType(compiler);
		ident.setType(t);
		ClassType type = (ClassType) compiler.environmentType.defOfType(ident.getName()).getType();
		if (!type.sameType(t)) {
			throw new ContextualError("instanciation impo", this.getLocation());
		}
		Definition def = compiler.environmentType.defOfType(ident.getName());
		this.setType(type);
		ident.setDefinition(def);
		return t;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		ident.prettyPrint(s, prefix, true);
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		ident.iter(f);
	}

}
