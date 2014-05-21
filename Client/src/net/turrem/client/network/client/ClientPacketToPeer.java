package net.turrem.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.client.network.peer.PeerPacket;

public class ClientPacketToPeer extends ClientPacket
{
	public PeerPacket packet;
	
	public ClientPacketToPeer(PeerPacket packet)
	{
		this.packet = packet;
	}
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
	}

	@Override
	public byte type()
	{
		return (byte) 0xA1;
	}
}
