package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {
	
	final private AbstractIdentifier className;
	private AbstractIdentifier superClass;
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
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol name = className.getName();
    	Symbol superName;
    	ClassDefinition superClas = null;
    	if (superClass != null) {
        	superName = superClass.getName();
        	superClas = (ClassDefinition) compiler.environmentType.defOfType(superName);
        	if (superClas == null) {
        		throw new ContextualError("superClass not defined", this.getLocation());
        	}
    	}
    	else {
    		superClass = new Identifier(compiler.createSymbol("object"));
    		superClas = (ClassDefinition) compiler.environmentType.defOfType(compiler.createSymbol("object"));
    	}
		superClass.setDefinition(superClas);
		superClass.setLocation(superClas.getLocation());
    	ClassType type = new ClassType(name, this.getLocation(), superClas);
    	//ClassDefinition def = new ClassDefinition(type, this.getLocation(), superClas);
    	ClassDefinition def = type.getDefinition();
    	compiler.environmentType.declareClass(name, def);
    	className.setDefinition(def);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol supe = null;
    	//if (superClass != null) {
    	supe = superClass.getName();
    	ClassDefinition defCourante  = (ClassDefinition) compiler.environmentType.defOfType(className.getName());
    	EnvironmentExp env = defCourante.getMembers();
    	ClassDefinition def  = (ClassDefinition) compiler.environmentType.defOfType(supe);
    	if (def == null) {
    		throw new ContextualError("problems with verifyClassMembers", this.getLocation());
    	}
    	//}
    	EnvironmentExp ee = new EnvironmentExp(null);
    	EnvironmentExp env_exp_super = def.getMembers();
    	try {
			declfields.verifyListDeclField(compiler, className.getName(), supe , ee);
			declmethods.verifyListDeclMethod(compiler, className.getName(), supe, ee);
			ee.Empilement(env);
			EnvironmentType neww = new EnvironmentType(compiler);
			neww.declareClass(className.getName(), defCourante);
			compiler.environmentType.Empilement(neww);
		} catch (DecacFatalError e) {
			// TODO Auto-generated catch block
			throw new ContextualError("environment are not disjoint", this.getLocation());
		}
    	
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol s = className.getName();
    	ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(s);
    	EnvironmentExp env = def.getMembers();
    	declfields.verifyListFields(compiler, env, s);
    	declmethods.verifyListMethod(compiler, env, s);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("Not yet supported");
    	className.prettyPrint(s, prefix, false);
    	if (superClass != null) {
    		superClass.prettyPrint(s, prefix, false);
    	}
    	declfields.prettyPrint(s, prefix, false);
    	declmethods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	className.iter(f);
    	if (superClass != null) {
        	superClass.iter(f);
    	}
    	declfields.iter(f);
    	declmethods.iter(f); 
        }

	@Override
	public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet supported");
		s.print("class ");
		this.className.decompile(s);
		s.print(" extends ");
		if(this.superClass == null){
			s.print("Object");
		}
		else{
			this.superClass.decompile(s);
		}
		s.println("{");
		s.indent();
		this.declfields.decompile(s);
		this.declmethods.decompile(s);
		s.unindent();
		s.print("}");
		
	}

}
