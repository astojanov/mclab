/* this represents a general builtin function
 * 
 * It can be used for 
 *  - type inference/propagation/checking
 *  - constant propagation
 *  - code generation
 * 
 * for every program, there should only be one builtin
 */
// use call.getSignature() insteed of Signature 
package fir.builtin;
import java.util.*;
import fir.table.*;
import fir.type.*;
import fir.ast.*;
import fir.codegen.*;

public abstract class Builtin {
	
	//queries (information about the function)
	public boolean isElemental(){return false;}
	public boolean isFortranIntrinsicd(){return false;}
	public abstract String getName();
	//..need more here? TODO
	
	//is this function defined for this input?
	public abstract boolean isDefined(Type[] inputTypes); //false if input is not possible (i.e. sin(logical))
	public abstract boolean isDefined(Type[] inputTypes,int numberOfOutputs);//false if types of input and number of output/input are not possible.
	public abstract boolean isDefined(Call call); //also includes output types
	
	//get return type, type propagation, type checking
	public abstract Type getReturnType(Type[] inputTypes); //null if complicated, or not possible
	public abstract Type[] getReturnTypes(Type[] inputTypes,int numberOfOutputs);
	
	//constant propagation -- returns null if propagation not possible
	public abstract Constant getResult(Constant[] inputs);
	public abstract Constant[] getResults(Constant[] inputs,int numberOfOutputs);
	
	//generate code
	//not that some generate code for expressions, other for statements
	public abstract void generate(ExpressionInterfacer interfacer,Call call,Vector<InternalVar> variable); //a created expression has a known type
	public abstract void generate(StatementInterfacer interfacer,Call call,Vector<InternalVar> variable);
	public abstract void generateWithConstants(ExpressionInterfacer interfacer,Call call,Vector<InternalVar> variable,Constant[] constants);	
	public abstract void generateWithConstants(StatementInterfacer interfacer,Call call,Vector<InternalVar> variable,Constant[] constants);	
}

