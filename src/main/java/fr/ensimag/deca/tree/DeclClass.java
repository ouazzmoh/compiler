package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    	ClassType type = new ClassType(name, this.getLocation(), superClas);
    	ClassDefinition def = new ClassDefinition(type, this.getLocation(), superClas);
    	compiler.environmentType.declareClass(name, def);
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
	public void codeGenVirtualTable(DecacCompiler compiler){
		compiler.addComment("Virtual Table of methodes of "+ this.className.getName().getName()+" class");
		if(superClass.getName().getName() == "Object"){
			compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), Register.R0));
		}
		else{
			compiler.addInstruction(new LEA(new RegisterOffset(superClass.getClassDefinition().getAddrTableMathodes(), Register.GB), Register.R0));
		}
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
		this.className.getClassDefinition().setAddrTableMethodes(compiler.getOffset());
		compiler.incOffset(1);
		Label objectLabel = new Label("code.Object.equals");
		LabelOperand oLabelOperand = new LabelOperand(objectLabel);
		compiler.addInstruction(new LOAD(oLabelOperand, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
		compiler.incOffset(1+declmethods.size());
		declmethods.codeGenVirtualTable(compiler,className.getName().getName(),superClass.getClassDefinition().getAddrTableMathodes());
	}

}
