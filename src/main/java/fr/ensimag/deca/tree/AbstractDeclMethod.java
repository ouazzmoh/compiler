package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod extends Tree {


    protected abstract void codeGenVtableMethods(DecacCompiler compiler, String className, int stackIndex);
	protected abstract AbstractIdentifier getMethodeName();
    

	protected abstract EnvironmentExp verifyDeclMethod(DecacCompiler compiler, Symbol className,
			Symbol superClass, int j) throws ContextualError;
	
	protected abstract void verifyMethod(DecacCompiler compiler, Symbol className,
			EnvironmentExp env) throws ContextualError;
	protected abstract void codeGenMethods(DecacCompiler compiler, String className);

}

