package net.turrem.server.world;

import java.util.ArrayList;
import java.util.Collection;

import net.turrem.server.world.material.MatStack;
import net.turrem.server.world.material.Material;
import net.turrem.utils.nbt.NBTCompound;
import net.turrem.utils.nbt.NBTList;

/**
 * A 16x16 region of the world
 */
public class Chunk
{
	protected ArrayList<Stratum> strata = new ArrayList<Stratum>();
	protected short[] height;
	protected short[] top;

	protected short minHeight;
	protected short maxHeight;
	/**
	 * Has this chunk's terrain been changed recently
	 */
	private boolean rebuildhmap = false;

	public final int chunkx;
	public final int chunky;

	protected int entitytickcount = 0;
	protected int noenttimer = 0;

	public Chunk(int chunkx, int chunky, ArrayList<Stratum> strata)
	{
		this.chunkx = chunkx;
		this.chunky = chunky;
		this.strata = strata;
		this.buildHeightMap();
	}

	public Chunk(int chunkx, int chunky)
	{
		this.chunkx = chunkx;
		this.chunky = chunky;
	}

	/**
	 * Called when any entity loading this chunk is ticked.
	 */
	public void onEntityTick()
	{
		this.entitytickcount++;
	}

	/**
	 * Determines is this chunk be unloaded.
	 * @param unloadtime The number calls to this method should the chunk wait before unloading if the chunk does not have a reason to stay loaded.
	 * @return True if the the chunk should be unloaded
	 */
	public boolean tickUnload(int unloadtime)
	{
		if (this.entitytickcount == 0)
		{
			this.noenttimer++;
			if (this.noenttimer >= unloadtime)
			{
				return true;
			}
		}
		else
		{
			this.entitytickcount = 0;
			this.noenttimer = 0;
			return false;
		}
		return false;
	}

	/**
	 * Re-builds the chunk's heightmap and a few other things. Should be called if any change to the terrain in this chunk has been made.
	 */
	public void buildHeightMap()
	{
		this.rebuildhmap = false;
		this.height = new short[256];
		this.top = new short[256];
		this.minHeight = Short.MAX_VALUE;
		this.maxHeight = Short.MIN_VALUE;
		for (int i = 0; i < this.strata.size(); i++)
		{
			byte[] st = this.strata.get(i).getDepthMap();
			for (int j = 0; j < 256; j++)
			{
				this.height[j] += st[j] & 0xFF;
				if (st[j] > 0)
				{
					this.top[j] = (byte) (i & 0xFF);
				}
			}
		}
		for (int i = 0; i < 256; i++)
		{
			short h = this.height[i];
			if (h > this.maxHeight)
			{
				this.maxHeight = h;
			}
			if (h < this.minHeight)
			{
				this.minHeight = h;
			}
		}
	}

	public short getMinHeight()
	{
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return minHeight;
	}

	public short getMaxHeight()
	{
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return maxHeight;
	}

	public short[] getHeightMap()
	{
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return this.height;
	}

	public short getHeight(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return this.height[x + y * 16];
	}

	public String getTopMaterial(int x, int y)
	{
		return this.getTopStratum(x, y).getMateriaId();
	}

	public Stratum getTopStratum(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		if (this.top == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		int top = this.top[x + y * 16] & 0xFF;
		return this.strata.get(top);
	}

	protected int getFirstOpen(int x, int y, String id)
	{
		x &= 0x0F;
		y &= 0x0F;
		int index = -1;
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			if (id.equals(st.getMateriaId()))
			{
				index = i;
			}
			if (st.getDepth(x, y) > 0)
			{
				return index;
			}
		}
		return -1;
	}

	public Collection<MatStack> removeTop(int x, int y)
	{
		Stratum st = this.getTopStratum(x, y);
		int z = this.getHeight(x, y);
		st.addDepth(x, y, -1);
		this.rebuildhmap = true;
		return st.getMat(x, y, z);
	}

