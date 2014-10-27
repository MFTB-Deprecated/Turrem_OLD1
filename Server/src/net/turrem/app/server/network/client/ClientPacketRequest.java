package net.turrem.app.server.network.client;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.app.server.network.client.request.Request;

/**
 * Request something from the server
 */
public class ClientPacketRequest extends ClientPacket
{
	/**
	 * The type of request made
	 */
	public String requestType;
	/**
	 * Any additional data on the request (depends on the type of request)
	 */
	public byte[] requestData;
	
	private ClientPacketRequest(String user, DataInput data, int length, byte type) throws IOException
	{
		super(user, type);
		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.requestType = new String(stringbytes, "UTF-8");
		
		this.requestData = new byte[length];
		data.readFully(this.requestData);
	}
	
	public static ClientPacketRequest create(String user, DataInput data, int length, byte type) throws IOException
	{
		return new ClientPacketRequest(user, data, length, type);
	}
	
	public Request getRequest()
	{
		try
		{
			return Request.getRequest(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
