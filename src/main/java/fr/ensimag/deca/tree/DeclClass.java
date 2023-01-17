package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {
	
	final private AbstractIdentifier className;
	final private AbstractIdentifier superClass;
	final private ListeDeclField declfields;
	final private ListDeclMethod declmethods;
	
	public DeclClass(AbstractIdentifier className,AbstractIdentifier superClass,ListeDeclField declfields, ListDeclMethod declmethods) {
		this.className = className;
		this.superClass = superClass;
		this.declfields = declfields;
		this.declmethods = declmethods;
	}


    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("Not yet supported");
    	declfields.prettyPrint(s, prefix, false);
    	declmethods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

	@Override
	public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet supported");
		
	}

}