	public Collection<MatStack> removeMultiTop(int x, int y, int num, boolean drop)
	{
		ArrayList<MatStack> mats = new ArrayList<MatStack>();
		int z = this.getHeight(x, y);
		if (num < z)
		{
			num = z;
		}
		int n = num;
		while (n > 0)
		{
			Stratum st = this.getTopStratum(x, y);
			int k = st.getDepth(x, y);
			if (n < k)
			{
				k = n;
			}
			if (drop)
			{
				for (int i = 0; i < k; i++)
				{
					mats.addAll(st.getMat(x, y, z--));
				}
			}
			n += st.addDepth(x, y, n);
			this.rebuildhmap = true;
		}
		return mats;
	}

	public Collection<MatStack> removeShapeTop(byte[] grid, boolean drop)
	{
		ArrayList<MatStack> mats = new ArrayList<MatStack>();
		int i = this.strata.size() - 1;
		byte[] rm = grid.clone();
		while (i >= 0)
		{
			Stratum st = this.strata.get(i);
			int sum = 0;
			for (int j = 0; j < 256; j++)
			{
				int g = rm[j] & 0xFF;
				if (g > 0)
				{
					if (drop)
					{
						int x = j % 16;
						int y = i / 16;
						int d = st.getDepth(x, y);
						int z = this.height[j] - (rm[j] & 0xFF) + (grid[j] & 0xFF);
						if (g < d)
						{
							d = g;
						}
						for (int k = 0; k < d; k++)
						{
							mats.addAll(st.getMat(x, y, z));
							z--;
						}
					}

					rm[j] = (byte) ((g - st.removeDepth(j, g)) & 0xFF);
					sum += rm[j] & 0xFF;
				}
			}
			if (sum == 0)
			{
				break;
			}
			i--;
		}
		this.rebuildhmap = true;
		return mats;
	}

	public void placeTop(int x, int y, String id)
	{
		x &= 0x0F;
		y &= 0x0F;
		int last = this.getFirstOpen(x, y, id);
		if (last == -1)
		{
			last = this.strata.size();
			this.strata.add(new Stratum(id));
		}
		Stratum st = this.strata.get(last);
		if (st.addDepth(x, y, 1) != 1)
		{
			st = new Stratum(id);
			this.strata.add(st);
			st.setDepth(x, y, 1);
		}
		this.rebuildhmap = true;
	}

	public int getChunkX()
	{
		return chunkx;
	}

	public int getChunkY()
	{
		return chunky;
	}

	public Material[] coreTerrain(int x, int depth, int z)
	{
		x &= 0x0F;
		z &= 0x0F;
		Material[] core = new Material[depth];
		int y = 0;
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			Material mat = st.getMaterial();
			int d = st.getDepth(x, z);
			for (int j = 0; j < d; j++)
			{
				if (y < depth)
				{
					core[y] = mat;
					y++;
				}
				else
				{
					return core;
				}
			}
		}
		return core;
	}

	public NBTCompound writeToNBT()
	{
		NBTCompound nbt = new NBTCompound();
		NBTList list = new NBTList();
		for (Stratum s : this.strata)
		{
			list.appendTag(s.writeToNBT());
		}
		nbt.setInt("chunkx", this.chunkx);
		nbt.setInt("chunky", this.chunky);
		nbt.setList("strata", list);
		return nbt;
	}

	public static Chunk readFromNBT(NBTCompound nbt, int chunkx, int chunky)
	{
		ArrayList<Stratum> strata = new ArrayList<Stratum>();
		NBTList list = nbt.getList("strata");
		for (int i = 0; i < list.tagCount(); i++)
		{
			strata.add(Stratum.readFromNBT((NBTCompound) list.tagAt(i)));
		}
		Chunk chunk = new Chunk(chunkx, chunky, strata);
		return chunk;
	}
}
