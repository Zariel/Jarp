package com.fein.jarp.par2.packets.core;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.fein.jarp.par2.packets.PacketBody;
import com.fein.jarp.util.ByteUtil;

public class FileDesc extends PacketBody {
	private byte[] fileID;

	private String fileIdString;

	private String name;

	private byte[] fileMd5;

	private byte[] fileMd516k;

	private BigInteger fileLength;

	private static final Charset ascii = Charset.forName("ASCII");

	/**
	 * included for the md5 summing.
	 */
	private byte[] fileLengthBytes;

	public FileDesc(byte[] body) {
		super(body);
	}

	public String getFileIdString() {
		if(fileIdString == null) {
			fileIdString = new String(getFileID());
		}

		return fileIdString;
	}

	public byte[] getFileID() {
		if(fileID == null) {
			fileID = Arrays.copyOfRange(body, 0, 16);
		}

		return fileID;
	}

	public byte[] getFileMd5() {
		if(fileMd5 == null) {
			fileMd5 = Arrays.copyOfRange(body, 16, 32);
		}

		return fileMd5;
	}

	public byte[] getFileMd516k() {
		if(fileMd516k == null) {
			fileMd516k = Arrays.copyOfRange(body, 32, 48);
		}

		return fileMd516k;
	}

	public String getFileName() {
		if(name == null) {
			int end = body.length;

			for(int i = body.length - 1; i > (body.length - 4); i--) {
				if(body[i] != 0x0) {
					end = i + 1;
					break;
				}
			}
			
			byte[] data = Arrays.copyOfRange(body, 56, end);

			name = new String(data, ascii);
		}

		return name;
	}

	public BigInteger getFileLength() {
		if(fileLength == null) {
			fileLength = ByteUtil.byteToUint8(body, 48);
		}

		return fileLength;
	}

	private byte[] getFileLengthBytes() {
		if(fileLengthBytes == null) {
			fileLengthBytes = Arrays.copyOfRange(body, 48, 56);
		}

		return fileLengthBytes;
	}

	public boolean isFileIdValid() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(getFileMd516k());
			md.update(getFileLengthBytes());
			byte[] digest = md.digest(getFileName().getBytes(ascii));

			return Arrays.equals(digest, getFileID());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return false;
	}
}
