package com.fein.jarp.par2.packets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.MainPacket;

public class Packet {
	private Header header;

	private PacketBody body;

	private byte[] packet;

	private byte[] packetMd5;

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

	public byte[] getPacketMd5() {
		if(packetMd5 == null) {
			int off = Header.headerSize - 32;
			byte[] data = Arrays.copyOfRange(packet, off, packet.length);

			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				packetMd5 = md.digest(data);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}

		return packetMd5;
	}

	public boolean isPacketMd5Valid() {
		return Arrays.equals(getPacketMd5(), header.getPacketMD5());
	}
}
