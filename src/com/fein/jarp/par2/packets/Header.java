package com.fein.jarp.par2.packets;

import java.math.BigInteger;
import java.util.Arrays;

public class Header extends AbstractPacket {
	public static int headerSize = 64;

	private byte[] header;

	private byte[] magicSequence;

	private byte[] packetMD5;

	private byte[] recoveryId;

	private byte[] type;

	private BigInteger length;

	public static final byte[] magicHeader = new byte[] {
			'P', 'A', 'R', '2', '\0', 'P', 'K', 'T'
	};

	public Header(byte[] header) {
		this.header = header;
	}

	public BigInteger getLength() {
		if(length == null) {
			length = getPacketSize(header);
		}

		return length;
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

	public byte[] getType() {
		if(type == null) {
			type = Arrays.copyOfRange(getHeader(), 48, 64);
		}

		return type;
	}

}
