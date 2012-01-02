package com.fein.jarp.par2.packets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.fein.jarp.par2.packets.core.CreatorPacket;
import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.InputFileSliceChecksum;
import com.fein.jarp.par2.packets.core.MainPacket;

public abstract class AbstractPacket {
	protected String id;

	private Header header;

	private PacketBody body;

	protected byte[] packet;

	private byte[] packetMd5;

	public AbstractPacket(byte[] packet) {
		this.packet = packet;
	}

	public String getID() {
		if(id == null) {
			id = new String(getRecoveryID());
		}

		return id;
	}

	public byte[] getRecoveryID() {
		return getHeader().getRecoveryID();
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
			case INPUT_FILE_SLICE_CHECKSUM:
				body = new InputFileSliceChecksum(data);
				break;
			case CREATOR:
				body = new CreatorPacket(data);
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
