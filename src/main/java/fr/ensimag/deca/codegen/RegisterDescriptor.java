package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Module to hold information about the register
 *
 * @author ouazzmoh
 * @date 09/01/2023
 */
public class RegisterDescriptor {

    private Map<GPRegister, Operand> registerMap = new HashMap<GPRegister, Operand>();

    /**
     * Constructor initializes the register at a NullOperand
     */
    public RegisterDescriptor() {
        for (int i = 0; i<=15; i++){
            registerMap.put(Register.getR(i), new NullOperand());
        }
    }

    public Map<GPRegister, Operand> getRegisterMap() {
        return registerMap;
    }

    public void setRegisterMap(Map<GPRegister, Operand> registerMap) {
        this.registerMap = registerMap;
    }

    //structure where to store used registers:
    //  stack?
    //getFreeRegister
    //

}
