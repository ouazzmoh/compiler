package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Not yet implemented");
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
            s.println();
        }
    }

}
