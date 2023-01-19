package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public void codeGenListVtableMethods(DecacCompiler compiler, String className, int stackIndex){
        for(AbstractDeclMethod e : this.getList()){
            e.codeGenVtableMethods(compiler,className, stackIndex);
        }
    }

}
