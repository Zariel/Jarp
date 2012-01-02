package com.fein.jarp.par2.verifier;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.fein.jarp.par2.ParFile;
import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.MainPacket;

public class FileVerifier implements Callable<VerifyStatus> {

	private MainPacket mainPacket;

	private FileDesc fileDesc;

	private ParFile par;

	private CountDownLatch count;

	public static final int kb16 = 16384;

	/**
	 * @param count
	 * @param recoveryFile
	 */
	public FileVerifier(ParFile par, MainPacket mainPacket, FileDesc fileDesc,
			CountDownLatch count) {
		this.par = par;
		this.mainPacket = mainPacket;
		this.fileDesc = fileDesc;
		this.count = count;
	}

	@Override
	public VerifyStatus call() throws Exception {
		// First check if it exists ..
		File file = new File(par.getFileRootPath() + "/"
				+ fileDesc.getFileName());

		if(!file.exists()) {
			return finished(VerifyStatus.FILE_NO_EXISTS);
		}

		if(!fileDesc.isFileIdValid()) {
			return finished(VerifyStatus.FILE_ID_INCORRECT);
		}

		try(InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			is.mark(0);

			if(!isMd516kValid(is)) {
				return finished(VerifyStatus.FILE_MD6_16K_INCORRECT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return finished(VerifyStatus.FILE_CORRECT);
	}

	private VerifyStatus finished(VerifyStatus status) {
		count.countDown();

		return status;
	}

	public boolean isMd516kValid(InputStream is) throws IOException {
		byte[] buf = new byte[kb16];

		int read = 0;
		int offset = 0;

		while((read = is.read(buf, offset, kb16)) < kb16) {
			offset += read;
		}

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(buf);

			return Arrays.equals(digest, fileDesc.getFileMd516k());
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
	}
}
