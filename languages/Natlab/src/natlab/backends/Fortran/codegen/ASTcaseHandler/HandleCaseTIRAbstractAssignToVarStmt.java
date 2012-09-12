package natlab.backends.Fortran.codegen.ASTcaseHandler;

import java.util.ArrayList;

import natlab.backends.Fortran.codegen.*;
import natlab.tame.tir.*;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.shape.Shape;
import natlab.tame.valueanalysis.components.shape.ShapeFactory;
import natlab.tame.classes.reference.*;

public class HandleCaseTIRAbstractAssignToVarStmt {

	static boolean Debug = false;
	static boolean lhsShapeIsknown = true;
	static boolean rhsShapeIsKnown = true;
	
	public HandleCaseTIRAbstractAssignToVarStmt(){
		
	}
	
	public FortranCodePrettyPrinter getFortran(FortranCodePrettyPrinter fcg, TIRAbstractAssignToVarStmt node){
		if (Debug) System.out.println("in an abstractAssignToVar statement");
		String LHS, RHS;
		//ArrayList<String> vars = new ArrayList<String>();
		LHS = node.getTargetName().getID();
		RHS = node.getRHS().getNodeString();
		if(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().get(LHS).getSingleton())).isConstant()){
			if (Debug) System.out.println(LHS+" is a constant");
		}
		else{
			ArrayList<Integer> lhsVariableDimension = new ArrayList<Integer>(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().get(LHS).getSingleton())).getShape().getDimensions());
			ArrayList<Integer> rhsVariableDimension = new ArrayList<Integer>(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().get(node.getRHS().getNodeString()).getSingleton())).getShape().getDimensions());
			try{
				for(Integer intgrL : lhsVariableDimension){
					//if lhs variable's shape is not exactly known, we need allocate it first.
					if(intgrL==null){
						System.out.println("The shape of "+LHS+" is not exactly known, we need allocate it first");
						lhsShapeIsknown = false;
					}
				}
				for(Integer intgrR : rhsVariableDimension){
					if(intgrR==null){
						System.out.println("The shape of "+RHS+" is not exactly konwn, we cannot allocate "+LHS+" with "+RHS+"'s shape, " +
								"we need runtime check "+RHS+"'s shape first.");
						rhsShapeIsKnown = false;
					}
				}
				//TODO inline runtime check code for RHS
				if((lhsShapeIsknown == false)&&(rhsShapeIsKnown == false)){
					fcg.buf.append(LHS+"_shapeTmp = shape("+RHS+");\n");
					fcg.buf.append("allocate("+LHS+"("+LHS+"_shapeTmp(1),"+LHS+"_shapeTmp(2)));\n");
					ArrayList<Integer> shape = new ArrayList<Integer>();
					shape.add(2);
					BasicMatrixValue tmp = 
							new BasicMatrixValue(new BasicMatrixValue(PrimitiveClassReference.INT8),(new ShapeFactory()).newShapeFromIntegers(shape));
					fcg.tmpVariables.put(LHS+"_shapeTmp",tmp);
				}
				else if(lhsShapeIsknown == false){
					fcg.buf.append("allocate("+LHS+"("+rhsVariableDimension.toString().replace("[", "").replace("]", "")+"));\n  ");
				}
			}
			catch(Exception e){
				System.err.println("error in HandleCaseTIRAbstractAssignToVarStmt.java");
			}
			String type = fcg.FortranMap.getFortranTypeMapping(((BasicMatrixValue)(fcg.analysis.getNodeList().get(fcg.index).getAnalysis().getCurrentOutSet().get(LHS).getSingleton())).getMatlabClass().toString());
			fcg.buf.append(node.getLHS().getNodeString()+" = ");
			if(type == "String"){
				//type = makeFortranStringLiteral(type);
				fcg.buf.append(fcg.makeFortranStringLiteral(node.getRHS().getNodeString()) + ";");
			}
			else
				fcg.buf.append(node.getRHS().getNodeString() + ";");
			//TODO check for expression on RHS
			//TODO check for built-ins
			//TODO check for operators
		}
		return fcg;
	}
}
