package natlab.tame.tamerplus.utils;

import java.util.List;
import java.util.Set;

import natlab.tame.tamerplus.analysis.DUChain;
import natlab.tame.tamerplus.analysis.DefinedVariablesNameCollector;
import natlab.tame.tamerplus.analysis.ReachingDefinitions;
import natlab.tame.tamerplus.analysis.TIRToMcSAFIRTableBuilder;
import natlab.tame.tamerplus.analysis.TemporaryVariablesRemoval;
import natlab.tame.tamerplus.analysis.UDChain;
import natlab.tame.tamerplus.analysis.UDDUWeb;
import natlab.tame.tamerplus.analysis.UsedVariablesNameCollector;
import ast.Name;

import com.google.common.collect.Sets;

public class TamerPlusUtils
{
    
    public static Set<String> getNameListAsStringSet(List<Name> nameList)
    {
        Set<String> nameSet = Sets.newHashSet();
        for (Name variableName : nameList)
        {
            if (variableName != null)
            {
                nameSet.add(variableName.getID());
            }
        }
        return nameSet;
    }
    
    public static Set<String> getNameListAsStringSet(ast.List<Name> nameList)
    {
        Set<String> nameSet = Sets.newHashSet();
        for (Name variableName : nameList)
        {
            if (variableName != null)
            {
                nameSet.add(variableName.getID());
            }
        }
        return nameSet;
    }
    
    public static void debugMode()
    {
        DefinedVariablesNameCollector.DEBUG = true;
        DUChain.DEBUG = true;
        ReachingDefinitions.DEBUG = true;
        TemporaryVariablesRemoval.DEBUG = true;
        TIRToMcSAFIRTableBuilder.DEBUG = true;
        UDChain.DEBUG = true;
        UDDUWeb.DEBUG = true;
        UsedVariablesNameCollector.DEBUG = true;
    }
}