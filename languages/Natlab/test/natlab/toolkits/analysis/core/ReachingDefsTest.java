package natlab.toolkits.analysis.core;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import mclint.util.Parsing;
import natlab.toolkits.analysis.HashMapFlowMap;
import ast.AssignStmt;
import ast.Function;
import ast.FunctionList;
import ast.GlobalStmt;
import ast.IfStmt;
import ast.Name;
import ast.Program;
import ast.Script;
import ast.Stmt;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class ReachingDefsTest extends TestCase {
  private Program program;
  private ReachingDefs analysis;
  
  private void parse(String code) {
    program = Parsing.string(code);
    analysis = new ReachingDefs(program);
    analysis.analyze();
  }
  
  private HashMapFlowMap<String, Set<Def>> inSet(Stmt node) {
    return analysis.getInFlowSets().get(node);
  }
  
  private HashMapFlowMap<String, Set<Def>> outSet(Stmt node) {
    return analysis.getOutFlowSets().get(node);
  }
  
  private HashMapFlowMap<String, Set<Def>> flow(Map<String, Set<Def>> flow) {
    HashMapFlowMap<String, Set<Def>> map = new HashMapFlowMap<String, Set<Def>>();
    for (Map.Entry<String, Set<Def>> entry : flow.entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }
  
  private Set<Def> set(Def... defs) {
    return Sets.newHashSet(defs);
  }

  public void testSimpleTransfer() {
    parse("x = 0");
    AssignStmt def = (AssignStmt) ((Script) program).getStmt(0);

    assertEquals(flow(ImmutableMap.of("x", set(ReachingDefs.UNDEF))), inSet(def));
    assertEquals(flow(ImmutableMap.of("x", set(def))), outSet(def));
  }
  
  public void testKill() {
    parse("x = 0; x = 1;");
    Script script = (Script) program;
    AssignStmt x0 = (AssignStmt) script.getStmt(0);
    AssignStmt x1 = (AssignStmt) script.getStmt(1);
    
    assertEquals(flow(ImmutableMap.of("x", set(x0))), inSet(x1));
    assertEquals(flow(ImmutableMap.of("x", set(x1))), outSet(x1));
  }
  
  public void testMerging() {
    parse(new StringBuilder()
        .append("x = 0;\n")
        .append("if 1 < 2\n")
        .append("  x = 1;\n")
        .append("else\n")
        .append("  x = 2;\n")
        .append("end").toString());
    Script script = (Script) program;
    IfStmt ifStmt = (IfStmt) script.getStmt(1);
    AssignStmt x1 = (AssignStmt) ifStmt.getIfBlock(0).getStmt(0);
    AssignStmt x2 = (AssignStmt) ifStmt.getElseBlock().getStmt(0);
    
    assertEquals(flow(ImmutableMap.of("x", set(x1))), outSet(x1));
    assertEquals(flow(ImmutableMap.of("x", set(x2))), outSet(x2));
    assertEquals(flow(ImmutableMap.of("x", set(x1, x2))), outSet(ifStmt));
  }
  
  public void testInputParametersAsDefs() {
    parse(new StringBuilder()
        .append("function f(x)\n")
        .append("  g(x);\n")
        .append("end").toString());
    
    Function f = ((FunctionList) program).getFunction(0);
    Name def = f.getInputParam(0);
    assertTrue(inSet(f.getStmt(0)).get("x").size() == 1);
    assertTrue(inSet(f.getStmt(0)).get("x").contains(def));
  }
  
  public void testGlobalStmtsAsDefs() {
    parse("global x; y = x;");
    
    Script script = (Script) program;
    GlobalStmt decl = (GlobalStmt) script.getStmt(0);
    
    assertTrue(inSet(script.getStmt(1)).get("x").size() == 1);
    assertTrue(inSet(script.getStmt(1)).get("x").contains(decl));
  }
}
