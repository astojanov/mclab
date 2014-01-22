package natlab.toolkits.analysis.core;

import java.util.Set;

import analysis.ForwardAnalysis;
import ast.ASTNode;
import ast.AssignStmt;
import ast.Function;
import ast.GlobalStmt;
import ast.Name;
import ast.Script;
import ast.Stmt;

import com.google.common.collect.Sets;

public class DefinitelyAssignedAnalysis extends
    ForwardAnalysis<Set<String>> {

  public DefinitelyAssignedAnalysis(ASTNode tree) {
    super(tree);
  }

  @Override
  public void caseScript(Script node) {
    currentInSet = newInitialFlow();
    currentOutSet = Sets.newHashSet(currentInSet);
    node.getStmts().analyze(this);
    outFlowSets.put(node, currentOutSet);
  }

  @Override
  public void caseFunction(Function node) {
    currentInSet = newInitialFlow();
    currentOutSet = Sets.newHashSet(currentInSet);

    for (Name n : node.getInputParams()) {
      currentInSet.add(n.getID());
    }

    node.getStmts().analyze(this);
    outFlowSets.put( node, currentOutSet);

    node.getNestedFunctions().analyze(this);
  }

  @Override
  public void caseAssignStmt(AssignStmt node) {
    inFlowSets.put(node, currentInSet);
    currentOutSet = Sets.newHashSet(currentInSet);
    currentOutSet.addAll(node.getLValues());
    outFlowSets.put(node, currentOutSet);
  }
  
  @Override
  public void caseGlobalStmt(GlobalStmt node) {
    inFlowSets.put(node, currentInSet);
    currentOutSet = Sets.newHashSet(currentInSet);
    for (Name name : node.getNames()) {
      currentOutSet.add(name.getID());
    }
    outFlowSets.put(node, currentOutSet);
  }

  @Override
  public void caseStmt(Stmt node){
    inFlowSets.put(node, currentInSet);
    currentOutSet = currentInSet;
    outFlowSets.put(node, currentOutSet);
  }

  @Override
  public Set<String> copy(Set<String> source) {
    return Sets.newHashSet(source);
  }

  @Override
  public Set<String> merge(Set<String> in1, Set<String> in2) {
    Set<String> out = Sets.newHashSet(in1);
    out.retainAll(in2);
    return out;
  }

  @Override
  public Set<String> newInitialFlow() {
    return Sets.newHashSet();
  }
  
  public boolean isDefinitelyAssignedAtInputOf(Stmt node, String name) {
    return inFlowSets.get(node).contains(name);
  }
}
