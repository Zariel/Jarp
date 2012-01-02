package com.fein.jarp.par2.packets.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fein.jarp.par2.packets.PacketBody;
import com.fein.jarp.util.ByteUtil;

public class MainPacket extends PacketBody {

	private int numRecovFiles = 0;

	private int sliceSize = 0;

	private List<String> recovFiles;

	private List<String> nonRecovFiles;

	private int numNonRecovFiles;

	public MainPacket(byte[] body) {
		super(body);
	}

	public int getNumFiles() {
		if(numRecovFiles == 0) {
			numRecovFiles = (int) ByteUtil.byteToUint4(body, 8);
		}

		return numRecovFiles;
	}

	public int getSliceSize() {
		if(sliceSize == 0) {
			sliceSize = (int) ByteUtil.byteToUint8(body, 0).longValue();
		}

		return sliceSize;
	}

	public List<String> getRecovFileIds() {
		if(recovFiles == null) {
			recovFiles = new ArrayList<String>(getNumFiles());
			for(int i = 0; i < numRecovFiles; i++) {
				byte[] fileId = Arrays.copyOfRange(body, 12 + (i * 16),
						12 + (i * 16) + 16);
				recovFiles.add(new String(fileId));
			}
		}

		return recovFiles;
	}

	public List<String> getNonRecovFileIds() {
		if(nonRecovFiles == null) {
			nonRecovFiles = new ArrayList<String>(getNumNonRecovFiles());
			int offset = 12 + (getNumFiles() * 16);

			for(int i = 0; i < numNonRecovFiles; i++) {
				byte[] fileId = Arrays.copyOfRange(body, offset + (i * 16),
						offset + (i * 16) + 16);
				nonRecovFiles.add(new String(fileId));
			}
		}

		return nonRecovFiles;
	}

	private int getNumNonRecovFiles() {
		if(numNonRecovFiles == 0) {
			numNonRecovFiles = (body.length - 12 - (getNumFiles() * 16)) / 16;
		}

		return numNonRecovFiles;
	}

}
