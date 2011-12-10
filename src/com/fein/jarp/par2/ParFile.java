package com.fein.jarp.par2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fein.jarp.par2.packets.AbstractPacket;
import com.fein.jarp.par2.packets.Header;
import com.fein.jarp.par2.packets.Packet;
import com.fein.jarp.par2.packets.PacketType;
import com.fein.jarp.par2.packets.core.MainPacket;

/**
 * Open a par2 file and parse it.
 * 
 * @author Chris
 * 
 */

public class ParFile {
	private final File file;

	private List<Packet> packets;

	private int numFiles = 0;

	public ParFile(String file) {
		this(new File(file));
	}

	public ParFile(File file) {
		this.file = file;
		packets = new ArrayList<Packet>();

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
					BigInteger len = AbstractPacket.getPacketSize(buffer);
					// TODO: Check we dont overflow long
					int skip = (int) len.longValue();

					byte[] packet = new byte[skip];

					System.arraycopy(buffer, 0, packet, 0, 16);

					is.read(packet, 16, skip - 16);

					Packet p = new Packet(packet);
					if(p.getPacketType() == PacketType.MAIN_PACKET) {
						numFiles = ((MainPacket) p.getBody()).getNumFiles();
					}

					packets.add(p);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumPackets() {
		return packets.size();
	}

	public Packet getPacket(int i) {
		return packets.get(i);
	}

	public long getSize() {
		return file.length();
	}

	public int getNumFiles() {
		return numFiles;
	}
}
