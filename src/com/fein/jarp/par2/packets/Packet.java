package com.fein.jarp.par2.packets;

import java.util.Arrays;

public class Packet {
	private Header header;

	private byte[] packet;

	public Packet(byte[] packet) {
		this.packet = packet;
	}

	public Header getHeader() {
		if(header == null) {
			header = new Header(
					Arrays.copyOfRange(packet, 0, Header.headerSize));
		}

		return header;
	}
}
