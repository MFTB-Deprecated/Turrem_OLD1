package net.turrem.server;

import java.util.ArrayList;

import net.turrem.server.entity.IHolding;
import net.turrem.server.network.server.ServerPacket;
import net.turrem.server.world.ChunkUpdate;
import net.turrem.server.world.ClientPlayer;
import net.turrem.server.world.World;
import net.turrem.utils.geo.Point;

public class Realm
{
	private static int nextRealmId = 0;
	public final String user;
	public World theWorld;
	public ArrayList<IHolding> holdings = new ArrayList<IHolding>();
	private ClientPlayer client = null;
	public final int realmId;
	
	public Point startingLocation = null;
	
	public Realm(String username, World world)
	{
		this.theWorld = world;
		this.user = username;
		this.realmId = nextRealmId++;
		this.spawn();
	}
	
	public void tick()
	{
		if (this.client != null)
		{
			this.client.sendChunks();
		}
	}
	
	public void spawn()
	{
		// Random rand = new Random(this.theWorld.seed ^ this.user.hashCode());
		int x = 2000;
		int z = 2000;
		int y;
		/**
		 * int size = this.theWorld.storage.chunks.mapSize; x =
		 * rand.nextInt(size - 1000) + 500; z = rand.nextInt(size - 1000) + 500;
		 **/
		y = this.theWorld.getHeight(x, z);
		this.startingLocation = Point.getPoint(x, y, z);
	}
	
	public void sendPacket(ServerPacket packet)
	{
		if (this.client != null)
		{
			this.client.sendPacket(packet);
		}
	}
	
	public boolean addChunkUpdate(int chunkx, int chunkz)
	{
		if (this.client != null)
		{
			return this.client.addChunkUpdate(chunkx, chunkz);
		}
		return false;
	}
	
	public boolean addChunkUpdate(ChunkUpdate update)
	{
		if (this.client != null)
		{
			return this.client.addChunkUpdate(update);
		}
		return false;
	}
	
	public void joinRealm(IHolding item)
	{
		if (!this.holdings.contains(item))
		{
			this.holdings.add(item);
		}
	}
	
	public void leaveRealm(IHolding item, String reason)
	{
		this.holdings.remove(item);
	}
	
	public ClientPlayer getClient()
	{
		return this.client;
	}
	
	public void setClient(ClientPlayer client)
	{
		this.client = client;
		this.client.theRealm = this;
	}
	
	public void onPlayerExit()
	{
		this.client = null;
	}
}
