package natlab.tame.builtin.isComplexInfoProp.ast;

import java.util.ArrayList;
import java.util.List;

import natlab.tame.builtin.isComplexInfoProp.ArgICType;
import natlab.tame.builtin.isComplexInfoProp.isComplexInfoPropMatch;
import natlab.tame.builtin.shapeprop.ShapePropMatch;
import natlab.tame.valueanalysis.value.Args;
import natlab.tame.valueanalysis.value.Value;

public class ICTypeR extends ICType{
	
	
	public ICTypeR()
	{
		
	}

	
	public String toString()
	{
		return "R";
	}


	@Override
	public isComplexInfoPropMatch match(boolean isPatternSide,
			isComplexInfoPropMatch previousMatchResult, List<Integer> argValues) {
		// TODO Auto-generated method stub
		if(true==isPatternSide)//on the symbol on the LHS
		{
			
			isComplexInfoPropMatch match = new isComplexInfoPropMatch(previousMatchResult);

			
			if(argValues.size() > previousMatchResult.getNumMatched())
			{
				int argument = argValues.get(previousMatchResult.getNumMatched());// get the value of argument
		
				 int isArgComplex =  (new ArgICType()).getArgICType(argument);
				
				
				 
			
				if (1 == isArgComplex) //i.e it is any
				{
				
					match.consumeArg();
					match.setLastMatchSucceed(true);
					match.setLastMatchICType("REAL");
					match.incNumRargs(1);
					System.out.println("matched argument to REAL\n");
										
				}
				else
				{

					match.setLastMatchSucceed(false);
				}
			}
			else
			{
				match.setError(true);
			}
			if (null == match.getLastMatchICType())
			{
				match.setLastMatchSucceed(false);
			}
			return match;
		}
		else
		{	//RHS
			isComplexInfoPropMatch match = new isComplexInfoPropMatch(previousMatchResult);
			if (match.getNumMatched() == argValues.size())
			{
				//LHS matched 
				match.loadOutput("REAL");
				System.out.println("REAL.");
				return match;
			}
			else
			{
				//match fail somewhere on LHS
				match.setError(true);
				return match;
			}
		}
	}
}