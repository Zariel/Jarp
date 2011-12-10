package com.fein.jarp;

import com.fein.jarp.par2.ParFile;
import com.fein.jarp.par2.packets.Packet;

public class Jarp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParFile par = new ParFile(
				"C:\\Users\\Chris\\Downloads\\fight.club.1999.remastered.repack.720p.bluray.vol000+01.PAR2");

		Packet p = null;
		int packets = par.getNumPackets();

		for(int i = 0; i < packets; i++) {
			p = par.getPacket(i);

			System.out.println(p.getHeader().getPacketType());
		}
	}
}
