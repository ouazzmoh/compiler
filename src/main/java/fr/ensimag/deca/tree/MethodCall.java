package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class MethodCall extends AbstractExpr  {
	private AbstractExpr exp;
	private AbstractIdentifier ident;
	private ListExpr args;

	public MethodCall(AbstractExpr exp, AbstractIdentifier ident, ListExpr args) {
		super();
		this.exp = exp;
		this.ident = ident;
		this.args = args;
	}
	
	public MethodCall(AbstractIdentifier ident, ListExpr args) {
		super();
		this.exp = null;
		this.ident = ident;
		this.args = args;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		EnvironmentExp env2;
		if(exp!=null) {
			Type t = exp.verifyExpr(compiler, localEnv, currentClass);
			ClassType c = t.asClassType("expression before DOT is not a class Type", getLocation());
			ClassDefinition class2 = c.getDefinition();
			env2 = class2.getMembers();
			
		}
		else {
			if(currentClass == null) {
				throw new ContextualError("method can't be called in main without an object", this.getLocation());
			}
			env2 = localEnv;
		}
		MethodDefinition res = this.methodIdent(env2);
		if (res == null) {
			throw new ContextualError("Object doesn't have this method", this.getLocation());
		}
		ident.setDefinition(res);
		rvalue(compiler, localEnv, currentClass, res.getSignature());
		this.setType(res.getType());
		return this.getType();

	}
	public void rvalue(DecacCompiler compiler, EnvironmentExp env,ClassDefinition currentClass,
			Signature s) throws ContextualError {
		if(args.getList().size() != s.size()) {
			throw new ContextualError("number of param not valid", this.getLocation());
		}
		int i = 0;
		for(AbstractExpr e: this.args.getList()) {
			e.verifyRValue(compiler, env, currentClass, s.paramNumber(i));
			i++;
		}
	}
	
	
	public MethodDefinition methodIdent(EnvironmentExp env) {
		MethodDefinition res = (MethodDefinition) env.get(ident.getName());
		return res;
	}
	

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		if(this.exp != null){
		    this.exp.decompile(s);
		    s.print(".");
		}
		this.ident.decompile(s);
		s.print("(");
		this.args.decompile(s);
		s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		if(exp != null) {
	        exp.prettyPrint(s, prefix, false);
		}
        ident.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		if(exp != null) {
			exp.iter(f);
		}
        ident.iter(f);
		args.iter(f);
	}

}
