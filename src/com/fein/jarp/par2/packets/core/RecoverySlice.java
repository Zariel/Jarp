package com.fein.jarp.par2.packets.core;

import java.util.Arrays;

import com.fein.jarp.par2.packets.PacketBody;
import com.fein.jarp.util.ByteUtil;

public class RecoverySlice extends PacketBody {

	private long exponent;

	private byte[] data;

	public RecoverySlice(byte[] body) {
		super(body);
	}

	public long getExponent() {
		if(exponent == 0) {
			exponent = ByteUtil.byteToUint4(body, 0);
		}

		return exponent;
	}

	public byte[] getRecoveryData() {
		if(data == null) {
			data = Arrays.copyOfRange(body, 4, body.length);
		}

		return data;
	}
}
