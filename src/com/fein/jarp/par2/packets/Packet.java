package com.fein.jarp.par2.packets;

import java.util.Arrays;

public class Packet {
	private Header header;

	private byte[] body;

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

	public byte[] getBody() {
		if(body == null) {
			int size = Header.headerSize;
			body = Arrays.copyOfRange(packet, size, getHeader().getLengthInt()
					- size);
		}

		return body;
	}
}
