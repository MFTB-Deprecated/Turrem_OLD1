package net.turrem.server.world;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.turrem.server.Realm;
import net.turrem.server.entity.Entity;
import net.turrem.server.network.server.ServerPacketTerrain;
import net.turrem.server.world.gen.WorldGen;
import net.turrem.server.world.gen.WorldGenBasic;
import net.turrem.server.world.material.Material;

public class World
{
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public HashMap<Integer, ChunkGroup> chunks = new HashMap<Integer, ChunkGroup>();
	public HashMap<String, Realm> realms = new HashMap<String, Realm>();
	public long worldTime = 0;
	public String saveLoc;
	public long seed;

	private Chunk lastChunk;

	public WorldGen theWorldGen;

	public World(String save, long seed)
	{
		this.saveLoc = save;
		this.seed = seed;
		this.theWorldGen = new WorldGenBasic(this.seed);
	}

	public void addEntity(Entity ent)
	{
		if (ent != null)
		{
			this.entities.add(ent);
			ent.onWorldRegister(this);
		}
	}

	public void tick()
	{
		this.worldTime++;
		this.updateEntities();
		int i = 0;
		for (ChunkGroup g : this.chunks.values())
		{
			g.onTick();
			i++;
			if ((i + this.worldTime) % 10 == 0)
			{
				g.tickUnload();
			}
		}

		if (this.worldTime == 4)
		{
			BufferedImage map = this.testTerrainMap();
			try
			{
				File outputfile = new File(this.saveLoc + "map.test.png");
				ImageIO.write(map, "png", outputfile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				DataOutputStream out = new DataOutputStream(new FileOutputStream(this.saveLoc + "chunk.test.bin"));
				this.writeTestPacket(out);
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void writeTestPacket(DataOutputStream out) throws IOException
	{
		ServerPacketTerrain packet = new ServerPacketTerrain(this, 0, 0);
		packet.write(out);
	}

	public BufferedImage testTerrainMap()
	{
		BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		for (int ci = -4; ci < 4; ci++)
		{
			for (int cj = -4; cj < 4; cj++)
			{
				Chunk chunk = this.getChunk(ci, cj);
				short[] map = chunk.getHeightMap();
				for (int i = 0; i < 16; i++)
				{
					for (int j = 0; j < 16; j++)
					{
						int k = i + j * 16;
						int x = (ci + 4) * 16 + i;
						int y = (cj + 4) * 16 + j;
						float h = map[k];
						h -= 128;
						h /= 256;
						if (h < 0)
						{
							h = 0;
						}
						if (h > 1)
						{
							h = 255;
						}
						Material mat = Material.list.get(chunk.getTopMaterial(x, y));
						int rgb = 0x000000;
						if (mat != null)
						{
							rgb = mat.getColor();
						}
						{
							int r = (rgb >> 16) & 0xFF;
							int g = (rgb >> 8) & 0xFF;
							int b = (rgb >> 0) & 0xFF;
							r = (int) (h * r);
							g = (int) (h * g);
							b = (int) (h * b);
							rgb = (r << 16) | (g << 8) | b;
						}
						img.setRGB(x, y, rgb);
					}
				}
			}
		}
		return img;
	}

	public void unloadAll()
	{
		for (ChunkGroup g : this.chunks.values())
		{
			g.tickUnload();
		}
		this.chunks.clear();
		this.entities.clear();
	}

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
				this.entities.remove(i--);
			}
			else
			{
				ent.onTick();
				if ((this.worldTime + i) % 5 == 0)
				{
					int r = ent.loadRadius();
					if (r > 1)
					{
						x = (int) ent.x;
						z = (int) ent.z;
						ChunkGroup g = this.groupAt(x, z);
						cx = (x >> 4) & 0x3F;
						cz = (z >> 4) & 0x3F;
						cxmin = cx - r;
						if (cxmin < 0)
						{
							cxmin = 0;
						}
						czmin = cz - r;
						if (czmin < 0)
						{
							czmin = 0;
						}
						cxmax = cx + r;
						if (cxmax > 63)
						{
							cxmax = 63;
						}
						czmax = cz + r;
						if (czmax > 63)
						{
							czmax = 63;
						}
						for (int ci = cxmin; ci < cxmax; ci++)
						{
							for (int cj = czmin; cj < czmax; cj++)
							{
								Chunk c = g.doGetChunk(ci, cj);
								if (c != null)
								{
									c.onEntityTick();
								}
							}
						}
					}
					else if (r == 1)
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
		}
	}

	public short getHeight(int x, int z)
	{
		int chunkx = x >> 4;
		int chunky = z >> 4;
		if (this.lastChunk != null && this.lastChunk.chunkx == chunkx && this.lastChunk.chunky == chunky)
		{
			return this.lastChunk.getHeight(x, z);
		}
		Chunk c = this.chunkAt(x, z);
		this.lastChunk = c;
		return c.getHeight(chunkx, z);
	}

	public ChunkGroup groupAt(int x, int y)
	{
		int i = x >> 10;
		int j = y >> 10;
		int k = i | (j << 16);
		ChunkGroup c = this.chunks.get(k);
		if (c == null)
		{
			c = new ChunkGroup(i, j, this);
			this.chunks.put(k, c);
		}
		return c;
	}

	public Chunk chunkAt(int x, int y)
	{
		ChunkGroup g = this.groupAt(x, y);
		if (g != null)
		{
			return g.getChunk(x >> 4, y >> 4);
		}
		return null;
	}

	public ChunkGroup getGroup(int x, int y)
	{
		int i = x >> 6;
		int j = y >> 6;
		int k = i | (j << 16);
		ChunkGroup c = this.chunks.get(k);
		if (c == null)
		{
			c = new ChunkGroup(i, j, this);
			this.chunks.put(k, c);
		}
		return c;
	}

	public Chunk getChunk(int x, int y)
	{
		ChunkGroup g = this.getGroup(x, y);
		if (g != null)
		{
			return g.getChunk(x, y);
		}
		return null;
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
}
