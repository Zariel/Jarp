package com.fein.jarp.par2.packets;

import java.util.Arrays;

import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.MainPacket;

public class Packet {
	private Header header;

	private PacketBody body;

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

	public PacketType getPacketType() {
		return getHeader().getPacketType();
	}

	public PacketBody getBody() {
		if(body == null) {
			int size = Header.headerSize;
			byte[] data = Arrays.copyOfRange(packet, size, getHeader()
					.getLengthInt());

			switch(getPacketType()) {
			case FILE_DESC:
				body = new FileDesc(data);
				break;
			case MAIN_PACKET:
				body = new MainPacket(data);
				break;
			}
		}

		return body;
	}
}
