package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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

    protected String readErrInt = "err_read_input_int";
    protected String readErrFloat = "err_read_input_float";


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        compiler.addInstruction(new STORE(Register.R1, adr));
    }

    @Override
    protected void codeGenPush(DecacCompiler compiler){
        compiler.addInstruction(new PUSH(Register.R1));
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister registerToUse = compiler.getRegisterDescriptor().getFreeReg();
        compiler.addInstruction(new LOAD(Register.R1, registerToUse));
        compiler.getRegisterDescriptor().useRegister(registerToUse, Register.R1);
        return registerToUse;
    }

}
