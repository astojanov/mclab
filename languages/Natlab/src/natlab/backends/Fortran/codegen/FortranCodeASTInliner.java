package natlab.backends.Fortran.codegen;

import java.util.ArrayList;

import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.tir.TIRAbstractAssignToListStmt;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.shape.ShapeFactory;
import natlab.tame.valueanalysis.components.constant.*;
import natlab.backends.Fortran.codegen.FortranAST.*;
import natlab.backends.Fortran.codegen.ASTcaseHandler.*;

public class FortranCodeASTInliner {

	static boolean Debug = false;
	
	public FortranCodeASTInliner(){
		
	}
	
	public static NoDirectBuiltinExpr inline(FortranCodeASTGenerator fcg, TIRAbstractAssignToListStmt node){
		NoDirectBuiltinExpr noDirBuiltinExpr = new NoDirectBuiltinExpr();
		String LHS = node.getLHS().getNodeString().replace("[", "").replace("]", "");
		
		if(fcg.isSubroutine==true){
			/**
			 * if input argument on the LHS of assignment stmt, we assume that this input argument maybe modified.
			 */
			if(fcg.inArgs.contains(LHS)){
				if (Debug) System.out.println("subroutine's input "+LHS+" has been modified!");
				if(fcg.inputHasChanged.contains(LHS)){
					if (Debug) System.out.println("encounter "+LHS+" again.");
				}
				else{
					if (Debug) System.out.println("first time encounter "+LHS);
					fcg.inputHasChanged.add(LHS);
				}
				LHS=LHS+"_copy";
			}
		}

		String indent = new String();
		for(int i=0; i<fcg.indentNum; i++){
			indent = indent + fcg.indent;
		}
		ArrayList<String> args = new ArrayList<String>();
		args = HandleCaseTIRAbstractAssignToListStmt.getArgsList(node);
		int argsNum = args.size();
		StringBuffer tmpBuf = new StringBuffer();
		String rhsFucntion = node.getRHS().getVarName();
		/*
		 * below are all the cases by enumeration, to be extended.
		 */
		if(rhsFucntion.equals("horzcat")){
			tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
			for(int i=1; i<=argsNum; i++){
				/**
				 * need constant variable replacement check.
				 */
				if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(i-1)).getSingleton())).isConstant()){
					Constant c = ((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(i-1)).getSingleton())).getConstant();
					if(fcg.inArgs.contains(LHS)){
						
					}
					tmpBuf.append(indent+LHS+"(1,"+i+") = "+c+";");
					if(i<argsNum){
						tmpBuf.append("\n");	
					}	
				}
				else{
					tmpBuf.append(indent+LHS+"(1,"+i+") = "+args.get(i-1)+";");
					if(i<argsNum){
						tmpBuf.append("\n");	
					}					
				}
			}
			tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		
		else if(rhsFucntion.equals("vertcat")){
			tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
			for(int i=1; i<=argsNum; i++){
				/**
				 * need constant variable replacement check.
				 */
				if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(i-1)).getSingleton())).isConstant()){
					DoubleConstant c = (DoubleConstant) ((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(i-1)).getSingleton())).getConstant();
					tmpBuf.append(indent+LHS+"("+i+",1) = "+c.toString()+";");
				}
				else{
					tmpBuf.append(indent+LHS+"("+i+",:) = "+args.get(i-1)+"(1,:);");					
				}
				if(i<argsNum){
					tmpBuf.append("\n");
				}
			}
			tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		
		else if(rhsFucntion.equals("ones")){
			tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
			/*do i = 1 , 10
			    do j = 1 , 5
		  	      a(i,j) = 1;
		   	    enddo
		      enddo
		     */
			/**
			 * need constant variable replacement check.
			 */
			if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
					get(args.get(0)).getSingleton())).isConstant()){
				DoubleConstant c = (DoubleConstant) ((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(0)).getSingleton())).getConstant();
				int ci = c.getValue().intValue();
				tmpBuf.append(indent+"do tmp_"+LHS+"_i = 1 , "+ci+"\n");
			}
			else{
				tmpBuf.append(indent+"do tmp_"+LHS+"_i = 1 , "+args.get(0)+"\n");
			}
			if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
					get(args.get(1)).getSingleton())).isConstant()){
				DoubleConstant c = (DoubleConstant)((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(1)).getSingleton())).getConstant();
				int ci = c.getValue().intValue();
				tmpBuf.append(indent+fcg.indent+"do tmp_"+LHS+"_j = 1 , "+ci+"\n");
			}
			else{
				tmpBuf.append(indent+fcg.indent+"do tmp_"+LHS+"_j = 1 , "+args.get(1)+"\n");
			}
			tmpBuf.append(indent+fcg.indent+fcg.indent+LHS+"(tmp_"+LHS+"_i,tmp_"+LHS+"_j) = 1;\n");
			tmpBuf.append(indent+fcg.indent+"enddo\n");
			tmpBuf.append(indent+"enddo");
			
			ArrayList<Integer> shape = new ArrayList<Integer>();
			shape.add(1);
			shape.add(1);
			BasicMatrixValue tmp = 
					new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
			fcg.tmpVariables.put("tmp_"+LHS+"_i", tmp);
			fcg.tmpVariables.put("tmp_"+LHS+"_j", tmp);
			fcg.forStmtParameter.add(args.get(0));
			fcg.forStmtParameter.add(args.get(1));
			tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		
		else if(rhsFucntion.equals("zeros")){
			tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
			/*do i = 1 , 10
			    do j = 1 , 5
		  	      a(i,j) = 0;
		   	    enddo
		      enddo
		     */
			/**
			 * need constant variable replacement check.
			 */
			if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
					get(args.get(0)).getSingleton())).isConstant()){
				DoubleConstant c = (DoubleConstant) ((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(0)).getSingleton())).getConstant();
				int ci = c.getValue().intValue();
				tmpBuf.append(indent+"do tmp_"+LHS+"_i = 1 , "+ci+"\n");
			}
			else{
				tmpBuf.append(indent+"do tmp_"+LHS+"_i = 1 , "+args.get(0)+"\n");
			}
			if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
					get(args.get(1)).getSingleton())).isConstant()){
				DoubleConstant c = (DoubleConstant)((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(1)).getSingleton())).getConstant();
				int ci = c.getValue().intValue();
				tmpBuf.append(indent+fcg.indent+"do tmp_"+LHS+"_j = 1 , "+ci+"\n");
			}
			else{
				tmpBuf.append(indent+fcg.indent+"do tmp_"+LHS+"_j = 1 , "+args.get(1)+"\n");
			}
			tmpBuf.append(indent+fcg.indent+fcg.indent+LHS+"(tmp_"+LHS+"_i,tmp_"+LHS+"_j) = 0;\n");
			tmpBuf.append(indent+fcg.indent+"enddo\n");
			tmpBuf.append(indent+"enddo");
			
			ArrayList<Integer> shape = new ArrayList<Integer>();
			shape.add(1);
			shape.add(1);
			BasicMatrixValue tmp = 
					new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
			fcg.tmpVariables.put("tmp_"+LHS+"_i", tmp);
			fcg.tmpVariables.put("tmp_"+LHS+"_j", tmp);
			fcg.forStmtParameter.add(args.get(0));
			fcg.forStmtParameter.add(args.get(1));
			tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		
		else if(rhsFucntion.equals("colon")){
			/*
			 * Depending on the fact that whether the target variable is temporary, 
			 * we have two solutions for colon.
			 * 1. if it is a temporary variable, it means that this variable will be used as an index in the future;
			 * 2. if it is not a temporary variable, it means that this variable is made on purpose, we should leave it as people expected.
			 */
			//one more thing, we assume that the number of target variables of colon is only one.
			if(node.getTargets().asNameList().get(0).tmpVar){
				//TODO store the range information of this temp variable for later use.
				for(int i=0;i<args.size();i++){
					if(((HasConstant)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(i)).getSingleton())).getConstant()!=null){
						DoubleConstant c = (DoubleConstant) ((HasConstant)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(i)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						args.remove(i);
						args.add(i, String.valueOf(ci));
					}
					else{
						//do nothing
					}
				}
				fcg.tmpVarAsArrayIndex.put(node.getTargets().asNameList().get(0).getID(), args);
			}
			else{
				/*
				 * a=1:10 -> a=[1,2,3,...,10]
				 * a=1:2:10 -> a=[1,3,5,...,9]
				 * so, depends on the number of input parameters, there are two transformations.
				 */
				if(argsNum==2){
					tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
					/* a = arg1:arg2
					 * -->
					 * do tmp_a_i = arg1,arg2
				  	 *   a(1,tmp_a_i) = tmp_a_i;
				     * enddo
				     */
					/**
					 * need constant variable replacement check.
					 */
					if(((HasConstant)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(0)).getSingleton())).getConstant()!=null){
						DoubleConstant c = (DoubleConstant) ((HasConstant)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(0)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						tmpBuf.append(indent+"do tmp_"+LHS+"_i = "+ci);
					}
					else{
						tmpBuf.append(indent+"do tmp_"+LHS+"_i = "+args.get(0));
					}
					if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(1)).getSingleton())).isConstant()){
						DoubleConstant c = (DoubleConstant)((HasConstant)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(1)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						tmpBuf.append(","+ci+"\n");
					}
					else{
						tmpBuf.append(","+args.get(1)+"\n");
					}
					tmpBuf.append(fcg.indent+fcg.indent+LHS+"(1,tmp_"+LHS+"_i) = tmp_"+LHS+"_i;\n");
					tmpBuf.append(indent+"enddo");
					
					ArrayList<Integer> shape = new ArrayList<Integer>();
					shape.add(1);
					shape.add(1);
					BasicMatrixValue tmp = 
							new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
					fcg.tmpVariables.put("tmp_"+LHS+"_i", tmp);
					fcg.forStmtParameter.add(args.get(0));
					fcg.forStmtParameter.add(args.get(1));
					tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
					noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
				}
				else if(argsNum==3){
					tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
					/* a = lower:inc:upper
					 * -->
					 * tmp_a_index = 1;
					 * do tmp_a_i = lower,upper,inc
				  	 *   a(1,tmp_a_index) = tmp_a_i;
				  	 *   tmp_a_index=tmp_a_index+1;
				     * enddo
				     */
					tmpBuf.append("tmp_"+LHS+"_index = 1;\n");
					/**
					 * need constant variable replacement check.
					 */
					if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(0)).getSingleton())).isConstant()){
						DoubleConstant c = (DoubleConstant) ((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(0)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						tmpBuf.append(indent+"do tmp_"+LHS+"_i = "+ci);
					}
					else{
						tmpBuf.append(indent+"do tmp_"+LHS+"_i = "+args.get(0));
					}
					if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(2)).getSingleton())).isConstant()){
						DoubleConstant c = (DoubleConstant)((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(2)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						tmpBuf.append(","+ci);
					}
					else{
						tmpBuf.append(","+args.get(1));
					}
					if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(1)).getSingleton())).isConstant()){
						DoubleConstant c = (DoubleConstant)((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
								get(args.get(1)).getSingleton())).getConstant();
						int ci = c.getValue().intValue();
						tmpBuf.append(","+ci+"\n");
					}
					else{
						tmpBuf.append(","+args.get(1)+"\n");
					}
					tmpBuf.append(fcg.indent+fcg.indent+LHS+"(1,tmp_"+LHS+"_index) = tmp_"+LHS+"_i;\n");
					tmpBuf.append(fcg.indent+fcg.indent+"tmp_"+LHS+"_index = tmp_"+LHS+"_index+1;\n");
					tmpBuf.append(indent+"enddo");
					
					ArrayList<Integer> shape = new ArrayList<Integer>();
					shape.add(1);
					shape.add(1);
					BasicMatrixValue tmp = 
							new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
					fcg.tmpVariables.put("tmp_"+LHS+"_i", tmp);
					fcg.tmpVariables.put("tmp_"+LHS+"_index", tmp);
					fcg.forStmtParameter.add(args.get(0));
					fcg.forStmtParameter.add(args.get(1));
					fcg.forStmtParameter.add(args.get(2));
					tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
					noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
				}
				
			}
		}
		
		else if(rhsFucntion.equals("randperm")){
			tmpBuf.append(indent+"!  below is the mapping Fortran code for the built-in function "+rhsFucntion+"\n");
			/*
			 * a=randperm(6) will get a=[1,4,3,6,5,2],
			 * 
			 */
			if(argsNum==1){
				if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
						get(args.get(0)).getSingleton())).isConstant()){
					DoubleConstant c = (DoubleConstant)((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().
							get(args.get(0)).getSingleton())).getConstant();
					int ci = c.getValue().intValue();
					tmpBuf.append("call randperm("+ci+","+LHS+")");
				}
				else{
					tmpBuf.append("call randperm("+args.get(0)+","+LHS+")");
				}
			}
			else if(argsNum==2){
				//TODO
			}
			else{
				//TODO this should be an error, throw exception?
			}
			tmpBuf.append("\n"+indent+"!  the mapping Fortran code for the built-in function "+rhsFucntion+" is over.");
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		
		else{
			/**
			 * for those no direct builtins which not be implemented yet 
			 */
			noDirBuiltinExpr.setCodeInline("!    the built-in function \""+rhsFucntion+"\" has not been implemented yet, fix it!");
		}
		return noDirBuiltinExpr;
	}
}
