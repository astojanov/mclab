package natlab.backends.Fortran.codegen.caseHandler;

import natlab.backends.Fortran.codegen.*;
import natlab.tame.tir.*;

public class HandleCaseTIRWhileStmt {

	static boolean Debug = false;
	
	public HandleCaseTIRWhileStmt(){
		
	}
	
	public FortranCodeGenerator getFortran(FortranCodeGenerator fcg, TIRWhileStmt node){
		if (Debug) System.out.println("in while statement.");
		if (Debug) System.out.println(node.getCondition().getVarName());
		fcg.buf.append("do while ("+node.getCondition().getVarName()+")\n");
		fcg.indentFW = true;
		fcg.printStatements(node.getStatements());
		fcg.indentFW = false;
		fcg.buf.append("enddo");
		return fcg;
	}
}
