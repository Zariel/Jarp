package com.fein.jarp.par2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fein.jarp.par2.packets.AbstractPacket;
import com.fein.jarp.par2.packets.Header;
import com.fein.jarp.par2.packets.Packet;
import com.fein.jarp.par2.packets.PacketType;
import com.fein.jarp.par2.packets.core.CreatorPacket;
import com.fein.jarp.par2.packets.core.FileDesc;
import com.fein.jarp.par2.packets.core.MainPacket;
import com.fein.jarp.util.ByteUtil;

/**
 * Open a par2 file and parse it.
 * 
 * @author Chris
 * 
 */

public class ParFile implements Runnable {
	private final File file;

	private List<Packet> packets;

	private int numFiles = 0;

	private Object sliceSize;

	private byte[] mainPacketMd5;

	private String creator;

	private MainPacket mainPacket;

	private Map<String, FileDesc> fileDesc;

	private String filePath;

	public ParFile(String file) {
		this(new File(file));
	}

	public ParFile(File file) {
		this.file = file;
		packets = new ArrayList<Packet>();
		fileDesc = new HashMap<String, FileDesc>();

		parsePackets();
	}

	private void parsePackets() {
		try(InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			// Read until we find a packet header
			byte[] buffer = new byte[16];

			while((is.read(buffer, 0, 16)) > 0) {
				boolean found = true;

				for(int i = 0; i < 8; i++) {
					if(buffer[i] != Header.magicHeader[i]) {
						found = false;
						break;
					}
				}

				if(found) {
					BigInteger len = ByteUtil.getPacketSize(buffer);
					// TODO: Check we dont overflow long
					int skip = (int) len.longValue();

					byte[] packet = new byte[skip];

					System.arraycopy(buffer, 0, packet, 0, 16);

					is.read(packet, 16, skip - 16);

					Packet p = new Packet(packet);

					// Get info like the number of files in the recovery set.
					getInfo(p);

					packets.add(p);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get info for the whole par2 file not just individual packets.
	 * 
	 * @param p
	 */
	private void getInfo(AbstractPacket p) {
		switch(p.getPacketType()) {
		case MAIN_PACKET:
			MainPacket m = (MainPacket) p.getBody();
			numFiles = m.getNumFiles();
			sliceSize = m.getSliceSize();
			mainPacketMd5 = m.getMd5();
			break;
		case CREATOR:
			CreatorPacket c = (CreatorPacket) p.getBody();
			creator = c.getClientId();
			break;
		case FILE_DESC:
			FileDesc f = (FileDesc) p.getBody();
			fileDesc.put(new String(f.getFileID()), f);
			break;
		}
	}

	public int getNumPackets() {
		return packets.size();
	}

	public AbstractPacket getPacket(int i) {
		return packets.get(i);
	}

	public long getSize() {
		return file.length();
	}

	public int getNumFiles() {
		return numFiles;
	}

	public String getCreator() {
		return creator;
	}

	/**
	 * Check all the packets SetID matches the MD5 hash of the mainPacket body.
	 * 
	 * @return
	 */
	public boolean isPacketsValid() {
		for(AbstractPacket p : packets) {
			if(!Arrays.equals(p.getHeader().getRecoveryID(), mainPacketMd5)) {
				return false;
			}
		}

		return true;
	}

	public List<Packet> getPackets() {
		return packets;
	}

	public MainPacket getMainPacket() {
		if(mainPacket == null) {
			for(AbstractPacket p : packets) {
				if(p.getPacketType() == PacketType.MAIN_PACKET) {
					mainPacket = (MainPacket) p.getBody();
				}
			}
		}

		return mainPacket;
	}

	public FileDesc getFileDesc(String fileId) {
		return fileDesc.get(fileId);
	}

	public String getFileRootPath() {
		if(filePath == null) {
			if((filePath = file.getParent()) == null) {
				filePath = "";
			}
		}

		return filePath;
	}

	@Override
	public void run() {
		if(file != null) {
			parsePackets();
		}
	}
}
