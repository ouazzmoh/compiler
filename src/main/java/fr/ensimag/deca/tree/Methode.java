package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class Methode extends AbstractExpr {
	private AbstractIdentifier ident;
	private ListExpr arguments;


	public Methode(AbstractIdentifier iden, ListExpr arguments) {
		
		this.ident = iden;
		this.arguments = arguments;
	}
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
    	ExpDefinition def = localEnv.get(ident.getName());
    	if (def == null) {
    		throw new ContextualError("Usage des méthodes pas encore traité", this.getLocation());
    	}
		return null;
	}


	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}


}
