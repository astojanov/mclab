package natlab.tame.builtin.classprop;

import java.util.*;

import java.io.StringReader;
import java.lang.System;
import natlab.tame.builtin.Builtin;
import natlab.tame.builtin.classprop.ast.*;
import natlab.tame.classes.reference.*;
import natlab.tame.valueanalysis.simplematrix.SimpleMatrixValueFactory;
import natlab.tame.valueanalysis.value.*;

/**
 * tools for building class propagation information for builtins.
 * This works together with the class propagation tag of the builtins.csv,
 * and implements the java backend for the tiny class propagation langauge
 * @author ant6n
 */


public class ClassPropTool {
    public static final boolean DEBUG = false;
    static { new Functions(); }
        
    /**
     * parse a class propagation flow equation from a string, returning a CP tree.
     * returns null if the parsing failes.
     */
    public static CP parse(String source){
    	//System.err.println("parsing: "+source);
    	ClassPropParser parser = new ClassPropParser();
    	ClassPropScanner input = new ClassPropScanner(new StringReader(source));
    	try{
    		CP cp = (CP) parser.parse(input);
    		//System.err.println("success, got: "+cp);
        	return cp;
    	}catch(Exception e){
    		System.err.println("failed to parse "+source+"; defined consts:\n"+Functions.constants);
    		e.printStackTrace();
    		return null;
    	}
    }
    
    
    /**
     * given a Matlab Class Propagation description and a set of argument values,
     * returns a list of sets of return matlab classes.
     * returns null if the combination of argument classes is illegal.
     */
    public static LinkedList<HashSet<ClassReference>> matchByValues(CP tree,
            List<? extends Value<?>> argValues){
        ArrayList<ClassReference> argClasses = new ArrayList<ClassReference>(argValues.size());
        for (Value<?> v : argValues){
            argClasses.add(v.getMatlabClass());
        }
        return match(tree,argClasses,argValues);
    }
    
    /**
     * given a Matlab Class Propagation description and a set of argument classes,
     * returns a list of sets of return matlab classes
     * returns null if the combination of argument classes is illegal.
     */
    public static LinkedList<HashSet<ClassReference>> match(CP tree,
            List<ClassReference> argClasses){
        return match(tree,argClasses,null);
    }
    /**
     * given a Matlab Class Propagation description and a set of argument classes,
     * and corresponding (optional) argument values,
     * returns a list of sets of return matlab classes
     * returns null if the combination of argument classes is illegal.
     */

    public static LinkedList<HashSet<ClassReference>> match(CP tree,
            List<ClassReference> argClasses,List<? extends Value<?>> argValues){
        if (DEBUG) System.out.println("match "+tree+" with "+argValues);
        ClassPropMatch match = tree.match(true, new ClassPropMatch(), argClasses, argValues);
        //System.out.println(match.getAllResults());
        if (match == null || match.isError || match.numMatched != argClasses.size()) return null;
        return match.getAllResults();
    }
    
    
    
