/**
 * 
 */
package de.wirecard.acqp.twi;

import java.io.IOException;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

/**
 * @author jan
 * 
 */
public class ParseISOMessage {

	public static void main(String[] args) throws IOException, ISOException  {
		// Create Packager based on XML that contain DE type
		GenericPackager packager = new GenericPackager("basic.xml");
//		ISOPackager packager = new GenericPackager("basic.xml");
//		GenericPackager packager = new GenericPackager("base1.xml");

		// Print Input Data
		// String data =
		// "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR021ABCDEFGHIJ 1234567890";
		// String data =
		// "01003224648108E080100000000000001100300120112051588321151007422761205906466255402011588321a00014701001a0001470   GoetheonMainJohannesburg DE             9780100900000005 1234567890";
//		String data =
//				"0100723C440188E1800819540562000000000001400000000000000050501201303095892781303090120151154118120601340106200350012014549354820000001D9C5E3D3820000001404040C3C3C240E3140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5060E361050000192035C5C5C42070103212432891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07E9780211025100006000591D7C1D512";
//		 String data =
//		 "0100723C440188E0000019540562000000000001400000000000000050501201303095892781303090120151154118120601340106200350012014549354820000001D9C5E3D3820000001404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5060E361050000192035C5C5C42070103212432891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07E9780211025100006000591D7C1D512";
		 // --GEHT!!, ohne Feld 48
//		 String data =
//				 "0100723C440188E080001954056200000000000140000000000000005050120130309589278130309012015115411812060134010620035001201454935482000001D9C5E3D38200001404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5060E361050000192035C5C5C42070103212432891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07E9780211025100006000591D7C1D512";
//		ausslassung Feld 48 führt zu fehlerhaften wert in 49
//		 String data = 
//				 "16010200B700000079542500000000000000000000000100F224648108E08012000000000000000410437835FFFFFF0013000000000000002238011709563958491215110780080401200806428104F4F0F1F7F0F9F5F8F4F9F1F283F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C109780500000000010580000000000E0040000000000000F1F140C6C6C6";
		
		 String data = 
				 "0100723C440188E0800019540562000000000001400000000000000050501201303095892781303090120151154118120601340106200350012014549354820000001D9C5E3D3820000001404040C3C3C240E3140E28899A340D581948540404040404040C3C3C240E3140E28899A340D340D7C1D5060E361050000192035C5C5C42070103212432891C982A8846E38581889492C1C2C5C1C1C1C699D894A8E7A69407E9780211025100006000591D7C1D512";
	
		
		System.out.println("DATA : " + data);

		// Create ISO Message
		ISOMsg isoMsg = new ISOMsg();
		isoMsg.setPackager(packager);
		isoMsg.unpack(data.getBytes());
		
//		 Logger logger = new Logger();
//		 logger.addListener (new SimpleLogListener(System.out));
//		 ((LogSource)packager).setLogger(logger, "debug");
		
		 Logger logger = new Logger(); 
		 logger.addListener (new SimpleLogListener (System.out)); 
		  ((LogSource)packager).setLogger(logger, "debug"); 
		 
		// print the DE list
		logISOMsg(isoMsg);
	}

	private static void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-" +  msg.getComponent(i).getKey().toString() + " : "
							+ msg.getComponent(i).getValue().toString() + "\t\t     value : " + msg.getComponent(i).getBytes());
				//"\t\t     Children? : " + msg.getComponent(i).getChildren().size()
				}
				
			}
//			ISOMsg field127 = (ISOMsg)msg.getComponent(43).getComposite();
//			System.out.println("127.3=" + field127.getString(0));
			
			String field43 =  msg.getString("43.3");
			System.out.println("43.3=" + field43);
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}

	}

}
