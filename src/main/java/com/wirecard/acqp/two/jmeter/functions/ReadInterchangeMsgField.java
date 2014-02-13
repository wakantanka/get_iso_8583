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
import org.jpos.iso.ISOException;

import com.wirecard.acqp.two.MsgAccessoryImpl;


/**
 * Provides a readFieldValue function that parses Interchange Messages and
 * return Fieldvalue from given fieldPath .

 * @author Wirecard AG (c) 2014. All rights reserved.
 */
public final class ReadInterchangeMsgField extends AbstractFunction {

	private static final List<String> DESC = new LinkedList<String>();
	private static final String KEY = "__readInterchangeMsgField";

	static {
		DESC.add("the twoInput");
		DESC.add("CardSchema");
		DESC.add("FieldPath");
	}
	private Object[] values;

	/**
	 * No-arg constructor.
	 */
	public ReadInterchangeMsgField() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(final SampleResult previousResult,
			final Sampler currentSampler) throws InvalidVariableException {

		// JMeterVariables vars = getVariables();

		String totalString = null;

		try {
			totalString = MsgAccessoryImpl.readFieldValue(
					((CompoundVariable) values[0]).execute().trim(),
					((CompoundVariable) values[1]).execute().trim(),
					((CompoundVariable) values[2]).execute().trim());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ISOException e) {
			e.printStackTrace();
		}

		return totalString;

	}

	/** {@inheritDoc} */
	@Override
	public synchronized void setParameters(
			final Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
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
		return DESC;
	}
}
