package com.fein.jarp;

import com.fein.jarp.par2.ParFile;
import com.fein.jarp.par2.packets.Header;

public class Jarp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParFile par = new ParFile(
				"C:\\Users\\Chris\\Downloads\\fight.club.1999.remastered.repack.720p.bluray.vol000+01.PAR2");

		for(int i = 0; i < par.getNumPackets(); i++) {
			Header head = par.getPacket(i).getHeader();
			System.out.println(head.isValidPar());
			System.out.println(new String(head.getType()));

		}
		// System.out.println("File size = " + par.getSize());
		// System.out.println("Valid = " + head.isValidPar());
		// System.out.println("Length = " + head.getLength());
		// System.out.println("Packet Hash = "
		// + Arrays.toString(head.getPacketMD5()));
		// System.out.println("Set ID = " +
		// Arrays.toString(head.getRecoveryID()));
		// System.out.println("Type = " + new String(head.getType()));
	}
}
