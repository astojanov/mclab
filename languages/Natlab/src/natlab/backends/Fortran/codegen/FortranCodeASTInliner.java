package natlab.backends.Fortran.codegen;

import java.util.ArrayList;

import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.tir.TIRAbstractAssignToListStmt;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.shape.ShapeFactory;
import natlab.backends.Fortran.codegen.FortranAST.*;
import natlab.backends.Fortran.codegen.ASTcaseHandler.*;

public class FortranCodeASTInliner {

	public FortranCodeASTInliner(){
		
	}
	
	public static NoDirectBuiltinExpr inline(FortranCodeASTGenerator fcg, TIRAbstractAssignToListStmt node){
		NoDirectBuiltinExpr noDirBuiltinExpr = new NoDirectBuiltinExpr();
		
		if(node.getRHS().getVarName().equals("horzcat")){
			String indent = new String();
			for(int i=0; i<fcg.indentNum; i++){
				indent = indent + fcg.indent;
			}
			String LHS = node.getLHS().getNodeString().replace("[", "").replace("]", "");
			ArrayList<String> args = new ArrayList<String>();
			args = HandleCaseTIRAbstractAssignToListStmt.getArgsList(node);
			int argsNum = args.size();
			StringBuffer tmpBuf = new StringBuffer();
			for(int i=1; i<=argsNum; i++){
				tmpBuf.append(indent+LHS+"(1,"+i+") = "+args.get(i-1)+";");
				if(i<argsNum){
					tmpBuf.append("\n");	
				}
			}
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		else if(node.getRHS().getVarName().equals("vertcat")){
			String indent = new String();
			for(int i=0; i<fcg.indentNum; i++){
				indent = indent + fcg.indent;
			}
			String LHS = node.getLHS().getNodeString().replace("[", "").replace("]", "");
			ArrayList<String> args = new ArrayList<String>();
			args = HandleCaseTIRAbstractAssignToListStmt.getArgsList(node);
			int argsNum = args.size();
			StringBuffer tmpBuf = new StringBuffer();
			for(int i=1; i<=argsNum; i++){
				tmpBuf.append(indent+LHS+"("+i+",:) = "+args.get(i-1)+"(1,:);");
				if(i<argsNum){
					tmpBuf.append("\n");
				}
			}
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		else if(node.getRHS().getVarName().equals("ones")){
			String indent = new String();
			for(int i=0; i<fcg.indentNum; i++){
				indent = indent + fcg.indent;
			}
			String LHS = node.getLHS().getNodeString().replace("[", "").replace("]", "");
			ArrayList<String> args = new ArrayList<String>();
			args = HandleCaseTIRAbstractAssignToListStmt.getArgsList(node);
			int argsNum = args.size();
			StringBuffer tmpBuf = new StringBuffer();
			/*do i = 1 , 10
			    do j = 1 , 5
		  	      a(i,j) = 1;
		   	    enddo
		      enddo
		     */
			tmpBuf.append(indent+"do mc_tmp_"+LHS+"_i = 1,"+args.get(0)+"\n");
			tmpBuf.append(indent+fcg.indent+"do mc_tmp_"+LHS+"_j = 1,"+args.get(1)+"\n");
			tmpBuf.append(indent+fcg.indent+fcg.indent+LHS+"(mc_tmp_"+LHS+"_i,mc_tmp_"+LHS+"_j) = 1;\n");
			tmpBuf.append(indent+fcg.indent+"enddo\n");
			tmpBuf.append(indent+"enddo");
			
			ArrayList<Integer> shape = new ArrayList<Integer>();
			shape.add(1);
			shape.add(1);
			BasicMatrixValue tmp = 
					new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
			fcg.tmpVariables.put("mc_tmp_"+LHS+"_i", tmp);
			fcg.tmpVariables.put("mc_tmp_"+LHS+"_j", tmp);
			fcg.forStmtParameter.add(args.get(0));
			fcg.forStmtParameter.add(args.get(1));
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		else if(node.getRHS().getVarName().equals("zeros")){
			String indent = new String();
			for(int i=0; i<fcg.indentNum; i++){
				indent = indent + fcg.indent;
			}
			String LHS = node.getLHS().getNodeString().replace("[", "").replace("]", "");
			ArrayList<String> args = new ArrayList<String>();
			args = HandleCaseTIRAbstractAssignToListStmt.getArgsList(node);
			int argsNum = args.size();
			StringBuffer tmpBuf = new StringBuffer();
			/*do i = 1 , 10
			    do j = 1 , 5
		  	      a(i,j) = 0;
		   	    enddo
		      enddo
		     */
			tmpBuf.append(indent+"do mc_tmp_"+LHS+"_i = 1,"+args.get(0)+"\n");
			tmpBuf.append(indent+fcg.indent+"do mc_tmp_"+LHS+"_j = 1,"+args.get(1)+"\n");
			tmpBuf.append(indent+fcg.indent+fcg.indent+LHS+"(mc_tmp_"+LHS+"_i,mc_tmp_"+LHS+"_j) = 0;\n");
			tmpBuf.append(indent+fcg.indent+"enddo\n");
			tmpBuf.append(indent+"enddo");
			
			ArrayList<Integer> shape = new ArrayList<Integer>();
			shape.add(1);
			shape.add(1);
			BasicMatrixValue tmp = 
					new BasicMatrixValue(PrimitiveClassReference.INT8,(new ShapeFactory()).newShapeFromIntegers(shape));
			fcg.tmpVariables.put("mc_tmp_"+LHS+"_i", tmp);
			fcg.tmpVariables.put("mc_tmp_"+LHS+"_j", tmp);
			fcg.forStmtParameter.add(args.get(0));
			fcg.forStmtParameter.add(args.get(1));
			noDirBuiltinExpr.setCodeInline(tmpBuf.toString());
		}
		else{
			/**
			 * for those no direct builtins which not be implemented yet 
			 */
			noDirBuiltinExpr.setCodeInline("!    the built-in function \""+node.getRHS().getVarName()+"\" has not been implemented yet, fix it!");
		}
		return noDirBuiltinExpr;
	}
}
