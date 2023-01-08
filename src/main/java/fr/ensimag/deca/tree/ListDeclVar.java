package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclVar i : getList()) {
            i.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDeclVariable(DecacCompiler compiler){
        int d1 = getList().size();
        //TSTO #d1
        compiler.addInstruction(new ADDSP(d1));
        for (AbstractDeclVar d : getList()){
            d.codeGenDeclVariable(compiler);
        }


    }
}
