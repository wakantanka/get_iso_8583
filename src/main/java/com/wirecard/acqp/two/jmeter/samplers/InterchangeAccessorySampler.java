package com.wirecard.acqp.two.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;

import com.wirecard.acqp.two.MsgAccessoryImpl;

/**
 * 
 * @author Wirecard AG (c) 2014. All rights reserved.
 */

public class InterchangeAccessorySampler extends AbstractSampler implements
		TestBean, Interruptible {

	private static final long serialVersionUID = 6455497354844447057L;
	public static final String IS_SUCCESSFUL = "SUCCESFULL";
	public static final String CARD_SCHEMA = "CARD_SCHEMA";
	public static final String FIELD_PATH = "FIELD_PATH";
	public static final String TWO_INPUT = "TWO_INPUT";

	public SampleResult sample(Entry e) {
		SampleResult res = new SampleResult();
		res.sampleStart();

		String fieldValue = null;
		setSuccessful(false);

		res.setSampleLabel(getName());

		// source data
		StringBuilder sb = new StringBuilder();
		res.setDataEncoding("UTF-8");
		String twoInput = getTwoInput().trim();
		String cardSchema = getCardSchema().trim();
		String fieldPath = getFieldPath().trim();

		sb.append("Two Input: ");
		sb.append(twoInput + "\n");
		sb.append("Card Scheme: ");
		sb.append(cardSchema + "\n");
		sb.append("Field Path: ");
		sb.append(fieldPath + "\n");
		System.out.println(sb.toString());
		res.setSamplerData(sb.toString());

		// responde data
		res.setResponseMessage("Value of Field" + fieldPath);
		try {
			fieldValue = MsgAccessoryImpl.readFieldValue(twoInput,
					cardSchema, fieldPath);
			res.setResponseCode("200");
			res.setResponseOK();
			res.setResponseData(fieldValue, null);
			setSuccessful(true);
		} catch (Exception e2) {
			setSuccessful(false);
			throw e2;
		} finally {
			res.setDataType(SampleResult.TEXT);

			res.setSuccessful(isSuccessfull());

			res.sampleEnd();

			return res;

		}

	}

	public void setSuccessful(boolean selected) {
		// selected=true;
		setProperty(IS_SUCCESSFUL, selected);
	}

	public void setCardSchema(String text) {
		setProperty(CARD_SCHEMA, text);
	}

	public void setFieldPath(String text) {
		setProperty(FIELD_PATH, text);
	}

	public void setTwoInput(String text) {
		setProperty(TWO_INPUT, text);
	}

	/**
	 * @return the successfull
	 */
	public boolean isSuccessfull() {
		// return true;
		return getPropertyAsBoolean(IS_SUCCESSFUL);
	}

	/**
	 * @return the cardSchema
	 */
	public String getCardSchema() {
		return getPropertyAsString(CARD_SCHEMA);
	}

	/**
	 * @return the fieldPath
	 */
	public String getFieldPath() {
		return getPropertyAsString(FIELD_PATH);
	}

	/**
	 * @return the twoInput
	 */
	public String getTwoInput() {
		return getPropertyAsString(TWO_INPUT);
	}

	public boolean interrupt() {
		Thread.currentThread().interrupt();
		return true;
	}
}
