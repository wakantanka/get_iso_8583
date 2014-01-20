/**
 * 
 */
package de.wirecard.acqp.twi;

import java.io.IOException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;


/**
 * @author jan
 *
 */
	public class ParseISOMessage {
	 
		public static void main(String[] args) throws IOException, ISOException {
			// Create Packager based on XML that contain DE type
			GenericPackager packager = new GenericPackager("basic.xml");
	 
			// Print Input Data
//			String data = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR021ABCDEFGHIJ 1234567890";
//			String data = "01003224648108E080100000000000001100300120112051588321151007422761205906466255402011588321a00014701001a0001470   GoetheonMainJohannesburg DE             9780100900000005 1234567890";
			String data = "0100723C440188E1800819540562000000000001400000000000000050501201303095892781303090120151154118120601340106200350012014549354820000001D9C5E3D3820000001404040C3C3C240E3140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5060E361050000192035C5C5C42070103212432891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07E9780211025100006000591D7C1D512";

			System.out.println("DATA : " + data);
	 
			// Create ISO Message
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.unpack(data.getBytes());
	 
			// print the DE list
			logISOMsg(isoMsg);
		}
	 
		private static void logISOMsg(ISOMsg msg) {
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1; i<=msg.getMaxField();i++) {
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
