/**
 * 
 */
package com.wirecard.acqp.two.jmeter.functions;


import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.jpos.iso.ISOException;

import com.wirecard.acqp.two.MsgAccessoryImpl;

/**
 * @author wirecard
 *
 */

/**
 * Provides a readFieldValue function that parses Interchange Messages and return Fieldvalue from given fieldPath .
 */
public class ReadInterchangeMsgField extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__readInterchangeMsgField"; 

    static {
        desc.add("the twoInput"); 
        desc.add("CardSchema"); 
        desc.add("FieldPath"); 
    }
    private Object[] values;

    /**
     * No-arg constructor.
     */
    public ReadInterchangeMsgField() {
    	System.out.println("############################# INIT ReadInterchangeMsgField #############################");

    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
    	System.out.println("############################# EXEC ReadInterchangeMsgField #############################");
    	System.out.println("############################# EXEC " + ((CompoundVariable) values[0]).execute().trim()  + " #############################");
    	System.out.println("############################# EXEC " + ((CompoundVariable) values[1]).execute().trim()  + " #############################");
    	System.out.println("############################# EXEC " + ((CompoundVariable) values[2]).execute().trim()  + " #############################");

        JMeterVariables vars = getVariables();


        String totalString = null;
        
    	try {
    		totalString = MsgAccessoryImpl.readFieldValue(((CompoundVariable) values[0]).execute().trim(), ((CompoundVariable) values[1]).execute().trim(), ((CompoundVariable) values[2]).execute().trim() );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ISOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return totalString;

    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 2);
        values = parameters.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    public List<String> getArgumentDesc() {
        return desc;
    }
}
