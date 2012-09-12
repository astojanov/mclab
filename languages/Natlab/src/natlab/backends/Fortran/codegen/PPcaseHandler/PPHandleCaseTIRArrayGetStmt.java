package natlab.backends.Fortran.codegen.PPcaseHandler;

import java.util.ArrayList;

import natlab.backends.Fortran.codegen.*;
import natlab.tame.tir.*;

public class PPHandleCaseTIRArrayGetStmt {

	static boolean Debug = false;
	
	public PPHandleCaseTIRArrayGetStmt(){
		
	}
	
	public FortranCodePrettyPrinter getFortran(FortranCodePrettyPrinter fcg, TIRArrayGetStmt node){
		if (Debug) System.out.println("in an arrayget statement!");
		String indexList = node.getIndizes().toString();
		String[] tokens = indexList.replace("[", "").replace("]", "").split("[,]");
		ArrayList<String> tokensAsArray = new ArrayList<String>();
		for(String indexName : tokens){
			tokensAsArray.add(indexName);
		}
		if(tokensAsArray.contains(":")){
			fcg.buf.append(node.getLHS().getNodeString().replace("[", "").replace("]", "")+
					"("+node.getIndizes().toString().replace("[", "").replace("]", "")+")"+
					" = "+node.getArrayName().getVarName()+"("+node.getIndizes().toString().replace("[", "").replace("]", "")+")");
		}
		else{
			fcg.buf.append(node.getLHS().getNodeString().replace("[", "").replace("]", "")+
					" = "+node.getArrayName().getVarName()+"("+node.getIndizes().toString().replace("[", "").replace("]", "")+")");
		}
		for(String indexName : tokens){
			if(indexName.equals(":")){
				//ignore this
			}
			else{
				fcg.arrayIndexParameter.add(indexName);
			}
		}
		return fcg;
	}
}
