package com.fein.jarp.par2.packets;

import java.math.BigInteger;
import java.util.Arrays;

import com.fein.jarp.util.ByteUtil;

public class Header {
	public static int headerSize = 64;

	private byte[] header;

	private byte[] magicSequence;

	private byte[] packetMD5;

	private byte[] recoveryId;

	private String type;

	private PacketType packetType;

	private BigInteger length;

	public static final byte[] magicHeader = new byte[] {
			'P', 'A', 'R', '2', '\0', 'P', 'K', 'T'
	};

	public Header(byte[] header) {
		this.header = header;
	}

	public BigInteger getLength() {
		if(length == null) {
			length = ByteUtil.getPacketSize(header);
		}

		return length;
	}

	public int getLengthInt() {
		return (int) getLength().longValue();
	}

	public byte[] getHeader() {
		return header;
	}

	public byte[] getMagicSequence() {
		if(magicSequence == null) {
			magicSequence = Arrays.copyOf(getHeader(), 8);
		}

		return magicSequence;
	}

	public boolean isValidPar() {
		for(int i = 0; i < 8; i++) {
			if(header[i] != magicHeader[i]) {
				return false;
			}
		}

		return true;
	}

	public byte[] getPacketMD5() {
		if(packetMD5 == null) {
			packetMD5 = Arrays.copyOfRange(getHeader(), 16, 32);
		}

		return packetMD5;
	}

	public byte[] getRecoveryID() {
		if(recoveryId == null) {
			recoveryId = Arrays.copyOfRange(getHeader(), 32, 48);
		}

		return recoveryId;
	}

	public String getType() {
		if(type == null) {
			type = new String(Arrays.copyOfRange(getHeader(), 48, 64));
		}

		return type;
	}

	public PacketType getPacketType() {
		if(packetType == null) {
			switch(getType()) {
			case "PAR 2.0\0RecvSlic":
				packetType = PacketType.REOCV_SLICE;
				break;
			case "PAR 2.0\0Creator\0":
				packetType = PacketType.CREATOR;
				break;
			case "PAR 2.0\0IFSC\0\0\0\0":
				packetType = PacketType.INPUT_FILE_SLICE_CHECKSUM;
				break;
			case "PAR 2.0\0FileDesc":
				packetType = PacketType.FILE_DESC;
				break;
			case "PAR 2.0\0Main\0\0\0\0":
				packetType = PacketType.MAIN_PACKET;
				break;
			}
		}

		return packetType;
	}

}
