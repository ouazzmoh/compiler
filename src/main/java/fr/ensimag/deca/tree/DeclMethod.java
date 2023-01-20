package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
	protected void codeGenVtableMethods(DecacCompiler compiler, String className, int stackIndex) {
		Label methodLabel = new Label("code." + className + "." + name.getName().getName());
		LabelOperand opMethodLabel = new LabelOperand(methodLabel);
		compiler.addInstruction(new LOAD(opMethodLabel, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(stackIndex + name.getMethodDefinition().getIndex() + 1, Register.GB)));
	}

	@Override
	protected void codeGenMethods(DecacCompiler compiler, String className){
		int oldRegNum = compiler.getCurrRegNum();
		compiler.addInstruction(new TSTO(oldRegNum));
		compiler.addInstruction(new BOV(new Label("err_stack_overflow")));
		compiler.addComment("Sauvegarde des registres");
		for(int i=2; i< oldRegNum ; i++){
			compiler.addInstruction(new PUSH(Register.getR(i)));
		}
		compiler.setCurrRegNum(2);
		Label startMethode = new Label("code."+className+"."+name.getName().getName());
		compiler.addLabel(startMethode);
		body.codeGenBodyMethod(compiler, parametres);
		Label endMethode = new Label("fin."+className+"."+name.getName().getName());
		compiler.addLabel(endMethode);
		compiler.addComment("Restauration des registres");
		for(int i=2;i < oldRegNum; i++){
			compiler.addInstruction(new POP(Register.getR(i)));
		}
		compiler.setCurrRegNum(oldRegNum);
		compiler.addInstruction(new RTS());

	}

	@Override
	protected AbstractIdentifier getMethodeName() {
		return name;
	}


}
