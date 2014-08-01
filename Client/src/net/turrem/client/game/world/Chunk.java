package net.turrem.client.game.world;

import org.lwjgl.opengl.GL11;

import net.turrem.client.Config;
import net.turrem.client.game.world.storage.ChunkQuad;
import net.turrem.client.game.world.storage.IWorldChunkStorageSegment;
import net.turrem.client.network.server.ServerPacketTerrain;
import net.turrem.client.render.RenderEngine;
import net.turrem.client.render.object.RenderTVF;
import net.turrem.utils.models.TVFFile;

public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	private TVFFile tvf;
	private short voff;
	private float yoffset;
	protected short[] height;
	private boolean loaded = false;
	public boolean usingPreAO;
	protected ChunkQuad parentQuad;

	public Chunk(int chunkx, int chunkz, TVFFile tvf, byte[] hmap, short voff)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.voff = voff;
		this.tvf = tvf;
		this.height = new short[256];
		for (int i = 0; i < 256; i++)
		{
			this.height[i] = (short) ((hmap[i] & 0xFF) + voff);
		}
	}

	public boolean isLoaded()
	{
		return loaded;
	}
	

	public int getHeight(int x, int z)
	{
		x &= 0x0F;
		z &= 0x0F;
		return this.height[x + z * 16];
	}

	public void render(int viewx, int viewz, int radius, int preciseRadius)
	{
	}

	public void unload()
	{
	}

	public void setParentQuad(ChunkQuad quad)
	{
		this.parentQuad = quad;
	}
}