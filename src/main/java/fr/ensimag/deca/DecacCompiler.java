package fr.ensimag.deca;

import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.tree.Print;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.arm.pseudocode.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl24
 * @date 01/01/2023
 */
public class  DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    public boolean isArm = true;

    public static Map<LabelArm, DValArm> data = new HashMap<LabelArm, DValArm>();

    public Set<OperandArm> dataSetArm;

    public void addOperandData(OperandArm op){
        assert(isArm);
        dataSetArm.add(op);
    }

    
    public LabelArm getLabel(int i){
		int c = 0;
		for (LabelArm a: data.keySet()) {
			if (i==c) {
				return a;
			}
		}
		return null;
	}
    
    public void addData(LabelArm lab, DValArm dv) {
    	data.putIfAbsent(lab, dv);
    }
	/**
	public static  LabelArm getLabel(){
		//return data.keySet()[0];
	}
	*/
    
    public Boolean getIsArm() {
    	return true;
    }

    //Holds information about the errors labels and messages
    private HashMap<String, String> errorsMap;

    public HashMap<String, String> getErrorsMap(){
        return errorsMap;
    }


    //The current free register, we start from 2
    private int currRegNum;

    //The maximal number of registers to use
    private int regMax;

    private int currRegNumArm;





    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        //

        this.errorsMap = new HashMap<String, String>();

        this.regMax = 15;
        //
        this.currRegNum = 2;

        if (isArm){
            this.dataSetArm = new HashSet<OperandArm>();
            this.currRegNumArm = 2;
        }


    }

    public DecacCompiler(CompilerOptions compilerOptions, File source, int regMax) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        //

        this.errorsMap = new HashMap<String, String>();

        assert(regMax<= 16 && regMax >= 4);

        this.regMax = regMax - 1;
        //
        this.currRegNum = 2;

        if (isArm){
            this.dataSetArm = new HashSet<OperandArm>();
        }


    }



    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * IMAProgram#add(AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }
    
    /**
     * @see
     * ArmProgram#add(AbstractLineArm)
     */
    public void add(AbstractLineArm line) {
        programArm.add(line);
    }

    /**
     * @see IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }
    
    /**
     * @see ArmProgram#addComment(java.lang.String)
     */
    public void addCommentArm(String comment) {
        programArm.addComment(comment);
    }

    /**
     * @see
     * IMAProgram#addLabel(Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }
    
    /**
     * @see
     * ArmProgram#addLabel(Label)
     */
    public void addLabel(LabelArm label) {
        programArm.addLabel(label);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }
    
    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm)
     */
    public void addInstruction(InstructionArm instruction) {
        programArm.addInstruction(instruction);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addFirst(instruction, comment);
    }
    
    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm,
     * java.lang.String)
     */
    public void addInstruction(InstructionArm instruction, String comment) {
        programArm.addFirst(instruction, comment);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction)
     */
    public void addInstructionFirst(Instruction instruction) {
        program.addFirst(instruction);
    }
    
    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm)
     */
    public void addInstructionFirst(InstructionArm instruction) {
        programArm.addFirst(instruction);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction,
     * java.lang.String)
     */
    public void addInstructionFirst(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm,
     * java.lang.String)
     */
    public void addInstructionFirst(InstructionArm instruction, String comment) {
        programArm.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    /**
     * @see 
     * ArmProgram#display()
     */
    public String displayArmProgram() {
        return programArm.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    
    /**
     * The main program. Every instruction generated will eventually end up here.
     * choosing between the IMA and the ARM achitecture depending on the 
     * boolean isArm
     */

    private final IMAProgram program = new IMAProgram();
    private final ArmProgram programArm = new ArmProgram();
 

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);

    public Symbol createSymbol(String name) {
        //return null;// A FAIRE: remplacer par la ligne en commentaire ci-dessous
        return symbolTable.create(name);
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        if (compilerOptions.getOptionp()) {
        	prog.decompile(out);
        	return false;
        }
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        if (compilerOptions.getOptionv()) {
        	return false;
        }
        assert(prog.checkAllDecorations());
        //

//        prog.decompile(out);
        /** we choose between ima and arm*/
        addComment("start main program");
        if (isArm){
            prog.codeGenProgramArm(this);
        }else {
            prog.codeGenProgram(this);
        }

        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");
        if (!isArm){
            program.display(new PrintStream(fstream));
        }
        else {

            PrintStream s = new PrintStream(fstream);
            programArm.display(s);

            //Display data section
            s.println(".section .data");
            Iterator<OperandArm> it = dataSetArm.iterator();
            while (it.hasNext()) {
                OperandArm op = it.next();
                s.println("\t\t" + op.toString() + ": .word  0");
            }

        }


//        for (LabelArm lab : dataSetArm) {
//            lab.display(s);
//            if (DecacCompiler.data.get(lab)!= null) {
//                DecacCompiler.data.get(lab).display(s);
//            }
//        }


        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    public void addError(String errLab, String errMsg){
        if (!errorsMap.containsKey(errLab)){
                errorsMap.put(errLab, errMsg);
        }
    }


    /**
     * Gives a free register, needs to check if it's possible before
     * @return
     */
    public GPRegister getFreeReg(){
        if (useLoad()){
            return Register.getR(currRegNum);
        }
        else {
            throw new DecacInternalError("out of registers!");
        }
    }

    public GPRegisterArm getFreeRegArm(){
       return RegisterArm.getR(currRegNumArm);
    }


    public void useReg(){
        assert(currRegNum <= regMax);
        currRegNum++;
    }


    public void freeReg(){
        assert(currRegNum >=2);
        currRegNum--;
    }

    public void useRegArm(){
        assert(currRegNumArm <= regMax);
        currRegNumArm++;
    }


    public void freeRegArm(){
        assert(currRegNumArm >=2);
        currRegNumArm--;
    }

    public boolean useLoad(){
        return (currRegNum < regMax) && (currRegNum > 1);
    }

	public static DValArm getLabel(LabelArm lab) {
		// TODO Auto-generated method stub
		for (LabelArm label : data.keySet()) {
			if(label.toString().equals(lab.toString())) {
				return data.get(label);
			}
		}
		return null;
	}





}
