package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Selection extends AbstractLValue {
	private AbstractExpr exp;
	private AbstractIdentifier ident;
	
	

	public Selection(AbstractExpr exp, AbstractIdentifier ident) {
		super();
		this.exp = exp;
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = exp.verifyExpr(compiler, localEnv, currentClass);
		ClassType c = t.asClassType("expression before DOT is not a class Type", getLocation());
		ClassDefinition class2 = c.getDefinition();
		EnvironmentExp env2 = class2.getMembers();
		
		ExpDefinition t2 = env2.get(ident.getName());
    	
		if(t2 == null) {
			throw new ContextualError("Selection type problem", this.getLocation());
		}
		if (t2.isField()) {
			FieldDefinition res = t2.asFieldDefinition(null, getLocation());
			ident.setDefinition(res);
	    	ident.setType(res.getType());
			if(res.getVisibility() == Visibility.PUBLIC) {

				return t2.getType();
			}
			else {
				if (currentClass == null) {
					throw new ContextualError("Access to protected field", this.getLocation());
				}
				if(compiler.environmentType.subType(t, currentClass.getType())){
					if(compiler.environmentType.subType(currentClass.getType(), res.getContainingClass().getType())) {
						return t2.getType();
					}
				}
			}

		}
		throw new ContextualError("Selection type problem", this.getLocation());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}
    @Override
    String prettyPrintNode() {
        return "Selection";
    }

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
        exp.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		exp.iter(f);
        ident.iter(f);
		
	}

}
