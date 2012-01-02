package com.fein.jarp;

import com.fein.jarp.par2.ParFile;
import com.fein.jarp.par2.packets.AbstractPacket;
import com.fein.jarp.par2.verifier.Par2Verifier;

public class Jarp {

	public static final int NUM_CPUS = Runtime.getRuntime()
			.availableProcessors();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParFile par = new ParFile(
				"D:\\dev\\jarp\\test\\Bored to Death - 1x01 - Stockholm Syndrome.avi.par2");

		AbstractPacket p = null;
		// int packets = par.getNumPackets();
		//
		// for(int i = 0; i < packets; i++) {
		// p = par.getPacket(i);
		//
		// if(p.getPacketType() == PacketType.FILE_DESC) {
		// FileDesc body = (FileDesc) p.getBody();
		// System.out.println(body.getFileName());
		// } else if(p.getPacketType() == PacketType.MAIN_PACKET) {
		// MainPacket m = (MainPacket) p.getBody();
		// }
		// }

		Par2Verifier v = new Par2Verifier(par);

		try {
			v.verifyFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(par.isPacketsValid());
		System.out.println(par.getNumFiles());
		System.out.println(par.getCreator());
	}
}
