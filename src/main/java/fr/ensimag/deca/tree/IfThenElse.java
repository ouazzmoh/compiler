package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

//    @Override
//    protected void codeGenInst(DecacCompiler compiler) {
//        Label endIf = new Label("endIf.l" + getLocation().getLine() +
//                ".c" + getLocation().getPositionInLine());
//        this.codeGenInstIfRec(compiler, endIf);
//        compiler.addLabel(endIf);
//    }
//
//
//    @Override
//    protected void codeGenInstIfRec(DecacCompiler compiler, Label endIf) {
//        Label elseIf = new Label("elseIf.l" + elseBranch.uniqueNum());
//        condition.codeGenBeq(compiler, elseIf, 0);
//        for (AbstractInst i : thenBranch.getList()){
//            i.codeGenInstIfRec(compiler, endIf);
//        }
//        compiler.addInstruction(new BRA(endIf));
//        compiler.addLabel(elseIf);
//        for (AbstractInst i : elseBranch.getList()){
//            i.codeGenInstIfRec(compiler, endIf);
//        }
//    }



    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label endIf = new Label("endIf.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        this.codeGenInstIfRec(compiler, endIf);
        compiler.addLabel(endIf);
    }


    @Override
    protected void codeGenInstIfRec(DecacCompiler compiler, Label endIf) {
        Label elseIf = new Label("elseIf.l" + elseBranch.uniqueNum());
        condition.codeGenBeq(compiler, elseIf, 0);
        for (AbstractInst i : thenBranch.getList()){
            i.codeGenInst(compiler);
        }
        compiler.addInstruction(new BRA(endIf));
        compiler.addLabel(elseIf);
        for (AbstractInst i : elseBranch.getList()){
            i.codeGenInst(compiler);
        }
    }

    @Override
    protected void codeGenInstIf(DecacCompiler compiler, Label endIf){
        this.codeGenInstIfRec(compiler, endIf);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
