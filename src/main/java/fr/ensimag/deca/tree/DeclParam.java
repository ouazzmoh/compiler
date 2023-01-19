package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclParam extends AbstractDeclParam {
	final private AbstractIdentifier type;
	final private AbstractIdentifier name;
	
	
	public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
		super();
		this.type = type;
		this.name = name;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		this.type.decompile(s);
		s.print(" ");
		this.name.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		name.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
}