    //classOfString([builtin1,builtin2,builtin3,...])
    //matches the next input if it is a String, and assumes it is a String denoting
    //
    //TODO - error?
    //var(numeric|logical)&(classOfString(numeric)|)
    //public Static Class MCClassOfString

    
    /**
     * main for testing
     */
    public static void main(String[] args) {
    	
        CPBuiltin dbl = new CPBuiltin("double");
        CPBuiltin chr = new CPBuiltin("char");
        CPBuiltin itg = new CPBuiltin("int16");
        CPBuiltin log = new CPBuiltin("logical");
        
        CP a = new CPMap(new CPUnion(dbl,chr),new CPChain(log,itg));
        System.out.println();
        System.out.println(a);
        System.out.println(parse("double|char -> logical int16"));
        printMatch(a,PrimitiveClassReference.DOUBLE);
        printMatch(a,PrimitiveClassReference.CHAR);
        printMatch(a,PrimitiveClassReference.CHAR,PrimitiveClassReference.DOUBLE);
        printMatch(a);
        
        CP b = new CPChain(new CPMap(dbl,dbl),new CPMap(itg,itg));
        System.out.println();
        System.out.println(b);
        System.out.println(parse("(double -> double) (int16 -> int16)"));
        printMatch(b,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.INT16);
        
        CP c = new CPUnion(new CPMap(new CPChain(new CPUnion(itg,dbl),dbl),chr),new CPMap(dbl,dbl));
        System.out.println();
        System.out.println(c);
        System.out.println(parse("int16|double double->char || double->double"));
        printMatch(c,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(c,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(c,PrimitiveClassReference.INT16,PrimitiveClassReference.DOUBLE);
        printMatch(c,PrimitiveClassReference.DOUBLE);

        CP c2 = new CPUnion(new CPMap(dbl,dbl),new CPMap(new CPChain(new CPUnion(itg,dbl),dbl),chr));
        System.out.println();
        System.out.println(c2);
        System.out.println(parse("double->double || int16|double double->char"));
        printMatch(c2,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(c2,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(c2,PrimitiveClassReference.INT16,PrimitiveClassReference.DOUBLE);
        printMatch(c2,PrimitiveClassReference.DOUBLE);
        
        CP d = new CPUnion(new CPMap(dbl,dbl),new CPMap(itg,itg));
        System.out.println();
        System.out.println(d);
        System.out.println(parse("double->double || int16->int16"));
        printMatch(d,PrimitiveClassReference.DOUBLE);
        printMatch(d,PrimitiveClassReference.INT16);

        CP e = new CPMap(new CPChain(dbl,dbl),itg);
        System.out.println();
        System.out.println(e);
        System.out.println(parse("double double->int16"));
        printMatch(e,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(e,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(e,PrimitiveClassReference.DOUBLE);
        
        
        CP f = (new CPChain(dbl,dbl));
        System.out.println();
        System.out.println(f);
        System.out.println(parse("double double"));
        printMatch(f,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f,PrimitiveClassReference.DOUBLE);

        CP f2 = new CPMap(new CPChain(dbl,dbl),new CPNone());
        System.out.println();
        System.out.println(f2);
        System.out.println(parse("double double->none"));
        printMatch(f2,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f2,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f2,PrimitiveClassReference.DOUBLE);

        CP f3 = new CPMap(new CPNone(), new CPChain(dbl,dbl));
        System.out.println();
        System.out.println(f3);
        System.out.println(parse("none->double double"));
        printMatch(f3,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f3,PrimitiveClassReference.DOUBLE);
        printMatch(f3);

        CP f4 = new CPCoerce(
                    new CPMap(new CPUnion(chr,itg),dbl),
                    new CPMap(new CPChain(dbl,dbl),new CPChain(new CPNum(0),new CPNum(1))));
        System.out.println();
        System.out.println(f4);
        System.out.println(parse("coerce(char|int16->double,(double double->0 1))"));
        System.out.println(parse("coerce ( int16 , int16 )"));
        System.out.println(parse("coerce ( 0 , 0 )"));
        System.out.println(parse("coerce ( 0 , 0 ) -> 0"));
        printMatch(f4,PrimitiveClassReference.DOUBLE,PrimitiveClassReference.DOUBLE);
        printMatch(f4,PrimitiveClassReference.CHAR,PrimitiveClassReference.INT16);
        printMatch(f4,PrimitiveClassReference.INT16,PrimitiveClassReference.CHAR);
        
                
        CP f5 = new CPTypeString(new CPUnion(new CPBuiltin("double"),new CPBuiltin("logical")));
        System.out.println();
        System.out.println(f5);
        System.out.println(parse("typeString(double|logical)"));
        printMatch(f5,PrimitiveClassReference.DOUBLE);
        printMatch(f5,PrimitiveClassReference.CHAR);
        printMatchByValue(f5,new SimpleMatrixValueFactory().newMatrixValue("double"));
        printMatchByValue(f5,new SimpleMatrixValueFactory().newMatrixValue("int16"));
        
        
        CP f6 = new CPMap(new CPChain(new CPScalar(),new CPBuiltin("char")),new CPBuiltin("logical"));
        System.out.println();
        System.out.println(f6);
        System.out.println(parse("scalar char->logical"));
        printMatchByValue(f6,new SimpleMatrixValueFactory().newMatrixValue("aaaa"));
        printMatchByValue(f6,new SimpleMatrixValueFactory().newMatrixValue("a"));
        
        CP f7 = new CPMap(new CPBuiltin("logical"),new CPError());
        System.out.println();
        System.out.println(f7);
        System.out.println(parse("logical->error"));
        printMatch(f7,PrimitiveClassReference.LOGICAL);
        printMatch(f7,PrimitiveClassReference.DOUBLE);
        
        
        System.out.println();
        System.out.println("more tests:");
        System.out.println(Builtin.Colon.getInstance().getMatlabClassPropagationInfo());
        printMatch(Builtin.Colon.getInstance().getMatlabClassPropagationInfo(),
                PrimitiveClassReference.SINGLE,PrimitiveClassReference.DOUBLE);
        
        System.out.println();
        System.out.println(Builtin.Conj.getInstance().getMatlabClassPropagationInfo());
        printMatch(Builtin.Colon.getInstance().getMatlabClassPropagationInfo(),
                PrimitiveClassReference.DOUBLE);
        
        System.out.println();
        System.out.println(Builtin.Colon.getInstance().getMatlabClassPropagationInfo());
        printMatch(Builtin.Colon.getInstance().getMatlabClassPropagationInfo(),
                PrimitiveClassReference.SINGLE,PrimitiveClassReference.DOUBLE);
        
        
        System.out.println();
        System.out.println(Builtin.Ones.getInstance().getMatlabClassPropagationInfo());
        printMatchByValue(Builtin.Ones.getInstance().getMatlabClassPropagationInfo(),
                new SimpleMatrixValueFactory().newMatrixValue("double"));

        System.out.println();
        System.out.println(Builtin.Complex.getInstance().getMatlabClassPropagationInfo());
        printMatch(Builtin.Complex.getInstance().getMatlabClassPropagationInfo(),
                PrimitiveClassReference.SINGLE,PrimitiveClassReference.DOUBLE);
        
        
        //test that parser is indemptotent for all builtins
        System.out.println("\ntest builtins:");
        for (String s : Builtin.getAllBuiltinNames()){
        	Builtin builtin = Builtin.getInstance(s);
        	if (builtin instanceof HasClassPropagationInfo){
        		CP cp = ((HasClassPropagationInfo)builtin).getMatlabClassPropagationInfo();
        		//System.out.println(cp);
        		if (!parse(cp.toString()).toString().equals(cp.toString())){
        			System.out.println("not indemptotent:\n"+cp+"\n"+parse(cp.toString()));        			
        		}
        		
        		        		
        		/*
            	//get new builtin spec
        		CP cp2 = ((HasClassPropagationInfo)builtin).getMatlabClassPropagationInfo();
        		//compare results
        		//System.out.println(builtin.getName());
        		ArrayList<ClassReference> classes = new ArrayList<ClassReference>();
        		for(ClassReference aClass : PrimitiveClassReference.values()){
        			classes.add(aClass);
        		}
        		classes.add(FunctionHandleClassReference.getInstance());
        		
        		int N = (classes.size()+1);
        		boolean same = true;
        		for (int i = 0;i<N*N*N*N*N;i++){
        			int n = i;
        			ArrayList<ClassReference> arglist = new ArrayList<ClassReference>();
        			while(n > 0){
        				arglist.add(classes.get(Math.max(n%N-1,0)));
        				n = n/N;
        			}
        			//System.out.println(arglist);
        			LinkedList<HashSet<ClassReference>> 
        				result1 = (match(cp,arglist)),
        				result2 = (match(cp2,arglist));
        			//System.out.println(result1+"\n"+result2+"\n--");
        			if (result1 == null?result1 != result2:!result1.equals(result2)){
            			System.out.println("\n\nbuiltin match failed for "+builtin.getName());
            			System.out.println(cp+"\n"+cp2);
        				System.out.println("with args: "+arglist+", producing: \n"+result1+"\n"+result2);
        				same = false;
        				break;
        			}
        		}
        		if (same) System.out.print(".");        		
        		*/
        	}
        }
        System.out.println("\ntest end");
    }
    /**
     * for testing
     */
    static void printMatch(CP tree,ClassReference ... args){
        System.out.print(Arrays.asList(args)+"->");
        System.out.println(match(tree,Arrays.<ClassReference>asList(args)));
    }
    static void printMatchByValue(CP tree,Value<?>... values){
        System.out.print(Arrays.asList(values)+"->");
        //create values
        LinkedList<ClassReference> types = new LinkedList<ClassReference>();
        for (Value<?> v: values){
            types.add(v.getMatlabClass());
        }
        System.out.println(match(tree,types,Arrays.asList(values)));
    }
}




