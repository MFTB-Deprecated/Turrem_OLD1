package net.turrem.server.world;

import net.turrem.utils.nbt.NBTCompound;

public class Stratum
{
	protected byte[] depth = new byte[256];
	private String material;
	protected NBTCompound data = null;

	public Stratum(String material)
	{
		this.material = material;
	}

	private Stratum(String material, byte[] map, NBTCompound data)
	{
		this.material = material;
		this.depth = map;
		this.data = data;
	}

	public byte[] getDepthMap()
	{
		return depth;
	}

	public void setDepthMap(byte[] depth)
	{
		this.depth = depth;
	}

	public String getMateriaId()
	{
		return material;
	}

	public NBTCompound getNBTData()
	{
		if (this.data == null)
		{
			this.data = new NBTCompound();
		}
		return data;
	}

	public int setDepth(int x, int y, int value)
	{
		x &= 0x0F;
		y &= 0x0F;
		int d = this.depth[x + y * 16] & 0xFF;
		if (value < 0)
		{
			value = 0;
		}
		if (value > 255)
		{
			value = 255;
		}
		this.depth[x + y * 16] = (byte) (value & 0xFF);
		return value - d;
	}

	public int addDepth(int x, int y, int value)
	{
		x &= 0x0F;
		y &= 0x0F;
		int d = this.depth[x + y * 16] & 0xFF;
		if (value < 0)
		{
			if (value + d < 0)
			{
				this.depth[x + y * 16] = (byte) (0 & 0xFF);
				return -d;
			}
			else
			{
				this.depth[x + y * 16] = (byte) ((d + value) & 0xFF);
				return value;
			}
		}
		else if (value > 0)
		{
			if (value + d > 255)
			{
				this.depth[x + y * 16] = (byte) (255 & 0xFF);
				return 255 - d;
			}
			else
			{
				this.depth[x + y * 16] = (byte) ((d + value) & 0xFF);
				return value;
			}
		}
		else
		{
			return 0;
		}
	}

	public int getDepth(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		return this.depth[x + y * 16] & 0xFF;
	}

	public NBTCompound writeToNBT()
	{
		NBTCompound nbt = new NBTCompound();
		nbt.setString("id", this.material);
		if (this.data != null && !this.data.isEmpty())
		{
			nbt.setCompound("data", this.data);
		}
		nbt.setByteArray("map", this.depth);
		return nbt;
	}

	public static Stratum readFromNBT(NBTCompound nbt)
	{
		String id = nbt.getString("id");
		NBTCompound dat = nbt.getCompound("data");
		byte[] map = nbt.getByteArray("map");
		return new Stratum(id, map, dat);
	}
}
