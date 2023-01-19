package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SETROUND_UPWARD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    	ClassDefinition def = new ClassDefinition(type, this.getLocation(), superClas);
    	compiler.environmentType.declareClass(name, def);
    	className.setDefinition(def);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	EnvironmentExp env = new EnvironmentExp(null);
    	Symbol supe = null;
    	//if (superClass != null) {
    	supe = superClass.getName();
    	ClassDefinition def  = (ClassDefinition) compiler.environmentType.defOfType(supe);
    	if (def == null) {
    		throw new ContextualError("problems with verifyClassMembers", this.getLocation());
    	}
    	//}
    	EnvironmentExp env_exp_super = def.getMembers();
    	declfields.verifyListDeclField(compiler, className.getName(), supe , env);
    	
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol s = className.getName();
    	ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(s);
    	EnvironmentExp env = def.getMembers();
    	declfields.verifyListFields(compiler, env, s);
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
        throw new UnsupportedOperationException("Not yet supported");
		
	}

	@Override
	protected void codeGenVirtualTable(DecacCompiler compiler){
		compiler.addComment("Virtual Table of methodes of "+ this.className.getName().getName()+" class");
		if(superClass.getName().getName().equals("object")){
			superClass.getClassDefinition().setStackIndex(1);
			//TODO: SET THIS IN PARSING
		}
		//Store @superClass, stackIndex of new class
		//Store inherited methods in, stakIndex + methodIndex for new class
		compiler.addInstruction(new LEA(new RegisterOffset(superClass.getClassDefinition().getStackIndex(), Register.GB), Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
		this.className.getClassDefinition().setStackIndex(compiler.getOffset());
		compiler.incOffset(1);
		Label objectLabel = new Label("code.Object.equals");
		LabelOperand oLabelOperand = new LabelOperand(objectLabel);
		compiler.addInstruction(new LOAD(oLabelOperand, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
		compiler.incOffset(1+declmethods.size());
		declmethods.codeGenListVtableMethods(compiler,className.getName().getName(),superClass.getClassDefinition().getStackIndex());
	}

	@Override
	protected void codeGenFieldsMethods(DecacCompiler compiler){
		//TODO: Generate the code of initialization for fields and methods of class
		compiler.addComment("--------------------------------------------------");
		compiler.addComment("                 Classe " + className.getName().getName());
		compiler.addComment("--------------------------------------------------");
		//Code for fields.initializations
		compiler.addLabel(new Label("init."+ className.getName().getName()));
		//TODO : TSTO
		//TODO: Avec superclass, tous les nv champs, initialiser les champs heritees, init explicit des nv champs
		for (AbstractDeclField d : declfields.getList()){
			d.codeGenDeclField(compiler);
		}


	}


}
