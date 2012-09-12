package natlab.backends.Fortran.codegen.FortranAST;


public class Program extends ASTNode implements Cloneable {
    // Declared in FortranIR.ast line 1

    public Program() {
        super();

        setChild(new List(), 0);
    }

    // Declared in FortranIR.ast line 1
    public Program(List p0) {
        setChild(p0, 0);
    }

    public Object clone() throws CloneNotSupportedException {
        Program node = (Program)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          Program node = (Program)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        Program res = (Program)copy();
        for(int i = 0; i < getNumChild(); i++) {
          ASTNode node = getChildNoTransform(i);
          if(node != null) node = node.fullCopy();
          res.setChild(node, i);
        }
        return res;
    }
    public void flushCache() {
        super.flushCache();
    }
  protected int numChildren() {
    return 1;
  }
    // Declared in FortranIR.ast line 1
    public void setSubProgramList(List list) {
        setChild(list, 0);
    }

    public int getNumSubProgram() {
        return getSubProgramList().getNumChild();
    }

    public SubProgram getSubProgram(int i) {
        return (SubProgram)getSubProgramList().getChild(i);
    }

    public void addSubProgram(SubProgram node) {
        List list = getSubProgramList();
        list.setChild(node, list.getNumChild());
    }

    public void setSubProgram(SubProgram node, int i) {
        List list = getSubProgramList();
        list.setChild(node, i);
    }
    public List getSubProgramList() {
        return (List)getChild(0);
    }

    public List getSubProgramListNoTransform() {
        return (List)getChildNoTransform(0);
    }


    // Declared in PrettyPrinter.jadd at line 3

   public void pp() {}

}
