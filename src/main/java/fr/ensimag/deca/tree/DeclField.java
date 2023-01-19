package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

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
        s.print(v + "");
        s.print(" ");
		this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
	}


	@Override
	protected EnvironmentExp verifyDeclField(DecacCompiler compiler, Symbol className, Symbol superClass, int j) throws ContextualError {
		Type t = type.verifyType(compiler);
		type.setType(t);
    	this.type.setDefinition(compiler.environmentType.defOfType(type.getName()));
		Symbol name = varName.getName();
		if (t.sameType(compiler.environmentType.VOID)) {
			throw new ContextualError("void type can't be a field", type.getLocation());
		}
		ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(superClass);
		EnvironmentExp envi = new EnvironmentExp(null);
		FieldDefinition defField = new FieldDefinition(t,this.getLocation(), v, (ClassDefinition) compiler.environmentType.defOfType(className), j);
		try {
			envi.declare(name, defField);
			def.getMembers().declare(name, defField);
    		varName.setDefinition(defField);
    		varName.setType(t);
		} catch (DoubleDefException e1) {
			throw new ContextualError("Something went wrong in fields", this.getLocation());
		}
		return envi;
	}


	@Override
	protected void verifyField(DecacCompiler compiler, Symbol className, EnvironmentExp env) throws ContextualError {
		Type t = this.type.getType();
		initialization.verifyInitialization(compiler, t, env, (ClassDefinition) compiler.environmentType.defOfType(className));
		
	}
	
	
}

