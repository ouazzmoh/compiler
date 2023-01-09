package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.*;

import java.util.*;

/**
 * Module to hold information about the register
 *
 * We need to update the information in this module at each use
 *
 * @author ouazzmoh
 * @date 09/01/2023
 */
public class RegisterDescriptor {

    private Map<GPRegister, Operand> usedRegisterMap = new HashMap<GPRegister, Operand>();
    private LinkedList<GPRegister> freeRegisterList = new LinkedList<GPRegister>();

    /**
     * Constructor to initialize the register descriptor
     * Puts all non-scratch(different from R0 and R1) registers in the free registers list
     */
    public RegisterDescriptor() {
        for (int i = 2; i<=15; i++){
            freeRegisterList.add(Register.getR(i));
        }
    }

    public void setUsedRegisterMap(Map<GPRegister, Operand> usedRegisterMap) {
        this.usedRegisterMap = usedRegisterMap;
    }


    public Map<GPRegister, Operand> getUsedRegisterMap() {
        return usedRegisterMap;
    }


    /**
     * updates the register descriptor (free)
     *i.e: removes the register from the map of used register
     * and adds it to the free registers
     * @param register
     */
    public void freeRegister(GPRegister register){
        usedRegisterMap.remove(register);
        freeRegisterList.addFirst(register); //Adds the register to the first position
    }


    /**
     * updates the register descriptor (uses)
     * i.e: adds the register to the map of used register with its value
     * and removes it from the free registers set
     * @param register
     * @param val
     */
    public void useRegister(GPRegister register, Operand val){
        freeRegisterList.remove(register);
        usedRegisterMap.put(register, val);
    }


    public GPRegister getFreeReg(){
        //TODO: Catch the case where the list is empty
        return freeRegisterList.get(0);
    }



}
