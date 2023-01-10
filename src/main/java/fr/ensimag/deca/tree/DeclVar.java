package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	//throw new UnsupportedOperationException("verifyDeclVar not yet implemented");
    	if (type.getName().equals("void")) {
    		throw new ContextualError("type = void impossible", this.getLocation());
    	}
    	else if (type == null) {
    		throw new ContextualError("le type de la variable n'est pas déclaré", this.getLocation());    		
    	}
    	Type t1 = this.type.verifyType(compiler);
    	this.type.setDefinition(compiler.environmentType.defOfType(type.getName()));

    	Symbol name = varName.getName();
    	
    	initialization.verifyInitialization(compiler, t1, localEnv, currentClass);    
    	try {
    		ExpDefinition def =  new VariableDefinition(t1, varName.getLocation());
    		localEnv.declare(name,def);
    		varName.setDefinition(def);
    		varName.setType(t1);
    	} catch (DoubleDefException e) {
    		throw new ContextualError("double declaration de la variable", this.getLocation());
    	}
    	
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenDeclVariable(DecacCompiler compiler, int varOffset){
            //TODO: If there is an initialization, initialize


        //Setting the adress
        //TODO: put it in the correpondant memory adresse(LIFO)
        varName.getExpDefinition().setOperand(new RegisterOffset(varOffset, Register.GB));
        initialization.codeGenInit(compiler, varName.getExpDefinition().getOperand());
        //TODO: get a free register
//        compiler.addInstruction(new STORE(Register.getR(2), varName.getExpDefinition().getOperand()));
//        compiler.getRegisterDescriptor().freeRegister();
    }
}
