package natlab.tame.valueanalysis.value;

import natlab.tame.classes.reference.*;
import natlab.tame.valueanalysis.ValueSet;
import natlab.tame.valueanalysis.aggrvalue.MatrixValue;
import natlab.tame.valueanalysis.constant.Constant;
import natlab.toolkits.analysis.Mergable;


/**
 * A possible value a variable can take on. This can be
 * - a primitive datum (integer, float, logical, string)
 * - a function handle
 * - a composite value (struct, cell, object)
 * 
 * TODO: How to deal with abstract values that store different things 
 *       - probably more 'precise' abstract values are such because of more advanced 'primitive values'
 * TODO: should these be interned?
 * 
 * TODO: what operations should these support?
 * TODO: should this be iterned?
 * TODO: maybe we should have a special 'colon' value representing ':'
 * 
 * A Value is an abstraction of Matlab values that only requires that it stores
 * its Matlab class. In order for the method signatures to be precise, the
 * value interce is generic in itself -- an actual value implementation V has to
 * fill in itself, so that the method signatures use V instead of Value.
 * 
 * 
 * @author ant6n
 */

public interface Value<V extends Value<V>> extends Mergable<V>{
    public ClassReference getMatlabClass();
    
    /**
     * performs an indexing operation
     * TODO separate according to type (),{},.
     * given that these can be overriden, should all these return Res<D>?
     * -> multiple assignment is possible, tooo!
     * subsasgn should be complex right away, no?
     * the args should always be values
     */
    public ValueSet<V> arraySubsref(Args<V> indizes);
    public Res<V> cellSubsref(Args<V> indizes); //TODO specify nargout? - via args?
    
    //TODO should the subsref be a matrix value? - what if the struct access unknown?
    public ValueSet<V> dotSubsref(String field);
    
    public V arraySubsasgn(Args<V> indizes,V value);
    public V dotSubsasgn(String field,V value);
    public V cellSubsasgn(Args<V> indizes,Args<V> values);
    
    /**
     * returns true if this value has a shape associated with it
     * @return
     */
    public boolean hasShape();
    
    /**
     * returns the shape associated with this value, or null if it doesn't have a shape
     * @return
     * TODO - put in HasShape
     */
    public Shape<?> getShape();
    
    
    /**
     * returns true if there is a constant associated with this value
     * @return
     */
    public boolean isConstant();
    
    /**
     * returns the constant associated with this value, or null if it's not a constant
     * @return
     */
    public Constant getConstant();
    
    
    /**
     * returns a simplified value to be used as a function argument. this allows simplification
     * of flow sets when encountering a call, to reduce the number of generated calling contexts.
     * @param if 'recursive' is true, there should only be finitely many possible values for a given
     * program
     * TODO - how to specify preferred values?
     */
    public V toFunctionArgument(boolean recursive);
}




