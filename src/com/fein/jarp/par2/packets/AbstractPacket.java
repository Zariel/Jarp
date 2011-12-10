package com.fein.jarp.par2.packets;

import java.math.BigInteger;

public abstract class AbstractPacket {
	public static BigInteger byteToUint8(byte[] input, int offset) {
		return byteToUint8(input, offset, 8);
	}

	public static BigInteger byteToUint8(byte[] input, int offset, int len) {
		BigInteger num = BigInteger.ZERO;

		for(int i = offset; i < (offset + len); i++) {
			BigInteger n = new BigInteger(
					Integer.toString((input[i] & 0x000000ff) << (i * 8)));
			num = num.or(n);
		}

		return num;
	}

	public static long byteToUint4(byte[] input, int offset) {
		return byteToUint4(input, offset, 4);
	}

	public static long byteToUint4(byte[] input, int offset, int len) {
		long num = 0;

		for(int i = offset; i < (offset + len); i++) {
			num |= ((input[i] & 0x000000ff) << (i * 8));
		}

		return num;

	}

	public static BigInteger getPacketSize(byte[] data) {
		return byteToUint8(data, 8, 8);
	}
}
