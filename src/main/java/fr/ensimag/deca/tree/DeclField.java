package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclField extends AbstractDeclField {
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    final private Visibility v;

    public DeclField(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization, Visibility v) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
		this.v = v;
    }
    

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
	}
	
	@Override
	String prettyPrintNode() {
		return "[visibility="+ v + "] " + this.getClass().getSimpleName();
	}

	@Override
	protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}
	
	
}
