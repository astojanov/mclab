package natlab.tame.valueanalysis.advancedMatrix;

import java.util.*;

import natlab.tame.builtin.*;
import natlab.tame.classes.reference.*;
import natlab.tame.valueanalysis.*;
import natlab.tame.valueanalysis.aggrvalue.*;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.constant.Constant;
import natlab.tame.valueanalysis.components.constant.ConstantPropagator;
import natlab.tame.valueanalysis.components.constant.DoubleConstant;
import natlab.tame.valueanalysis.components.isComplex.isComplexInfo;
import natlab.tame.valueanalysis.components.isComplex.isComplexInfoPropagator;
import natlab.tame.valueanalysis.components.mclass.ClassPropagator;
import natlab.tame.valueanalysis.components.shape.Shape;
import natlab.tame.valueanalysis.components.shape.ShapeFactory;
import natlab.tame.valueanalysis.components.shape.ShapePropagator;
import natlab.tame.valueanalysis.simplematrix.SimpleMatrixValue;
import natlab.tame.valueanalysis.value.*;

public class AdvancedMatrixValuePropagator extends
		MatrixPropagator<AdvancedMatrixValue> {
	static boolean Debug = false;
	ConstantPropagator<AggrValue<AdvancedMatrixValue>> constantProp = ConstantPropagator
			.getInstance();
	ClassPropagator<AggrValue<AdvancedMatrixValue>> classProp = ClassPropagator
			.getInstance();
	ShapePropagator<AggrValue<AdvancedMatrixValue>> shapeProp = ShapePropagator
			.getInstance();
	isComplexInfoPropagator<AggrValue<AdvancedMatrixValue>> isComplexInfoProp = isComplexInfoPropagator
			.getInstance();

	public AdvancedMatrixValuePropagator() {
		super(new AdvancedMatrixValueFactory());
	}

	/**
	 * base case
	 */
	@Override
	public Res<AggrValue<AdvancedMatrixValue>> caseBuiltin(Builtin builtin,
			Args<AggrValue<AdvancedMatrixValue>> arg) {
		Constant cResult = builtin.visit(constantProp, arg);
		if (cResult != null) {
			return Res
					.<AggrValue<AdvancedMatrixValue>> newInstance(new AdvancedMatrixValue(
							cResult));
		}

		// if the result is not a constant, just do mclass propagation
		List<Set<ClassReference>> matchClassResult = builtin.visit(classProp,
				arg);
		if (matchClassResult == null) { // class prop returned error
			return Res.newErrorResult(builtin.getName()
					+ " is not defined for arguments " + arg + "as class");
		}
		// deal with shape XU added
		List<Shape<AggrValue<AdvancedMatrixValue>>> matchShapeResult = builtin.visit(shapeProp, arg); 
		//FIXME - commented to stop visiting shape propogation

		if (matchShapeResult == null) {
			if (Debug)
				System.out.println("shape results are empty");
		}

		List<isComplexInfo<AggrValue<AdvancedMatrixValue>>> matchisComplexInfoResult = builtin
				.visit(isComplexInfoProp, arg);
		if (matchisComplexInfoResult == null) {
			System.out.println("no complexinfo results");
		}

		// build results out of the result classes and shape XU modified, not
		// finished!!!
		return matchResultToRes(matchClassResult, matchShapeResult,
				matchisComplexInfoResult);

	}

	private Res<AggrValue<AdvancedMatrixValue>> matchResultToRes(
			List<Set<ClassReference>> matchClassResult,
			List<Shape<AggrValue<AdvancedMatrixValue>>> matchShapeResult,
			List<isComplexInfo<AggrValue<AdvancedMatrixValue>>> matchisComplexInfoResult) {
		// go through and fill in result
		Res<AggrValue<AdvancedMatrixValue>> result = Res.newInstance();
		for (Set<ClassReference> values : matchClassResult) {
			HashMap<ClassReference, AggrValue<AdvancedMatrixValue>> map = new HashMap<ClassReference, AggrValue<AdvancedMatrixValue>>();

			for (ClassReference classRef : values) {
				map.put(classRef,
						new AdvancedMatrixValue(new AdvancedMatrixValue(
								(PrimitiveClassReference) classRef),
								matchShapeResult.get(0),  //FIXME - commented to stop visiting shape propogation
								matchisComplexInfoResult.get(0)));// FIXME a
																	// little
																	// bit
																	// tricky
			}
			result.add(ValueSet.newInstance(map));
			if (Debug)
				System.out.println(result);
		}
		return result;
	}

	
	@Override
	public Res<AggrValue<AdvancedMatrixValue>> caseAbstractConcatenation(
			Builtin builtin, Args<AggrValue<AdvancedMatrixValue>> arg) {

		List<isComplexInfo<AggrValue<AdvancedMatrixValue>>> matchisComplexInfoResult = builtin
				.visit(isComplexInfoProp, arg);
		if (matchisComplexInfoResult == null) {
			System.out.println("no complexinfo results");
		}
		// this block ends
		return Res
				.<AggrValue<AdvancedMatrixValue>> newInstance(new AdvancedMatrixValue(
						new AdvancedMatrixValue(
								(PrimitiveClassReference)getDominantCatArgClass(arg)),

						matchisComplexInfoResult.get(0)));// FIXME a little bit
															// tricky
	}
}
