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

        JMeterVariables vars = getVariables();

        Double sum = 0D;
        String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();

        for (int i = 0; i < values.length - 1; i++) {
            sum += Double.parseDouble(((CompoundVariable) values[i]).execute());
        }

        try {
            sum += Double.parseDouble(varName);
            varName = null; // there is no variable name
        } catch (NumberFormatException ignored) {
        }

        String totalString = Double.toString(sum);
        if (vars != null && varName != null && varName.length() > 0) {// vars will be null on TestPlan
            vars.put(varName, totalString);
        }
        
    	try {
			System.out.println(com.wirecard.acqp.two.MsgAccessoryImpl.readFieldValue( "16010200D600000079542500000000000000000000000400F22464810CE08016000000400000000010400601FFFFFF0013000000000000002238012812282961059615110780080401205906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F583F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C10978050900000005104000000000000000041402859374059607A0000000002501010000000000000000000000000000000000000000", "VISA", "62.2" ));
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
