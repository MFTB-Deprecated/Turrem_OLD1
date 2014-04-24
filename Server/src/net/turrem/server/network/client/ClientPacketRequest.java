package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.server.network.client.request.Request;

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
	
	public ClientPacketRequest(String user, DataInput data, int length) throws IOException
	{
		super(user);
		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.requestType = new String(stringbytes, "UTF-8");
		
		this.requestData = new byte[length];
		data.readFully(this.requestData);
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
