package com.fein.jarp.par2.packets.core;

import java.util.Arrays;

import com.fein.jarp.par2.packets.PacketBody;

public class InputFileSliceChecksum extends PacketBody {
	private byte[] fileId;

	public InputFileSliceChecksum(byte[] body) {
		super(body);
	}

	public byte[] getFileId() {
		if(fileId == null) {
			fileId = Arrays.copyOfRange(body, 0, 16);
		}

		return fileId;
	}
}
