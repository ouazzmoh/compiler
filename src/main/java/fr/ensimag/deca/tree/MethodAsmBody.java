package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class MethodAsmBody extends AbstractMethodBody {
	private StringLiteral string;
	

	public MethodAsmBody(StringLiteral string) {
		super();
		this.string = string;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		string.prettyPrintChildren(s, prefix);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void verifyBody(DecacCompiler compiler, EnvironmentExp env, EnvironmentExp envExpParam, Symbol className,
			Type returnType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not yet implemented");
	}

}
