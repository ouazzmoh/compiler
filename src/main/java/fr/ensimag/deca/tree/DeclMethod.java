package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class DeclMethod extends AbstractDeclMethod {
	final private AbstractIdentifier type;
	final private AbstractIdentifier name;

	public AbstractIdentifier getName() {
		return name;
	}

	final private ListDeclParam parametres;
	final private AbstractMethodBody body;
	

	public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam parametres, AbstractMethodBody body) {
		super();
		this.type = type;
		this.name = name;
		this.parametres = parametres;
		this.body = body;
	}

	
	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		name.prettyPrint(s, prefix, false);
		parametres.prettyPrint(s, prefix, false);
		body.prettyPrint(s,prefix,true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        type.iter(f);
        name.iter(f);
        parametres.iter(f);
        body.iter(f);
	}




	protected EnvironmentExp verifyDeclMethod(DecacCompiler compiler,Symbol className, Symbol superClass, int j) throws ContextualError {
		// TODO Auto-generated method stub
		int k = ((ClassDefinition )compiler.environmentType.defOfType(className)).getNumberOfMethods() + 1;
		Type t = type.verifyType(compiler);
		type.setType(t);
    	this.type.setDefinition(compiler.environmentType.defOfType(type.getName()));
		Symbol name = this.name.getName();
		Signature s = parametres.verifyListDeclParam(compiler);
		ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(superClass);
		EnvironmentExp env_exp_super = def.getMembers();
		MethodDefinition defname = (MethodDefinition) env_exp_super.get(name);
		if (defname != null) {
			Signature sig2 = defname.getSignature();
			Type t2 = defname.getType();
			if (!s.sameSignature(sig2)) {
				throw new ContextualError("this method's signature doesn't match the one in superclass", this.getLocation());
			}
			if(!compiler.environmentType.subType(t, t2)) {
				throw new ContextualError(t + " is not a subtype of " + t2, this.getLocation());
			}
			k = defname.getIndex();
		}
		else {
			((ClassDefinition )compiler.environmentType.defOfType(className)).setNumberOfMethods(k);;
		}
		
		MethodDefinition resDef = new MethodDefinition(t, getLocation(), s, k);
		EnvironmentExp res = new EnvironmentExp(null);
		this.name.setDefinition(resDef);
		try {
			res.declare(name, resDef);
		} catch (DoubleDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected void verifyMethod(DecacCompiler compiler, Symbol className,
			EnvironmentExp env) throws ContextualError {
		// TODO Auto-generated method stub
		Type returnType = this.type.verifyType(compiler);
		try {
			EnvironmentExp envExpParam = parametres.verifyParam(compiler, env);
			body.verifyBody(compiler,env,envExpParam,className, returnType);
		} catch (DecacFatalError e) {
			// TODO Auto-generated catch block
			throw new ContextualError("body params are invalids", this.getLocation());
		}

	}



	@Override
	protected void  codeGenDeclMethod(DecacCompiler compiler, String className){
		int oldRegNum = compiler.getCurrRegNum();
		Label startMethod = new Label("code."+className+"."+name.getName().getName());
		compiler.addLabel(startMethod);
		int line = compiler.getIndexLineProgram();
		compiler.setCurrRegNum(2);
		compiler.resetMaxRegisterUsed();
		//We load the object in a register
		GPRegister thisReg = compiler.getFreeReg();
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
		compiler.useReg();
		compiler.updateMaxRegisterUsed();
		body.codeGenBodyMethod(compiler, parametres, thisReg);
		compiler.setCurrRegNum(oldRegNum);
		compiler.addHere(new TSTO(compiler.getMaxRegisterUsed()-1),line);
		line+=1;
		compiler.addHere(new BOV(new Label("err_stack_overflow")),line);
		line+=1;
		compiler.addCommentHere("Sauvegarde des registres",line);
		line+=1;
		for(int i=2; i<= compiler.getMaxRegisterUsed(); i++){
			compiler.addHere(new PUSH(Register.getR(i)),line);
			line+=1;
		}
		Label endMethod = new Label("fin."+className+"."+name.getName().getName());
		compiler.addLabel(endMethod);
		compiler.addComment("Restoring Registers");
		for(int i=2; i <= compiler.getMaxRegisterUsed(); i++){
			compiler.addInstruction(new POP(Register.getR(i)));
		}
		compiler.addInstruction(new RTS());

	}

	@Override
	protected AbstractIdentifier getMethodeName() {
		return name;
	}


}
