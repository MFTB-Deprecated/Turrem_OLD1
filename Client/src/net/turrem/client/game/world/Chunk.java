package net.turrem.client.game.world;

import org.lwjgl.opengl.GL11;

import net.turrem.client.Config;
import net.turrem.client.game.world.storage.ChunkQuad;
import net.turrem.client.game.world.storage.IWorldChunkStorageSegment;
import net.turrem.client.network.server.ServerPacketTerrain;
import net.turrem.client.render.RenderEngine;
import net.turrem.client.render.object.RenderTVF;
import net.turrem.tvf.TVFFile;

public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	private TVFFile tvf;
	private short voff;
	private float yoffset;
	protected short[] height;
	public float averageHeight;
	private boolean loaded = false;
	public boolean usingPreAO;
	protected ChunkQuad parentQuad;
	
	public static int chunkRenders = 0;

	public Chunk(int chunkx, int chunkz, TVFFile tvf, byte[] hmap, short voff)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.voff = voff;
		this.tvf = tvf;
		this.height = new short[256];
		float aver = 0.0F;
		for (int i = 0; i < 256; i++)
		{
			this.height[i] = (short) ((hmap[i] & 0xFF) + voff);
			aver += this.height[i];
		}
		aver /= 256;
		this.averageHeight = aver;
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
		chunkRenders++;
		GL11.glBegin(GL11.GL_QUADS);
		int x = this.chunkx * 16;
		int z = this.chunkz * 16;
		GL11.glVertex3f(x, this.averageHeight, z);
		GL11.glVertex3f(x + 16, this.averageHeight, z);
		GL11.glVertex3f(x + 16, this.averageHeight, z + 16);
		GL11.glVertex3f(x, this.averageHeight, z + 16);
		GL11.glEnd();
	}

	public void unload()
	{
	}

	public void setParentQuad(ChunkQuad quad)
	{
		this.parentQuad = quad;
	}
}