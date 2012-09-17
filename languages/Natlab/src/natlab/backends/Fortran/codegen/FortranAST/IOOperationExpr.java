package natlab.backends.Fortran.codegen.FortranAST;


public class IOOperationExpr extends Expression implements Cloneable {
    // Declared in FortranIR.ast line 31

    public IOOperationExpr() {
        super();

    }

    // Declared in FortranIR.ast line 31
    public IOOperationExpr(String p0, String p1) {
        setIOOperator(p0);
        setArgsList(p1);
    }

    public Object clone() throws CloneNotSupportedException {
        IOOperationExpr node = (IOOperationExpr)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          IOOperationExpr node = (IOOperationExpr)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        IOOperationExpr res = (IOOperationExpr)copy();
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
    return 0;
  }
    // Declared in FortranIR.ast line 31
    private String tokenString_IOOperator;
    public void setIOOperator(String value) {
        tokenString_IOOperator = value;
    }
    public String getIOOperator() {
        return tokenString_IOOperator;
    }


    // Declared in FortranIR.ast line 31
    private String tokenString_ArgsList;
    public void setArgsList(String value) {
        tokenString_ArgsList = value;
    }
    public String getArgsList() {
        return tokenString_ArgsList;
    }


    // Declared in PrettyPrinter.jadd at line 148

    public void pp() {
    	System.out.print(getIOOperator()+getArgsList()+";");
    }

}
