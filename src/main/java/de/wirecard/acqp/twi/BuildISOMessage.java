package de.wirecard.acqp.twi;

import java.io.IOException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
 
public class BuildISOMessage {
 
	public static void main(String[] args) throws IOException, ISOException {
		// Create Packager based on XML that contain DE type
		GenericPackager packager = new GenericPackager("basic.xml");
 
		// Create ISO Message
		ISOMsg isoMsg = new ISOMsg();
		isoMsg.setPackager(packager);
		isoMsg.setMTI("0100");
		isoMsg.set(3, "000000");
		isoMsg.set(4, "000000110030");
		isoMsg.set(7, "0120112051");
		isoMsg.set(11, "588321");
		isoMsg.set(14, "1510");
		isoMsg.set(18, "742");
		isoMsg.set(19, "276");
		isoMsg.set(22, "120");
		isoMsg.set(25, "59");
		isoMsg.set(32, "466255");
		isoMsg.set(37, "402011588321");
		isoMsg.set(41, "a0001470");
		isoMsg.set(42, "1001a0001470");
		isoMsg.set(43, "GoetheonMainJohannesburg DE");
		isoMsg.set(48, "fsdgdsfgsdfgsdfgsdg");
		isoMsg.set(49, "978");
		isoMsg.set(60, "0900000005");
 
		// print the DE list
		logISOMsg(isoMsg);
 
		// Get and print the output result
		byte[] data = isoMsg.pack();
		System.out.println("RESULT : " + new String(data));
	}
 
	private static void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
 
	}
 
}