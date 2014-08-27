package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;

import net.turrem.client.Config;
import net.turrem.client.game.world.Chunk;
import net.turrem.client.game.world.material.Material;
import net.turrem.client.game.world.material.MaterialList;
import net.turrem.tvf.TVFFile;
import net.turrem.tvf.color.TVFColor;
import net.turrem.tvf.color.TVFPaletteColor;
import net.turrem.tvf.face.EnumLightingType;
import net.turrem.tvf.face.TVFFace;
import net.turrem.tvf.layer.TVFLayerFaces;
import net.turrem.utils.geo.EnumDir;

public class ServerPacketTerrain extends ServerPacket
{
	public static final short basePadding = 6;
	public int chunkx;
	public int chunkz;
	public short voff;
	public byte[] hmap;
	public short[] mats;
	public byte[][] chunk;

	private ServerPacketTerrain(DataInput data, byte type) throws IOException
	{
		super(type);
		this.chunkx = data.readInt();
		this.chunkz = data.readInt();
		this.voff = data.readShort();
		this.hmap = new byte[256];
		for (int i = 0; i < 256; i++)
		{
			this.hmap[i] = data.readByte();
		}
		this.mats = new short[data.readByte() & 0xFF];
		for (int i = 0; i < this.mats.length; i++)
		{
			mats[i] = data.readShort();
		}
		this.chunk = new byte[256][];
		for (int i = 0; i < 256; i++)
		{
			byte[] col = new byte[data.readByte() & 0xFF];
			data.readFully(col);
			this.chunk[i] = col;
		}
	}
	
	public static ServerPacketTerrain create(DataInput data, byte type) throws IOException
	{
		return new ServerPacketTerrain(data, type);
	}

	public Chunk buildChunk()
	{
		return new Chunk(this.chunkx, this.chunkz, this.buildTVF(), this.hmap, (short) this.voff);
	}

	public TVFFile buildTVF()
	{
		TVFFile tvf = new TVFFile();
		tvf.width = 16;
		tvf.length = 16;
		tvf.height = 256;
		tvf.layers.add(this.buildTVFLayer());
		return tvf;
	}
	
