/**
 * 
 */
package com.wirecard.acqp.twi;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

/**
 * @author jan.wahler Copyright Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public class MsgAccessoryImpl implements IMsgAccessory {
	GenericPackager packager = null;
	AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();

	public MsgAccessoryImpl() {
		super();
	}

	public MsgAccessoryImpl(String msg, CardScheme scheme) throws ISOException {
		packager = new GenericPackager(scheme.getPath());
		throw new NotYetImpementedException();

	}

	public String getFieldValue(String fieldNo) {
		throw new NotYetImpementedException();

	}

	// TODO Exception behandeln
	public String getFieldValue(String msg, CardScheme scheme, String fieldPath)
			throws ISOException {
		packager = new GenericPackager(scheme.getPath());
		
//		Logger logger = new Logger();
//		logger.addListener(new SimpleLogListener(System.out));
//		((LogSource) packager).setLogger(logger, "debug");


		ISOMsg isoMsg = new ISOMsg();
		
		
		isoMsg.setPackager(packager);
		
		
		byte[] dataPart = asciiIn.uninterpret(msg.substring(44, msg.length()).getBytes(), 0, msg.substring(44, msg.length()).length()/2);
		isoMsg.unpack(dataPart);
		
		MsgUtils.logISOMsg(isoMsg);
		
		 if ( isoMsg.getValue(fieldPath) instanceof byte[]) {
			 return  	 Hex.encodeHexString((byte[]) isoMsg.getComponent(fieldPath).getValue()).toString();
		}
		 else {
				return isoMsg.getString(fieldPath);
		}
	
	}

	
	
	@SuppressWarnings("serial")
	class NotYetImpementedException extends java.lang.RuntimeException {

		public NotYetImpementedException() {
			super("NotYetImpementedException");
		}

	};

}
