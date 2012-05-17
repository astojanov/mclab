package natlab.tame.builtin.isComplexInfoProp.ast;

import java.util.ArrayList;
import java.util.List;

import natlab.tame.builtin.isComplexInfoProp.isComplexInfoPropMatch;
import natlab.tame.builtin.shapeprop.ShapePropMatch;
import natlab.tame.valueanalysis.value.Args;
import natlab.tame.valueanalysis.value.Value;
import natlab.tame.builtin.isComplexInfoProp.ast.*;

public class ICComplexValueQ extends ICAbstractValue{
	
	ICAbstractValue xv;
	ICQOp qop;
	public ICComplexValueQ(ICAbstractValue xv, ICQOp qop)
	{
		this.xv = xv;
		this.qop = qop;
	}

	
	public String toString()
	{
		return xv.toString()+qop.toString();
	}


	@Override
	public isComplexInfoPropMatch match(boolean isPatternSide,
			isComplexInfoPropMatch previousMatchResult, List<Integer> argValues) {
		
		if (qop.toString().equals("*"))
		{
	        //String typeOfEquationValue = xv.toString();// save the type of xv i.e X,R or A
			 int localMatchCtr =0;
	         isComplexInfoPropMatch lastMatch = new isComplexInfoPropMatch(previousMatchResult); //match returns same as previousmatch instead of null
	         while (lastMatch.getNumMatched()<argValues.size() && (true == lastMatch.getLastMatchSucceed() || localMatchCtr==0))
				{
					lastMatch = xv.match(isPatternSide, lastMatch, argValues);
					localMatchCtr++;
				}
				return lastMatch;
			
		}
		else if (qop.toString().equals("+"))
		{
			isComplexInfoPropMatch lastMatch = xv.match(isPatternSide, previousMatchResult, argValues); 
			
			System.out.println(lastMatch.getNumMatched()+"~"+lastMatch.getLastMatchICType()+"\n");
		
			while (lastMatch.getNumMatched()<argValues.size() && true == lastMatch.getLastMatchSucceed())
			{
				lastMatch = xv.match(isPatternSide, lastMatch, argValues);
			}
			return lastMatch;
			
		}
		else if (qop.toString().equals("?"))
		{
			isComplexInfoPropMatch lastMatch = xv.match(isPatternSide, previousMatchResult, argValues);
			if(true == lastMatch.getError())
			{
				lastMatch = previousMatchResult;
			}
			return lastMatch;
		}
	//	return lastMatch;
		return previousMatchResult; //TODO - make sure of this
		
	}
}
