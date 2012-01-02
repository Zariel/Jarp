package com.fein.jarp.par2.packets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class PacketBody {
	protected final byte[] body;

	protected byte[] bodyMd5;

	public PacketBody(byte[] body) {
		this.body = body;
	}

	public byte[] getRecoveryID() {
		return null;
	}

	/**
	 * Returns the MD5 sum of the body of the packet.
	 * 
	 * @return
	 */
	public byte[] getMd5() {
		if(bodyMd5 == null) {
			int off = Header.headerSize - 32;
			byte[] data = Arrays.copyOfRange(body, 0, body.length);

			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				bodyMd5 = md.digest(data);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}

		return bodyMd5;
	}
}
