package net.turrem.server.network.client;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.turrem.server.world.ClientPlayer;
import net.turrem.utils.CallList;

public class ClientPacketManager
{
	private static Map<Class<? extends ClientPacket>, Byte> packetClassMap;
	
	private static CallList[] packetProcessCalls;
	private static CallList[] packetReviewCalls;

	public static ClientPacket readPacket(byte packetType, int length, DataInput data, String user) throws IOException
	{
		switch (packetType & 0xFF)
		{
			case 0x10:
				return ClientPacketAction.create(data, user, length, packetType);
			case 0x30:
				return ClientPacketRequest.create(user, data, length, packetType);
			case 0x31:
				return ClientPacketServerInfoRequest.create(user, packetType);
			case 0x32:
				return ClientPacketPing.create(user, data, packetType);
			case 0xA0:
				return ClientPacketChat.create(user, data, length, packetType);
			case 0xFD:
				return ClientPacketKeepAlive.create(user, packetType);
			case 0xFE:
				return ClientPacketCustomNBT.create(user, data, packetType);
			case 0xFF:
				return ClientPacketCustom.create(user, data, length, packetType);
			default:
				return null;
		}
	}
	
	public static Class<? extends ClientPacket> getPacket(byte packetType)
	{
		switch (packetType & 0xFF)
		{
			case 0x10:
				return ClientPacketAction.class;
			case 0x30:
				return ClientPacketRequest.class;
			case 0x31:
				return ClientPacketServerInfoRequest.class;
			case 0x32:
				return ClientPacketPing.class;
			case 0xA0:
				return ClientPacketChat.class;
			case 0xFD:
				return ClientPacketKeepAlive.class;
			case 0xFE:
				return ClientPacketCustomNBT.class;
			case 0xFF:
				return ClientPacketCustom.class;
			default:
				return null;
		}
	}
	
	public static boolean addProcessCall(Method call, byte type)
	{
		if (getPacket(type) == null)
		{
			return false;
		}
		Class<?>[] pars = call.getParameterTypes();
		if (!Modifier.isStatic(call.getModifiers()))
		{
			return false;
		}
		if (pars.length == 2)
		{
			if (pars[0].isAssignableFrom(ClientPacket.class) && pars[1].isAssignableFrom(ClientPlayer.class))
			{
				packetProcessCalls[type & 0xFF].addCall(call);
				return true;
			}
		}
		return false;
	}
	
	public static boolean addProcessCall(Method call, Class<? extends ClientPacket> type)
	{
		Byte typeByte = packetClassMap.get(type);
		if (typeByte != null)
		{
			return addProcessCall(call, typeByte);
		}
		return false;
	}
	
	public static boolean addReviewCall(Method call, byte type)
	{
		if (getPacket(type) == null)
		{
			return false;
		}
		Class<?>[] pars = call.getParameterTypes();
		Class<?> ret = call.getReturnType();
		if (!Boolean.TYPE.isAssignableFrom(ret))
		{
			return false;
		}
		if (!Modifier.isStatic(call.getModifiers()))
		{
			return false;
		}
		if (pars.length == 2)
		{
			if (pars[0].isAssignableFrom(ClientPacket.class) && pars[1].isAssignableFrom(ClientPlayer.class))
			{
				packetReviewCalls[type & 0xFF].addCall(call);
				return true;
			}
		}
		return false;
	}
	
	public static boolean addReviewCall(Method call, Class<? extends ClientPacket> type)
	{
		Byte typeByte = packetClassMap.get(type);
		if (typeByte != null)
		{
			return addReviewCall(call, typeByte);
		}
		return false;
	}
	
	public static boolean reviewPacket(ClientPacket pak, ClientPlayer player)
	{
		boolean process = true;
		CallList list = packetReviewCalls[pak.getPacketType() & 0xFF];
		Iterator<Method> mtds = list.getCalls();
		while (mtds.hasNext())
		{
			Method mtd = mtds.next();
			try
			{
				process &= (boolean) mtd.invoke(null, pak, player);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
		return process;
	}
	
	public static void processPacket(ClientPacket pak, ClientPlayer player)
	{
		CallList list = packetProcessCalls[pak.getPacketType() & 0xFF];
		Iterator<Method> mtds = list.getCalls();
		while (mtds.hasNext())
		{
			Method mtd = mtds.next();
			try
			{
				mtd.invoke(null, pak, player);
			}
			catch (IllegalAccessException | IllegalArgumentException e)
			{
				
			}
			catch (InvocationTargetException e)
			{
				e.getCause().printStackTrace();
			}
		}
	}
	
	public static ClientPacket readSinglePacket(String user, InputStream stream) throws IOException
	{
		byte type = (byte) stream.read();
		int length = (stream.read() << 8) | (stream.read() << 0);
		if (length == 0xFFFF)
		{
			length = (stream.read() << 24) | (stream.read() << 16) | (stream.read() << 8) | (stream.read() << 0);
		}
		byte[] packet = new byte[length];
		stream.read(packet);
		DataInput input = new DataInputStream(new ByteArrayInputStream(packet));
		return readPacket(type, length, input, user);
	}
	
	static
	{
		packetClassMap = new HashMap<Class<? extends ClientPacket>, Byte>();
		for (int i = 0; i <= 0xFF; i++)
		{
			Class<? extends ClientPacket> pak = getPacket((byte) i);
			if (pak != null)
			{
				packetClassMap.put(pak, (byte) i);
			}
		}
		
		packetProcessCalls = new CallList[256];
		for (int i = 0; i < packetProcessCalls.length; i++)
		{
			packetProcessCalls[i] = new CallList();
		}
		
		packetReviewCalls = new CallList[256];
		for (int i = 0; i < packetReviewCalls.length; i++)
		{
			packetReviewCalls[i] = new CallList();
		}
	}
}
