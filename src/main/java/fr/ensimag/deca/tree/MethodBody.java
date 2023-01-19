package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBody extends AbstractMethodBody {
	
    private ListDeclVar declVariables;
    private ListInst insts; 
    

	public MethodBody(ListDeclVar declVariables, ListInst insts) {
		super();
		this.declVariables = declVariables;
		this.insts = insts;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.println("{");
		s.indent();
		this.declVariables.decompile(s);
		this.insts.decompile(s);
		s.unindent();
		s.print("}");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        declVariables.iter(f);
        insts.iter(f);		
	}

}
