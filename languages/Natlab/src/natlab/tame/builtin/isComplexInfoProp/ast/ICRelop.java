package natlab.tame.builtin.isComplexInfoProp.ast;

import java.util.List;

import natlab.tame.builtin.isComplexInfoProp.isComplexInfoPropMatch;
import natlab.tame.valueanalysis.value.Value;

public abstract class ICRelop extends ICNode{
	abstract public isComplexInfoPropMatch match(boolean isPatternSide, isComplexInfoPropMatch previousMatchResult, List<? extends Value<?>> argValues);
}
