package com.fein.jarp.par2.packets.core;

import com.fein.jarp.par2.packets.PacketBody;

public class CreatorPacket extends PacketBody {

	private String clientId;

	public CreatorPacket(byte[] body) {
		super(body);
	}

	public String getClientId() {
		if(clientId == null) {
			clientId = new String(body);
		}

		return clientId;
	}

}
