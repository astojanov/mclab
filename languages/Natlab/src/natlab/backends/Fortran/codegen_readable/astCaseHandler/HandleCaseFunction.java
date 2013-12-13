package natlab.backends.Fortran.codegen_readable.astCaseHandler;

import ast.Name;
import ast.Function;
import java.util.*;

import natlab.backends.Fortran.codegen_readable.*;

public class HandleCaseFunction {
	static boolean Debug =false;
	/**
	 * main entry point MATLAB functions are mapped to main programs in fortran;
	 * functions with only one return value are mapped to functions in fortran;
	 * functions with 0 or more than 1 return values are mapped to subroutines.
	 * 
	 * user-defined functions are mapped to subroutines.
	 *  
	 * Subprogram ::= ProgramTitle DeclarationSection StatementSection;
	 */ 
	public FortranCodeASTGenerator getFortran(
			FortranCodeASTGenerator fcg, 
			Function node) 
	{
		fcg.functionName = node.getName();
		for (Name param : node.getInputParams()) {
			fcg.inArgs.add(param.getVarName());
		}
		for (Name result : node.getOutputParams()) {
			fcg.outRes.add(result.getVarName());
		}
		/*
		 *  since all of our input matlab files are matlab functions, 
		 *  we use that entry point file name to determine which function 
		 *  should be transformed as the main program in fortran.
		 */
		if (fcg.entryPointFile.equals(node.getName())) {
			GenerateMainEntryPoint mainEntryPoint = new GenerateMainEntryPoint();
			mainEntryPoint.newMain(fcg, node);
		}
		/* 
		 * transform matlab functions, which are not entry point functions 
		 * but has only one return value, to function files in fortran.
		 * TODO currently, this case cannot happen...
		 */
		else if (fcg.outRes.size() == 1 && !fcg.userDefinedFunctions.contains(node.getName())) {
			GenerateFunction function = new GenerateFunction();
			function.newFunction(fcg, node);			
		}
		/*
		 * transform matlab functions, which are not entry point functions 
		 * but has 0 or more than one return values, to subroutines in fortran.
		 */
		else {
			GenerateSubroutine subroutine = new GenerateSubroutine();
			subroutine.newSubroutine(fcg, node);
		}
		return fcg;
	}
}
