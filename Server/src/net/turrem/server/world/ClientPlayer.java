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
import net.turrem.server.network.server.ServerPacketStartingInfo;
import net.turrem.server.network.server.ServerPacketTerrain;
import net.turrem.server.world.material.Material;
import net.turrem.server.world.material.MaterialList;

public class ClientPlayer
{
	public World theWorld;
	public String username;
	public GameConnection theConnection;
	public Realm theRealm;

	private Set<ChunkUpdate> chunkUpdates = new HashSet<ChunkUpdate>();
	private Object chunkUpdatesLock = new Object();

	private boolean sentStarting = false;
	private boolean sentWorld = false;

	public ClientPlayer(World world, String name)
	{
		this.theWorld = world;
		this.username = name;
	}

	public void joinNetwork(GameConnection connection)
	{
		this.theConnection = connection;
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
		if (!this.sentStarting && this.theRealm.startingLocation != null)
		{
			this.sentStarting = true;
			ServerPacketStartingInfo sti = new ServerPacketStartingInfo(this.theRealm);
			this.theConnection.addToSendQueue(sti);
			for (Material mat : MaterialList.list)
			{
				ServerPacketMaterialSync pak = new ServerPacketMaterialSync();
				pak.material = mat;
				this.theConnection.addToSendQueue(pak);
			}
		}
		if (this.sentStarting && !this.sentWorld)
		{
			this.sentWorld = true;
			int x = ((int) this.theRealm.startingLocation.xCoord) >> 4;
			int z = ((int) this.theRealm.startingLocation.zCoord) >> 4;
			for (int i = -50; i <= 50; i++)
			{
				for (int j = -50; j <= 50; j++)
				{
					this.addChunkUpdate(x + i, z + j);
				}
			}
		}
		ChunkUpdate[] updates;
		synchronized (this.chunkUpdatesLock)
		{
			int size = this.chunkUpdates.size();
			if (size != 0)
			{
				System.out.println("Sending " + size + " chunk updates to " + this.username);
			}
			updates = new ChunkUpdate[size];
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
