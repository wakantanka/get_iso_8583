package com.wirecard.acqp.two.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;

/**
 * 
 * @author Wirecard AG (c) 2014. All rights reserved.
 */

public class InterchangeAccessorySampler extends AbstractSampler implements
		TestBean, Interruptible {

	private static final long serialVersionUID = 6455497354844447057L;
//	public static final String IS_SUCCESSFUL = "SUCCESFULL";
	public static final String CARD_SCHEMA = "CARD_SCHEMA";
	public static final String FIELD_PATH = "FIELD_PATH";
//	public static final String RESPONSE_DATA = "RESPONSE_DATA";
	public static final String TWO_INPUT = "TWO_INPUT";
//	public static final String RESPONSE_TIME = "RESPONSE_TIME";
//	public static final String LATENCY = "LATENCY";
//	public static final String IS_WAITING = "WAITING";

	public SampleResult sample(Entry e) {
		SampleResult res 
		=new SampleResult();
			res.sampleStart();
		
//		if (isSimulateWaiting()) {
//			res = new SampleResult();
//			try {
//				Thread.sleep(getResponseTime());
//			} catch (InterruptedException ignored) {
//			}
//			res.sampleEnd();
//		} else {
//			res = new SampleResult(System.currentTimeMillis(),
//					getResponseTime());
//		}

		res.setSampleLabel(getName());

		// source data
		res.setSamplerData(getTwoInput());

		// response code
//		res.setResponseCode(getCardSchema());
		res.setResponseMessage(getResponseMessage());
		res.setSuccessful(isSuccessfull());

		// responde data
		// res.setResponseData(getResponseData().getBytes());

		// res.setLatency(getLatency());
		res.setDataEncoding("UTF-8");
//		res.setSamplerData("samplerRequsData");
        res.setResponseData("response", null);
        res.setDataType(SampleResult.TEXT);

        res.setResponseCode("200");
        res.setResponseOK();
        res.sampleEnd();
        
		return res;
	}

//	public void setSuccessful(boolean selected) {
//		// selected=true;
//		setProperty(IS_SUCCESSFUL, selected);
//	}

//	public void setSimulateWaiting(boolean selected) {
//		setProperty(IS_WAITING, selected);
//	}

	public void setCardSchema(String text) {
		setProperty(CARD_SCHEMA, text);
	}

	public void setFieldPath(String text) {
		setProperty(FIELD_PATH, text);
	}

//	public void setResponseData(String text) {
//		setProperty(RESPONSE_DATA, text);
//	}

	public void setTwoInput(String text) {
		setProperty(TWO_INPUT, text);
	}

	/**
	 * @return the successfull
	 */
	public boolean isSuccessfull() {
		return true;
//		return getPropertyAsBoolean(IS_SUCCESSFUL);
	}

//	public boolean isSimulateWaiting() {
//		return getPropertyAsBoolean(IS_WAITING);
//	}

	/**
	 * @return the responseCode
	 */
	public String getCardSchema() {
		return getPropertyAsString(CARD_SCHEMA);
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return getPropertyAsString(FIELD_PATH);
	}

	/**
	 * @return the responseData
	 */
//	public String getResponseData() {
//		return getPropertyAsString(RESPONSE_DATA);
//	}

	public String getTwoInput() {
		return getPropertyAsString(TWO_INPUT);
	}

//	public int getResponseTime() {
//		int time = 0;
//		try {
//			time = Integer.valueOf(getPropertyAsString(RESPONSE_TIME));
//		} catch (NumberFormatException ignored) {
//		}
//		return time;
//	}

//	public int getLatency() {
//		int time = 0;
//		try {
//			time = Integer.valueOf(getPropertyAsString(LATENCY));
//		} catch (NumberFormatException ignored) {
//		}
//		return time;
//	}

//	public void setResponseTime(String time) {
//		setProperty(RESPONSE_TIME, time);
//	}

//	public void setLatency(String time) {
//		setProperty(LATENCY, time);
//	}

	public boolean interrupt() {
		Thread.currentThread().interrupt();
		return true;
	}
}
