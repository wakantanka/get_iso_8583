package com.wirecard.acqp.two.jmeter.samplers;

import java.io.UnsupportedEncodingException;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.jpos.iso.ISOException;

import com.wirecard.acqp.two.MsgAccessoryImpl;

/**
 * 
 * @author Wirecard AG (c) 2014. All rights reserved.
 */

public final class InterchangeAccessorySampler extends AbstractSampler
		implements TestBean, Interruptible {

	private static final long serialVersionUID = 6455497354844447057L;
	public static final String IS_SUCCESSFUL = "SUCCESFULL";
	public static final String CARD_SCHEMA = "CARD_SCHEMA";
	public static final String FIELD_PATH = "FIELD_PATH";
	public static final String TWO_INPUT = "TWO_INPUT";

	public SampleResult sample(final Entry e) {
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
		try {
			fieldValue = MsgAccessoryImpl.readFieldValue(twoInput, cardSchema,
					fieldPath);
//			res.setResponseMessage("Value of Field : " + fieldPath);
			res.setResponseCode("200");
			res.setResponseOK();
			res.setResponseData(fieldValue, null);
			setSuccessful(true);
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			return setSampleResultErrorState(res, e1);
//			
//		} catch (ISOException e2) {
//			e2.printStackTrace();
//			return	setSampleResultErrorState(res, e2);
		} catch (Exception e3) {
			e3.printStackTrace();
			setSuccessful(false);
			res.setResponseCode("500");
			res.setErrorCount(res.getErrorCount() + 1);
			res.setResponseMessage("ERROR " + e3.getMessage());
		} finally {
			res.setDataType(SampleResult.TEXT);
			res.setSuccessful(isSuccessfull());
			res.sampleEnd();
		}
		return res;

	}
 
	public void setSuccessful(final boolean selected) {
		// selected=true;
		setProperty(IS_SUCCESSFUL, selected);
	}

	public void setCardSchema(final String text) {
		setProperty(CARD_SCHEMA, text);
	}

	public void setFieldPath(final String text) {
		setProperty(FIELD_PATH, text);
	}

	public void setTwoInput(final String text) {
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
