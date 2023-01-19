package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class ListeDeclField extends TreeList<AbstractDeclField> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Not yet implemented");
		for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
	}
	/*
	 * 
	 */
	
    void verifyListDeclField(DecacCompiler compiler, Symbol className,
            Symbol superClass, EnvironmentExp env) throws ContextualError {
    	int j = 0;
        for (AbstractDeclField i : getList()) {
        	EnvironmentExp envr = i.verifyDeclField(compiler, className, superClass, j);
        	j++;
        	envr.empilement(env);
        }
    }
    
    void verifyListFields(DecacCompiler compiler, EnvironmentExp env, Symbol ClassName) throws ContextualError {
        for (AbstractDeclField i : getList()) {
        	i.verifyField(compiler, ClassName, env);
        }
    }

}
