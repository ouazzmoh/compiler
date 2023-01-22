package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class This extends AbstractExpr {



	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		if (currentClass == null) {
			throw new ContextualError("impossible d'appeler this dans main", this.getLocation());
		}
		this.setType(currentClass.getType());
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do

	}
	
    @Override
    String prettyPrintNode() {
        return "this ";
    }


	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex, Identifier ident){
		//the ident is a field inside the scope of a class
		GPRegister reg = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new LOAD(new RegisterOffset(-2 , Register.LB), reg));
		//ident is null
		ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
		ident.codeGenPrint(compiler, printHex);
		ident.getExpDefinition().setOperand(null);
		compiler.freeReg();
	}


	@Override
	protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg, Identifier ident){
		return ident.setAdrField(compiler, refReg);
	}


}
