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

	private Set<ChunkUpdate> chunkUpdates = new HashSet<ChunkUpdate>();
	private Object chunkUpdatesLock = new Object();

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
				if (this.theWorld.storage.getVisibility(reqchunk.chunkx, reqchunk.chunky, this.theRealm.realmId))
				{
					Chunk chunk = this.theWorld.getChunk(reqchunk.chunkx, reqchunk.chunky);
					if (chunk != null)
					{
						ServerPacketTerrain pak = new ServerPacketTerrain(chunk, this.theWorld);
						this.theConnection.addToSendQueue(pak);
					}
				}
			}
		}
	}

	public boolean addChunkUpdate(int chunkx, int chunkz)
	{
		ChunkUpdate update = new ChunkUpdate(chunkx, chunkz);
		boolean flag;
		synchronized (this.chunkUpdatesLock)
		{
			flag = this.chunkUpdates.add(update);
		}
		return flag;
	}

	public boolean addChunkUpdate(ChunkUpdate update)
	{
		boolean flag;
		synchronized (this.chunkUpdatesLock)
		{
			flag = this.chunkUpdates.add(update);
		}
		return flag;
	}

	public void sendChunks()
	{
		ChunkUpdate[] updates;
		synchronized (this.chunkUpdatesLock)
		{
			updates = new ChunkUpdate[this.chunkUpdates.size()];
			this.chunkUpdates.toArray(updates);
			this.chunkUpdates.clear();
		}
		for (ChunkUpdate update : updates)
		{
			Chunk chunk = this.theWorld.getChunk(update.chunkx, update.chunkz);
			if (chunk != null)
			{
				ServerPacketTerrain pak = new ServerPacketTerrain(chunk, this.theWorld);
				this.theConnection.addToSendQueue(pak);
			}
		}
	}

	public boolean reviewPacket(ClientPacket packet)
	{
		return true;
	}
}
