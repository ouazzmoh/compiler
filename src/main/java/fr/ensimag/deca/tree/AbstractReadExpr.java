package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * read...() statement.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        compiler.addInstruction(new STORE(Register.R1, adr));
    }

    @Override
    protected void codeGenAssign(DecacCompiler compiler, Identifier identifier){
        compiler.addInstruction(new STORE(Register.R1, identifier.getExpDefinition().getOperand()));
    }
}