	public TVFLayerFaces buildTVFLayer()
	{
		TVFPaletteColor pal = new TVFPaletteColor();
		
		ArrayList<TVFFace> faces = new ArrayList<TVFFace>();

		for (int i = 0; i < this.mats.length; i++)
		{
			Material mat = MaterialList.getMaterial(this.mats[i]);
			TVFColor color;
			if (mat != null)
			{
			color = new TVFColor(mat.red, mat.green, mat.blue);
			}
			else
			{
				color = new TVFColor(0xFFFFFF);
			}
			pal.palette[i] = color;
		}

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				this.makeCore(i, j, faces);
			}
		}

		TVFFace[] far = new TVFFace[faces.size()];
		far = faces.toArray(far);
		TVFLayerFaces tvf = new TVFLayerFaces();
		if (Config.terrainUsePreAO)
		{
			tvf.prelightType = EnumLightingType.SMOOTH;
		}
		else
		{
			tvf.prelightType = EnumLightingType.NONE;
		}
		return tvf;
	}

	private int getHeight(int x, int z)
	{
		return this.getHeight(x, z, Integer.MIN_VALUE);
	}

	private int getHeight(int x, int z, int def)
	{
		if (x < 0 || x >= 16 || z < 0 || z >= 16)
		{
			return def;
		}
		return this.hmap[x + z * 16] & 0xFF;
	}

	private void makeCore(int x, int z, ArrayList<TVFFace> faces)
	{
		int ind = x + z * 16;
		byte[] col = this.chunk[ind];
		int y = this.getHeight(x, z);
		if (col != null && col.length > 0)
		{
			TVFFace top = new TVFFace(EnumLightingType.SMOOTH);
			top.color = col[0];
			top.x = (byte) x;
			top.z = (byte) z;
			top.y = (byte) y;
			top.y += basePadding;
			top.direction = EnumDir.YUp.ind;
			faces.add(top);

			int h;
			int hu;
			int hd;

			float mao = Config.terrainAoSampleMult;

			h = this.getHeight(x + 1, z);
			hu = this.getHeight(x + 1, z + 1);
			hd = this.getHeight(x + 1, z - 1);
			if (h > y)
			{
				top.multiplyLight(1, 1, 0, mao);
			}
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace(EnumLightingType.SMOOTH);
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.direction = EnumDir.XUp.ind;
					faces.add(f);
					if (y - i == h + 1)
					{
						f.multiplyLight(1, -1, 0, mao);
					}
					if (y - i <= hu)
					{
						f.multiplyLight(1, 0, 1, mao);
					}
					if (y - i <= hd)
					{
						f.multiplyLight(1, 0, -1, mao);
					}
					if (y - i == hu + 1)
					{
						f.multiplyLight(1, -1, 1, mao);
					}
					if (y - i == hd + 1)
					{
						f.multiplyLight(1, -1, -1, mao);
					}
				}
				else
				{
					break;
				}
			}

			h = this.getHeight(x - 1, z);
			hu = this.getHeight(x - 1, z + 1);
			hd = this.getHeight(x - 1, z - 1);
			if (h > y)
			{
				top.multiplyLight(-1, 1, 0, mao);
			}
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace(EnumLightingType.SMOOTH);
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.direction = EnumDir.XDown.ind;
					faces.add(f);
					if (y - i == h + 1)
					{
						f.multiplyLight(-1, -1, 0, mao);
					}
					if (y - i <= hu)
					{
						f.multiplyLight(-1, 0, 1, mao);
					}
					if (y - i <= hd)
					{
						f.multiplyLight(-1, 0, -1, mao);
					}
					if (y - i == hu + 1)
					{
						f.multiplyLight(-1, -1, 1, mao);
					}
					if (y - i == hd + 1)
					{
						f.multiplyLight(-1, -1, -1, mao);
					}
				}
				else
				{
					break;
				}
			}

			h = this.getHeight(x, z + 1);
			hu = this.getHeight(x + 1, z + 1);
			hd = this.getHeight(x - 1, z + 1);
			if (h > y)
			{
				top.multiplyLight(0, 1, 1, mao);
			}
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace(EnumLightingType.SMOOTH);
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.direction = EnumDir.ZUp.ind;
					faces.add(f);
					if (y - i == h + 1)
					{
						f.multiplyLight(0, -1, 1, mao);
					}
					if (y - i <= hu)
					{
						f.multiplyLight(1, 0, 1, mao);
					}
					if (y - i <= hd)
					{
						f.multiplyLight(-1, 0, 1, mao);
					}
					if (y - i == hu + 1)
					{
						f.multiplyLight(1, -1, 1, mao);
					}
					if (y - i == hd + 1)
					{
						f.multiplyLight(-1, -1, 1, mao);
					}
				}
				else
				{
					break;
				}
			}

			h = this.getHeight(x, z - 1);
			hu = this.getHeight(x + 1, z - 1);
			hd = this.getHeight(x - 1, z - 1);
			if (h > y)
			{
				top.multiplyLight(0, 1, -1, mao);
			}
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace(EnumLightingType.SMOOTH);
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.direction = EnumDir.ZDown.ind;
					faces.add(f);
					if (y - i == h + 1)
					{
						f.multiplyLight(0, -1, -1, mao);
					}
					if (y - i <= hu)
					{
						f.multiplyLight(1, 0, -1, mao);
					}
					if (y - i <= hd)
					{
						f.multiplyLight(-1, 0, -1, mao);
					}
					if (y - i == hu + 1)
					{
						f.multiplyLight(1, -1, -1, mao);
					}
					if (y - i == hd + 1)
					{
						f.multiplyLight(-1, -1, -1, mao);
					}
				}
				else
				{
					break;
				}
			}

			h = this.getHeight(x + 1, z + 1);
			if (h > y)
			{
				top.multiplyLight(1, 1, 1, mao);
			}

			h = this.getHeight(x + 1, z - 1);
			if (h > y)
			{
				top.multiplyLight(1, 1, -1, mao);
			}

			h = this.getHeight(x - 1, z - 1);
			if (h > y)
			{
				top.multiplyLight(-1, 1, -1, mao);
			}

			h = this.getHeight(x - 1, z + 1);
			if (h > y)
			{
				top.multiplyLight(-1, 1, 1, mao);
			}
		}
	}
}
