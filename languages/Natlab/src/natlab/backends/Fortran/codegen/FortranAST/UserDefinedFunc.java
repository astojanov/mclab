package natlab.backends.Fortran.codegen.FortranAST;
public class UserDefinedFunc extends Program implements Cloneable {
    // Declared in FortranIR.ast line 3

    public UserDefinedFunc() {
        super();

        setChild(null, 0);
        setChild(null, 1);
    }

    // Declared in FortranIR.ast line 3
    public UserDefinedFunc(DeclarationSection p0, StatementSection p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    public Object clone() throws CloneNotSupportedException {
        UserDefinedFunc node = (UserDefinedFunc)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          UserDefinedFunc node = (UserDefinedFunc)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        UserDefinedFunc res = (UserDefinedFunc)copy();
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
    return 2;
  }
    // Declared in FortranIR.ast line 3
    public void setDeclarationSection(DeclarationSection node) {
        setChild(node, 0);
    }
    public DeclarationSection getDeclarationSection() {
        return (DeclarationSection)getChild(0);
    }

    public DeclarationSection getDeclarationSectionNoTransform() {
        return (DeclarationSection)getChildNoTransform(0);
    }


    // Declared in FortranIR.ast line 3
    public void setStatementSection(StatementSection node) {
        setChild(node, 1);
    }
    public StatementSection getStatementSection() {
        return (StatementSection)getChild(1);
    }

    public StatementSection getStatementSectionNoTransform() {
        return (StatementSection)getChildNoTransform(1);
    }


}
