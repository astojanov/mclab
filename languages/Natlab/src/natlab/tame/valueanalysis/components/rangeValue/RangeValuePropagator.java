package natlab.tame.valueanalysis.components.rangeValue;

import java.util.ArrayList;
import java.lang.Math;

import natlab.tame.builtin.Builtin;
import natlab.tame.builtin.BuiltinVisitor;
import natlab.tame.valueanalysis.value.Args;
import natlab.tame.valueanalysis.value.Value;
import natlab.tame.valueanalysis.components.constant.*;
import natlab.tame.valueanalysis.components.shape.*;

public class RangeValuePropagator<V extends Value<V>> 
extends BuiltinVisitor<Args<V>, RangeValue<V>> {
	
	static boolean Debug = false;
	@SuppressWarnings("rawtypes")
	static RangeValuePropagator instance = null;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	static public <V extends Value<V>> RangeValuePropagator<V> getInstance() {
		if (instance == null) instance = new RangeValuePropagator();
		return instance;
	}
	
	//hidden private constructor
	private RangeValuePropagator() {}
	
	@Override
	/**
	 * if the range value analysis cannot infer from some built-ins, 
	 * return null, not top. currently, top is returned when merging 
	 * range value of the variables which can not be easily merged.
	 */
	public RangeValue<V> caseBuiltin(Builtin builtin, Args<V> arg) {
		return null;
	}
	
	@Override
	/**
	 * unary plus
	 */
	public RangeValue<V> caseUplus(Builtin builtin, Args<V> arg) {
		RangeValue<V> range = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		if (range != null) 
			return new RangeValue<V>(range);
		else
			return null;
	}
	
	@Override
	/**
	 * binary plus.
	 */
	public RangeValue<V> casePlus(Builtin builtin, Args<V> arg) {
		RangeValue<V> range0 = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		RangeValue<V> range1 = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
		if (range0 != null	&& range1 != null) {
			DomainValue lower = null, upper = null;
			if (range0.hasLowerBound() && range1.hasLowerBound()) {
				lower = range0.getLowerBound()
						.binary_plus(range1.getLowerBound());				
			}
			if (range0.hasUpperBound() && range1.hasUpperBound()) {
				upper = range0.getUpperBound()
						.binary_plus(range1.getUpperBound());				
			}
			return new RangeValue<V>(lower, upper);			
		}
		else
			return null;
	}
	
	@Override
	/**
	 * unary minus.
	 */
	public RangeValue<V> caseUminus(Builtin builtin, Args<V> arg) {
		RangeValue<V> range = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		if (range != null) {
			DomainValue lower = null, upper = null;
			if (range.hasUpperBound()) {
				lower = range.getUpperBound().unary_minus();
			}
			if (range.hasLowerBound()) {
				upper = range.getLowerBound().unary_minus();
			}
			return new RangeValue<V>(lower, upper);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * binary minus.
	 */
	public RangeValue<V> caseMinus(Builtin builtin, Args<V> arg) {
		RangeValue<V> range0 = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		RangeValue<V> range1 = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
		if (range0 != null && range1 != null) {
			DomainValue lower = null, upper = null;
			if (range0.hasLowerBound() && range1.hasUpperBound()) {
				lower = range0.getLowerBound()
						.binary_minus(range1.getUpperBound());
			}
			if (range0.hasUpperBound() && range1.hasLowerBound()) {
				upper = range0.getUpperBound()
						.binary_minus(range1.getLowerBound());				
			}
			return new RangeValue<V>(lower, upper);			
		}
		else
			return null;
	}
	
	@Override
	/**
	 * element-by-element multiplication.
	 */
	public RangeValue<V> caseTimes(Builtin builtin, Args<V> arg) {
		RangeValue<V> range0 = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		RangeValue<V> range1 = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
		if (range0 != null && range1 != null) {
			DomainValue lower = null, upper = null;
			if (range0.hasLowerBound() && range0.hasUpperBound() 
					&& range1.hasLowerBound() && range1.hasUpperBound()) {
				lower = this.getMinimum(
						range0.getLowerBound().times(range1.getLowerBound()), 
						range0.getLowerBound().times(range1.getUpperBound()), 
						range0.getUpperBound().times(range1.getLowerBound()), 
						range0.getUpperBound().times(range1.getUpperBound()));
				upper = this.getMaximum(
						range0.getLowerBound().times(range1.getLowerBound()), 
						range0.getLowerBound().times(range1.getUpperBound()), 
						range0.getUpperBound().times(range1.getLowerBound()), 
						range0.getUpperBound().times(range1.getUpperBound()));
			}
			return new RangeValue<V>(lower, upper);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * matrix multiplication, when the arguments are both scalars, 
	 * it works the same as element-by-element multiplication.
	 */
	public RangeValue<V> caseMtimes(Builtin builtin, Args<V> arg) {
		if (((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			return caseTimes(builtin, arg);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * element-by-element rdivision.
	 */
	public RangeValue<V> caseRdivide(Builtin builtin, Args<V> arg) {
		RangeValue<V> range0 = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		RangeValue<V> range1 = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
		if (range0 != null && range1 != null) {
			DomainValue lower = null, upper = null;
			if (range0.hasLowerBound() && range0.hasUpperBound() 
					&& range1.hasLowerBound() && range1.hasUpperBound()) {
				lower = this.getMinimum(
						range0.getLowerBound().divide(range1.getLowerBound()), 
						range0.getLowerBound().divide(range1.getUpperBound()), 
						range0.getUpperBound().divide(range1.getLowerBound()), 
						range0.getUpperBound().divide(range1.getUpperBound()));
				upper = this.getMaximum(
						range0.getLowerBound().divide(range1.getLowerBound()), 
						range0.getLowerBound().divide(range1.getUpperBound()), 
						range0.getUpperBound().divide(range1.getLowerBound()), 
						range0.getUpperBound().divide(range1.getUpperBound()));				
			}
			return new RangeValue<V>(lower, upper);						
		}
		else
			return null;
	}
	
	@Override
	/**
	 * matrix rdivision, when the arguments are both scalars, 
	 * it works the same as element-by-element rdivision.
	 */
	public RangeValue<V> caseMrdivide(Builtin builtin, Args<V> arg) {
		if (((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			return caseRdivide(builtin, arg);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * log
	 */
	public RangeValue<V> caseLog(Builtin builtin, Args<V> arg) {
		RangeValue<V> range = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		if (range != null && range.getLowerBound().greaterThanZero()) {
			DomainValue lower = null, upper = null;
			if (range.hasLowerBound()) {
				lower = range.getLowerBound().log();
			}
			if (range.hasUpperBound()) {
				upper = range.getUpperBound().log();
			}
			return new RangeValue<V>(lower, upper);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * exponential
	 */
	public RangeValue<V> caseExp(Builtin builtin, Args<V> arg) {
		RangeValue<V> range = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		if (range != null) {
			DomainValue lower = null, upper = null;
			if (range.hasLowerBound()) {
				lower = range.getLowerBound().exp();
			}
			if (range.hasUpperBound()) {
				upper = range.getUpperBound().exp();
			}
			return new RangeValue<V>(lower, upper);
		}
		else
			return null;
	}
	
	@Override
	/**
	 * abs
	 */
	public RangeValue<V> caseAbs(Builtin builtin, Args<V> arg) {
		RangeValue<V> range = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		if (range != null) {
			DomainValue lower = null, upper = null;
			if (range.hasUpperBound() && range.getUpperBound().lessThanZero()) {
				lower = range.getUpperBound().unary_minus();
				if (range.hasLowerBound()) {
					upper = range.getLowerBound().unary_minus();					
				}				
			}
			else if (range.hasLowerBound() && range.getLowerBound().greaterThanZero()) {
				lower = range.getLowerBound().unary_plus();
				if (range.hasUpperBound()) {
					upper = range.getUpperBound().unary_plus();					
				}
			}
			else {
				if (range.hasLowerBound() && range.hasUpperBound()) {
					lower = new DomainValue().min(range.getLowerBound().abs())
							.min(range.getUpperBound());
					upper = range.getLowerBound().abs().max(range.getUpperBound());					
				}
			}
			return new RangeValue<V>(lower, upper);
		}
		else 
			return null;
	}
	
	@Override
	/**
	 * colon case:
	 * In MATLAB, if the lower bound bigger than the upper bound, 
	 * MATLAB will return a 1-by-0 empty matrix.
	 */
	public RangeValue<V> caseColon(Builtin builtin, Args<V> arg) {
		RangeValue<V> range0 = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
		RangeValue<V> range1 = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
		if (range0 !=null && range1 !=null) {
			DomainValue lower = null, upper = null;
			if (range0.hasLowerBound() && range1.hasLowerBound()) {
				lower = range0.getLowerBound().min(range1.getLowerBound());				
			}
			if (range0.hasUpperBound() && range1.hasUpperBound()) {
				upper = range0.getUpperBound().max(range1.getUpperBound());
			}
			return new RangeValue<V>(lower, upper);
		}
		else 
			return null;
	}
	
	/*@Override
	public RangeValue<V> caseLt(Builtin builtin, Args<V> arg) {
		if (((HasConstant)arg.get(0)).getConstant() == null 
				&& ((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& (DoubleConstant)((HasConstant)arg.get(1)).getConstant() != null) {
			double rightConstant = ((DoubleConstant)((HasConstant)arg.get(1))
					.getConstant()).getValue();
			RangeValue<V> leftRange = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
			DomainValue newUpperBound = new DomainValue(rightConstant);
			newUpperBound.setSuperMinus();
			if (leftRange != null) {
				leftRange.setUpperBound(newUpperBound);
			}
			else {
				leftRange = new RangeValue<V>(null, newUpperBound);
			}
			return null;
		}
		else if ((DoubleConstant)((HasConstant)arg.get(0)).getConstant() != null 
				&& ((HasConstant)arg.get(1)).getConstant() == null 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			double leftConstant = ((DoubleConstant)((HasConstant)arg.get(0))
					.getConstant()).getValue();
			RangeValue<V> rightRange = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(leftConstant);
			newLowerBound.setSuperPlus();
			if (rightRange != null) {
				rightRange.setLowerBound(newLowerBound);
			}
			else {
				rightRange = new RangeValue<V>(newLowerBound, null);
			}
			return null;
		}
		else 
			return null;
	}
	
	@Override
	public RangeValue<V> caseLe(Builtin builtin, Args<V> arg) {
		if (((HasConstant)arg.get(0)).getConstant() == null 
				&& ((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& (DoubleConstant)((HasConstant)arg.get(1)).getConstant() != null) {
			double rightConstant = ((DoubleConstant)((HasConstant)arg.get(1))
					.getConstant()).getValue();
			RangeValue<V> leftRange = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
			DomainValue newUpperBound = new DomainValue(rightConstant);
			if (leftRange != null) {
				leftRange.setUpperBound(newUpperBound);
			}
			else {
				leftRange = new RangeValue<V>(null, newUpperBound);
			}
			return null;
		}
		else if ((DoubleConstant)((HasConstant)arg.get(0)).getConstant() != null 
				&& ((HasConstant)arg.get(1)).getConstant() == null 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			double leftConstant = ((DoubleConstant)((HasConstant)arg.get(0))
					.getConstant()).getValue();
			RangeValue<V> rightRange = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(leftConstant);
			if (rightRange != null) {
				rightRange.setLowerBound(newLowerBound);
			}
			else {
				rightRange = new RangeValue<V>(newLowerBound, null);
			}
			return null;
		}
		else 
			return null;		
	}
	
	@Override
	public RangeValue<V> caseGt(Builtin builtin, Args<V> arg) {
		if (((HasConstant)arg.get(0)).getConstant() == null 
				&& ((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& (DoubleConstant)((HasConstant)arg.get(1)).getConstant() != null) {
			double rightConstant = ((DoubleConstant)((HasConstant)arg.get(1))
					.getConstant()).getValue();
			RangeValue<V> leftRange = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(rightConstant);
			newLowerBound.setSuperPlus();
			if (leftRange != null) {
				leftRange.setLowerBound(newLowerBound);
			}
			else {
				leftRange = new RangeValue<V>(newLowerBound, null);
			}
			return null;
		}
		else if ((DoubleConstant)((HasConstant)arg.get(0)).getConstant() != null 
				&& ((HasConstant)arg.get(1)).getConstant() == null 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			double leftConstant = ((DoubleConstant)((HasConstant)arg.get(0))
					.getConstant()).getValue();
			RangeValue<V> rightRange = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
			DomainValue newUpperBound = new DomainValue(leftConstant);
			newUpperBound.setSuperMinus();
			if (rightRange != null) {
				rightRange.setUpperBound(newUpperBound);
			}
			else {
				rightRange = new RangeValue<V>(null, newUpperBound);
			}
			return null;
		}
		else 
			return null;
	}
	
	@Override
	public RangeValue<V> caseGe(Builtin builtin, Args<V> arg) {
		if (((HasConstant)arg.get(0)).getConstant() == null 
				&& ((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& (DoubleConstant)((HasConstant)arg.get(1)).getConstant() != null) {
			double rightConstant = ((DoubleConstant)((HasConstant)arg.get(1))
					.getConstant()).getValue();
			RangeValue<V> leftRange = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(rightConstant);
			if (leftRange != null) {
				leftRange.setLowerBound(newLowerBound);
			}
			else {
				leftRange = new RangeValue<V>(newLowerBound, null);
			}
			return null;
		}
		else if ((DoubleConstant)((HasConstant)arg.get(0)).getConstant() != null 
				&& ((HasConstant)arg.get(1)).getConstant() == null 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			double leftConstant = ((DoubleConstant)((HasConstant)arg.get(0))
					.getConstant()).getValue();
			RangeValue<V> rightRange = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
			DomainValue newUpperBound = new DomainValue(leftConstant);
			if (rightRange != null) {
				rightRange.setUpperBound(newUpperBound);
			}
			else {
				rightRange = new RangeValue<V>(null, newUpperBound);
			}
			return null;
		}
		else 
			return null;
	}
	
	@Override
	public RangeValue<V> caseEq(Builtin builtin, Args<V> arg) {
		if (((HasConstant)arg.get(0)).getConstant() == null 
				&& ((HasShape<V>)arg.get(0)).getShape().isScalar() 
				&& (DoubleConstant)((HasConstant)arg.get(1)).getConstant() != null) {
			double rightConstant = ((DoubleConstant)((HasConstant)arg.get(1))
					.getConstant()).getValue();
			RangeValue<V> leftRange = ((HasRangeValue<V>)arg.get(0)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(rightConstant);
			DomainValue newUpperBound = new DomainValue(rightConstant);
			if (leftRange != null) {
				leftRange.setLowerBound(newLowerBound);
				leftRange.setUpperBound(newUpperBound);
			}
			else {
				leftRange = new RangeValue<V>(newLowerBound, newUpperBound);
			}
			return null;
		}
		else if ((DoubleConstant)((HasConstant)arg.get(0)).getConstant() != null 
				&& ((HasConstant)arg.get(1)).getConstant() == null 
				&& ((HasShape<V>)arg.get(1)).getShape().isScalar()) {
			double leftConstant = ((DoubleConstant)((HasConstant)arg.get(0))
					.getConstant()).getValue();
			RangeValue<V> rightRange = ((HasRangeValue<V>)arg.get(1)).getRangeValue();
			DomainValue newLowerBound = new DomainValue(leftConstant);
			DomainValue newUpperBound = new DomainValue(leftConstant);
			if (rightRange != null) {
				rightRange.setLowerBound(newLowerBound);
				rightRange.setUpperBound(newUpperBound);
			}
			else {
				rightRange = new RangeValue<V>(newLowerBound, newUpperBound);
			}
			return null;
		}
		else 
			return null;		
	}*/
	
	/**
	 * for loop variable.
	 * In MATLAB, the default increment is 1, and without increment input, 
	 * if the lower bound bigger than the upper bound, MATLAB will return a 
	 * 1-by-0 empty matrix. but if the increment is negative, the lower bound 
	 * should be bigger than the upper bound.
	 */
	public RangeValue<V> forRange(V lower, V upper, V inc) {
		if (inc == null) {
			if (((DoubleConstant)((HasConstant)lower).getConstant()) == null 
					|| ((DoubleConstant)((HasConstant)upper).getConstant()) == null)
				return null;
			else {
				double lowerBound = ((DoubleConstant)((HasConstant)lower)
						.getConstant()).getValue();
				double upperBound = ((DoubleConstant)((HasConstant)upper)
						.getConstant()).getValue();
				return new RangeValue<V>(new DomainValue(lowerBound)
				, new DomainValue(upperBound));							
			}
		}
		else {
			if (((DoubleConstant)((HasConstant)lower).getConstant()) == null 
					|| ((DoubleConstant)((HasConstant)upper).getConstant()) == null)
				return null;
			else {
				double lowerBound = ((DoubleConstant)((HasConstant)lower)
						.getConstant()).getValue();
				double upperBound = ((DoubleConstant)((HasConstant)upper)
						.getConstant()).getValue();
				if (lowerBound <= upperBound) {
					return new RangeValue<V>(new DomainValue(lowerBound), new DomainValue(upperBound));
				}
				else {
					return new RangeValue<V>(new DomainValue(upperBound), new DomainValue(lowerBound));
				}
			}
		}
	}
	
	// helper method
	public DomainValue getMinimum(DomainValue d1, DomainValue d2, DomainValue d3
			, DomainValue d4) {
		ArrayList<DomainValue> arr = new ArrayList<DomainValue>(4);
		arr.add(d1);
		arr.add(d2);
		arr.add(d3);
		arr.add(d4);
		DomainValue tmp;
		for (int i=0; i<4; i++) {
			for (int j=i; j<3; j++) {
				if (!arr.get(j).isLessThanEq(arr.get(j+1))) {
					tmp = arr.get(j+1);
					arr.set(j+1, arr.get(j));
					arr.set(j, tmp);
				}
			}
		}
		return arr.get(0);
	}
	
	public DomainValue getMaximum(DomainValue d1, DomainValue d2, DomainValue d3
			, DomainValue d4) {
		ArrayList<DomainValue> arr = new ArrayList<DomainValue>(4);
		arr.add(d1);
		arr.add(d2);
		arr.add(d3);
		arr.add(d4);
		DomainValue tmp;
		for (int i=0; i<4; i++) {
			for (int j=i; j<3; j++) {
				if (!arr.get(j).isLessThanEq(arr.get(j+1))) {
					tmp = arr.get(j+1);
					arr.set(j+1, arr.get(j));
					arr.set(j, tmp);
				}
			}
		}
		return arr.get(3);
	}
}
