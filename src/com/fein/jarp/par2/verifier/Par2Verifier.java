package com.fein.jarp.par2.verifier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fein.jarp.Jarp;
import com.fein.jarp.par2.ParFile;
import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.MainPacket;

public class Par2Verifier {

	private ParFile par;

	ExecutorService threadPool;

	ExecutorCompletionService<VerifyStatus> verifyPool;

	public Par2Verifier(ParFile par) {
		this.par = par;

		threadPool = Executors.newFixedThreadPool(Jarp.NUM_CPUS);

		verifyPool = new ExecutorCompletionService<VerifyStatus>(threadPool);
	}

	public void verifyFiles() throws InterruptedException, Exception {
		if(threadPool.isShutdown()) {
			throw new Exception(
					"Threadpool shutdown. Create new instance to verify.");
		}

		// get all the files and create some objects
		MainPacket m = par.getMainPacket();

		FileDesc f = null;

		CountDownLatch count = new CountDownLatch(m.getNumFiles());

		for(String fileId : m.getRecovFileIds()) {
			// System.out.println(Arrays.toString(fileId) + " "
			// + par.getFileDesc(fileId));

			f = par.getFileDesc(fileId);

			if(f == null) {
				// System.out.println("FileID not in recoverable set");
				continue;
			}

			verifyPool.submit(new FileVerifier(par, m, f, count));
		}

		count.await();

		Future<VerifyStatus> task = null;
		try {
			for(int i = 0; i < m.getNumFiles(); i++) {
				task = verifyPool.take();
				VerifyStatus t = task.get();

				System.out.println(t);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		threadPool.shutdown();
	}
}
