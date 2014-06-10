package net.turrem.server;

import java.util.ArrayList;
import java.util.Random;

import net.turrem.server.entity.Entity;
import net.turrem.server.entity.IHolding;
import net.turrem.server.network.server.ServerPacket;
import net.turrem.server.network.server.ServerPacketAddEntity;
import net.turrem.server.network.server.ServerPacketEntityRemove;
import net.turrem.server.network.server.ServerPacketEntityRemove.EntityRemoveType;
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
		Random rand = new Random(this.theWorld.seed ^ this.user.hashCode());
		int x = 0;
		int z = 0;
		int y;
		int tries = 0;
		while (tries++ < 100)
		{
			x = rand.nextInt(400) - 200;
			z = rand.nextInt(400) - 200;
			if (this.theWorld.chunkAt(x, z).getTopStratum(x, z).getMaterial().isPlayerSpawnable())
			{
				break;
			}
		}
		y = this.theWorld.getHeight(x, z);
		this.startingLocation = Point.getPoint(x, y, z);
		this.theWorld.theTurrem.theLoader.getEntityLoader().processRealmInits(this, this.theWorld);
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
		return client;
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

	public void onEntityDisappear(Entity ent)
	{
		ServerPacket rem = new ServerPacketEntityRemove(EntityRemoveType.OFFMAP, ent.entityIdentifier);
		this.sendPacket(rem);
	}

	public void onEntityAppear(Entity ent)
	{
		ServerPacket add = new ServerPacketAddEntity(ent);
		this.sendPacket(add);
	}

	public void setVisibility(int chunkx, int chunkz, boolean visible)
	{
		this.theWorld.storage.setVisibility(chunkx, chunkz, this.realmId, visible);
	}

	public boolean getVisibility(int chunkx, int chunkz)
	{
		return this.theWorld.storage.getVisibility(chunkx, chunkz, this.realmId);
	}

	public void setVisibilityAt(int x, int z, boolean visible)
	{
		this.theWorld.storage.setVisibility(x >> 4, z >> 4, this.realmId, visible);
	}

	public boolean getVisibilityAt(int x, int z)
	{
		return this.theWorld.storage.getVisibility(x >> 4, z >> 4, this.realmId);
	}
}
