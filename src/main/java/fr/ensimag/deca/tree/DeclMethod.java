package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class DeclMethod extends AbstractDeclMethod {
	final private AbstractIdentifier type;
	final private AbstractIdentifier name;
	final private ListDeclParam parametres;
	final private AbstractMethodBody body;
	

	public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam parametres, AbstractMethodBody body) {
		super();
		this.type = type;
		this.name = name;
		this.parametres = parametres;
		this.body = body;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		name.prettyPrint(s, prefix, false);
		parametres.prettyPrint(s, prefix, false);
		body.prettyPrint(s,prefix,true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        type.iter(f);
        name.iter(f);
        parametres.iter(f);
        body.iter(f);
	}

	@Override
	protected void codeGenVtableMethods(DecacCompiler compiler, String className, int stackIndex){
		Label methodLabel = new Label("code."+className+"."+name.getName().getName());
		LabelOperand opMethodLabel = new LabelOperand(methodLabel);
		compiler.addInstruction(new LOAD(opMethodLabel, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(stackIndex+name.getMethodDefinition().getIndex(), Register.GB)));
	}

}
