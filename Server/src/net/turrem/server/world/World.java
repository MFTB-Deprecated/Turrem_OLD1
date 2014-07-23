package net.turrem.server.world;

import java.util.HashMap;

import net.turrem.server.Realm;
import net.turrem.server.TurremServer;
import net.turrem.server.load.control.SubscribePacket;
import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.client.ClientPacketChat;
import net.turrem.server.network.server.ServerPacketChat;
import net.turrem.server.world.gen.WorldGen;
import net.turrem.server.world.gen.WorldGenBasic;
import net.turrem.server.world.storage.WorldStorage;

public class World
{
	private HashMap<String, Integer> realmMap = new HashMap<String, Integer>();
	private Realm[] realms = new Realm[16];
	public long worldTime = 0;
	public String saveLoc;
	public long seed;
	public TurremServer theTurrem;

	private Chunk lastChunk;

	public WorldGen theWorldGen;
	
	public WorldStorage storage;

	public World(String save, long seed, TurremServer turrem)
	{
		this.theTurrem = turrem;
		this.saveLoc = save;
		this.seed = seed;
		this.storage = new WorldStorage(32, 9, this);
		this.theWorldGen = new WorldGenBasic(this.seed);
	}


	/**
	 * Finds the entity with the given identifier using a binary search
	 * algorithm. It is expected to finish in log2(N)-1 iterations, giving it
	 * O(LOG N) time.
	 * 
	 * @param id The entity's server/client identifier.
	 * @return The entity with that identifier. Null if that entity has been
	 *         removed or if the world's entity list is out of order.
	 */

	public void addPlayer(ClientPlayer player)
	{
		Integer id = this.realmMap.get(player.username);
		Realm realm;
		if (id == null)
		{
			realm = new Realm(player.username, this);
			this.realmMap.put(player.username, realm.realmId);
			this.realms[realm.realmId] = realm;
		}
		else
		{
			realm = this.realms[id];
		}
		realm.setClient(player);
	}

	public void tick()
	{
		this.worldTime++;
		this.storage.tickChunks();
		this.storage.tickEntities();
		for (Realm realm : this.realms)
		{
			if (realm != null)
			{
				realm.tick();
			}
		}
	}

	public void unloadAll()
	{
		this.storage.clear();
	}

	/**
	public void updateEntities()
	{
		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity ent = this.entities.get(i);

			int x;
			int z;
			int cx;
			int cz;
			int cxmin;
			int czmin;
			int cxmax;
			int czmax;

			if (ent.isDead())
			{
				ServerPacket rem = new ServerPacketEntityRemove(EntityRemoveType.KILL, ent.entityIdentifier);
				ent.sendPacketToClients(rem);
				this.entities.remove(i--);
			}
			else if (ent.shouldUnload)
			{
				ServerPacket rem = new ServerPacketEntityRemove(EntityRemoveType.UNLOAD, ent.entityIdentifier);
				ent.sendPacketToClients(rem);
				this.entities.remove(i--);
			}
			else
			{
				ent.onTick();

				int realm = -1;
				float vis = ent.veiwDistance();

				if (ent instanceof IHolding)
				{
					Realm al = ((IHolding) ent).getAllegiance();
					if (al != null)
					{
						realm = al.realmId;
					}
				}

				int r = ent.loadRadius();

				if (r > 0)
				{
					x = (int) ent.x;
					z = (int) ent.z;
					cx = x >> 4;
					cz = z >> 4;
					cxmin = cx - r;
					czmin = cz - r;
					cxmax = cx + r;
					czmax = cz + r;
					ChunkGroup[] groups = this.storage.getChunksAround(cx, cz);
					for (ChunkGroup g : groups)
					{
						if (realm != -1 && realm < 16)
						{
							g.entityChunkTick(cxmin, czmin, cxmax, czmax, x, z, vis, realm);
						}
						else
						{
							g.entityChunkTick(cxmin, czmin, cxmax, czmax);
						}
					}
				}
				else if (r == 0)
				{
					x = (int) ent.x;
					z = (int) ent.z;
					Chunk c = this.chunkAt(x, z);
					if (c != null)
					{
						c.onEntityTick();
					}
				}
			}
		}

		this.storage.processVisibility();
	}
	**/

	public short getHeight(int x, int z)
	{
		int chunkx = x >> 4;
		int chunky = z >> 4;
		if (this.lastChunk != null && this.lastChunk.chunkx == chunkx && this.lastChunk.chunkz == chunky)
		{
			return this.lastChunk.getHeight(x, z);
		}
		Chunk c = this.getChunk(chunkx, chunky);
		this.lastChunk = c;
		return c.getHeight(x, z);
	}

	public Chunk chunkAt(int x, int z)
	{
		return this.storage.chunks.findAndGenerateChunk(x >> 4, z >> 4, this);
	}

	public Chunk getChunk(int chunkx, int chunkz)
	{
		return this.storage.chunks.findAndGenerateChunk(chunkx, chunkz, this);
	}

	public int getSideDrop(int x, int z)
	{
		int height = this.getHeight(x, z);
		int diff = 0;
		int h;

		h = this.getHeight(x + 1, z);
		h -= height;
		if (h < diff)
		{
			diff = h;
		}

		h = this.getHeight(x, z + 1);
		h -= height;
		if (h < diff)
		{
			diff = h;
		}

		h = this.getHeight(x - 1, z);
		h -= height;
		if (h < diff)
		{
			diff = h;
		}

		h = this.getHeight(x, z - 1);
		h -= height;
		if (h < diff)
		{
			diff = h;
		}

		return -diff;
	}

	public Realm[] getRealms()
	{
		return realms;
	}

	@SubscribePacket(type = (byte) 0xA0)
	public static void processChat(ClientPacket pak, ClientPlayer player)
	{
		ClientPacketChat chat = (ClientPacketChat) pak;
		ServerPacketChat out = new ServerPacketChat();
		out.chat = chat.chat;
		out.from = chat.user;
		out.type = ServerPacketChat.ChatType.PLAYER;
		for (Realm rlm : player.theWorld.realms)
		{
			if (rlm.getClient() != null)
			{
				rlm.getClient().theConnection.addToSendQueue(out);
			}
		}
	}
}
