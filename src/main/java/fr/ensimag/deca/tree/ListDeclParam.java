package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Not yet implemented");
		int c = 0;
        for (AbstractDeclParam i : getList()) {
            if( c == 0 ){
                if(i != null){
                    i.decompile(s);
                    c = c + 1;
                }
			}
            else{
                if(i != null){
                    s.print(",");
                    i.decompile(s);
                }             
            }         
        }
	}
}
