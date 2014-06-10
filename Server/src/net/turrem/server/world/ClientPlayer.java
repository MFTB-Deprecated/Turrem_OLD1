package net.turrem.server.world;

import java.util.HashSet;
import java.util.Set;

import net.turrem.server.Realm;
import net.turrem.server.network.GameConnection;
import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.client.ClientPacketRequest;
import net.turrem.server.network.client.request.Request;
import net.turrem.server.network.client.request.RequestChunk;
import net.turrem.server.network.server.ServerPacket;
import net.turrem.server.network.server.ServerPacketMaterialSync;
import net.turrem.server.network.server.ServerPacketTerrain;
import net.turrem.server.world.material.Material;

public class ClientPlayer
{
	public World theWorld;
	public String username;
	public GameConnection theConnection;
	public Realm theRealm;
	
	protected Set<ChunkUpdate> chunkUpdates = new HashSet<ChunkUpdate>();
	
	public ClientPlayer(World world, String name)
	{
		this.theWorld = world;
		this.username = name;
	}
	
	public void joinNetwork(GameConnection connection)
	{
		this.theConnection = connection;
		for (Material mat : Material.list.values())
		{
			ServerPacketMaterialSync sync = new ServerPacketMaterialSync(mat);
			this.theConnection.addToSendQueue(sync);
		}
	}
	
	public void sendPacket(ServerPacket packet)
	{
		if (this.theConnection != null)
		{
			this.theConnection.addToSendQueue(packet);
		}
	}
	
	public void exit()
	{
		if (this.theRealm != null)
		{
			this.theRealm.onPlayerExit();
		}
	}
	
	public void processPacket(ClientPacket packet)
	{
		if (packet instanceof ClientPacketRequest)
		{
			ClientPacketRequest reqpak = (ClientPacketRequest) packet;
			Request req = reqpak.getRequest();
			if (req instanceof RequestChunk)
			{
				RequestChunk reqchunk = (RequestChunk) req;
				Chunk chunk = this.theWorld.getChunk(reqchunk.chunkx, reqchunk.chunky);
				if (chunk != null)
				{
					ServerPacketTerrain pak = new ServerPacketTerrain(chunk, this.theWorld);
					this.theConnection.addToSendQueue(pak);
				}
			}
		}
	}
	
	public void sendChunks()
	{
		
	}
	
	public boolean reviewPacket(ClientPacket packet)
	{
		return true;
	}
}
