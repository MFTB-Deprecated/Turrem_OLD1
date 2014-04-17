package net.turrem.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

public abstract class ClientPacket
{
	protected abstract void writePacket(DataOutput stream) throws IOException;

	public abstract byte type();
}
