package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue tree, AbstractExpr rightOperand) {
        super(tree, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	AbstractExpr exp = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t1);
    	exp.verifyExpr(compiler, localEnv, currentClass);
    	this.setRightOperand(exp);
    	this.setType(t1);
    	return t1;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }


//    @Override
//    protected void codeGenInst(DecacCompiler compiler, Label label, GPRegister thisReg){
//        if (getLeftOperand().isIdent()&& thisReg != null){//For variable assignment and field assignment inside class
//            Identifier ident = (Identifier)getLeftOperand();
//            if (ident.getExpDefinition().getOperand() == null){
//                ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(),thisReg));
//            }
//            this.getRightOperand().codeGenAssign(compiler, (Identifier)this.getLeftOperand(), null);
//        }
//        else {
//            //The left operand is then a selection
//            Selection sel = (Selection) getLeftOperand();
//            Identifier fieldIdent = (Identifier) sel.getIdent();
//            if (thisReg != null){
//                //We are inside a class and to get to the field we set the adr to index(thiReg)
//                if (fieldIdent.getExpDefinition().getOperand() == null){
//                    fieldIdent.getExpDefinition().setOperand(new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(), thisReg));
//                }
//                this.getRightOperand().codeGenAssign(compiler, fieldIdent, null);
//            }
//            else {
//                //We are outside the scope of the class and the left part of the selection contains the adress of the object
//                //The left part of the dot in this case is an identifier
//                GPRegister reg = compiler.getFreeReg();
//                Identifier leftDot = (Identifier)sel.getExp(); //TODO: see if the left part can be smthg other than an ident
//                compiler.addInstruction(new LOAD(leftDot.getExpDefinition().getOperand(), reg));
//                compiler.addInstruction(new CMP(null, reg));
//                //todo: add a BEQ to error label
//                compiler.useReg();
//                if (fieldIdent.getExpDefinition().getOperand() == null){
//                    fieldIdent.getExpDefinition().setOperand(new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(), reg));
//                }
//                this.getRightOperand().codeGenAssign(compiler, fieldIdent, null);
//                fieldIdent.getExpDefinition().setOperand(null);//We set it to null because the register storing the adress of the object is forgotten
//                compiler.freeReg();
//            }
//
//        }
//
//    }



    @Override
    protected void codeGenInst(DecacCompiler compiler, Label label){
        //The left operand is either an identifier or a selection
        if (getLeftOperand().isIdent()){
            //The identifier is either a field or a variable: the variable's adress is already set in the declaration
            Identifier leftIdent = (Identifier) getLeftOperand();
            if (leftIdent.getExpDefinition().isField()){
                //The identifier is a field, its address should be null
                GPRegister thisReg = compiler.getFreeReg();
                compiler.useReg();
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
                leftIdent.getExpDefinition().setOperand(new RegisterOffset(leftIdent.getFieldDefinition().getIndex(), thisReg));

                //TODO:Push pop

                this.getRightOperand().codeGenAssign(compiler, leftIdent);
                compiler.freeReg();
                //After assigning the value we free the thisReg and make the address null if it's a field
                leftIdent.getExpDefinition().setOperand(null);
            }
            else {
                //The identifier is a variable or a parameter: The address is already set
                this.getRightOperand().codeGenAssign(compiler, leftIdent);
            }
        }
        else {
            //The left operand is a selection in this case
            Selection sel = (Selection) getLeftOperand();
            //What is in the left of the dot is either This or an identifier
            if (sel.getExp() instanceof Identifier){
                //What is left of the dot is an instance, we should use its address to get the register
                Identifier leftDotIdent = (Identifier)sel.getExp();
                Identifier rightDotIdent = (Identifier)sel.getIdent();

                GPRegister thisReg = compiler.getFreeReg();
                compiler.useReg();

                //todo: FACTORIZE THIS

                compiler.addInstruction(new LOAD(leftDotIdent.getExpDefinition().getOperand(), thisReg));
                rightDotIdent.getExpDefinition().setOperand(new RegisterOffset(rightDotIdent.getFieldDefinition().getIndex(), thisReg));

                this.getRightOperand().codeGenAssign(compiler, rightDotIdent);
                compiler.freeReg();


                ///After assigning the value we free the thisReg and make the address null if it's a field
                rightDotIdent.getExpDefinition().setOperand(null);

            }
            else {
                //What is left of the dot is "this", we are in the scope of a class and "this" is in -2(LB)
                Identifier rightDotIdent = (Identifier) sel.getIdent();
                GPRegister thisReg = compiler.getFreeReg();
                compiler.useReg();

                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
                rightDotIdent.getExpDefinition().setOperand(new RegisterOffset(rightDotIdent.getFieldDefinition().getIndex(), thisReg));

                this.getRightOperand().codeGenAssign(compiler, rightDotIdent);
                compiler.freeReg();


                ///After assigning the value we free the thisReg and make the address null if it's a field
                rightDotIdent.getExpDefinition().setOperand(null);
            }
        }


    }



    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        this.getRightOperand().codeGenPrint(compiler, hex);
    }

}
