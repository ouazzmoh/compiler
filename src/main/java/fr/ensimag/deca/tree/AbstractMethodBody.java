package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public abstract class AbstractMethodBody extends Tree {

	protected abstract void verifyBody(DecacCompiler compiler, EnvironmentExp env, EnvironmentExp envExpParam,
			Symbol className, Type returnType) throws ContextualError;

}
