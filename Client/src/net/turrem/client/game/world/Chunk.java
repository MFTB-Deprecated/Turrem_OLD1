package net.turrem.client.game.world;

import org.lwjgl.opengl.GL11;

import net.turrem.client.Config;
import net.turrem.client.network.server.ServerPacketTerrain;
import net.turrem.client.render.RenderEngine;
import net.turrem.client.render.object.RenderTVF;
import net.turrem.utils.models.TVFFile;

public class Chunk
{
	public final int chunkx;
	public final int chunkz;
	private TVFFile tvf;
	private short voff;
	private float yoffset;
	protected short[] height;
	private RenderTVF render = null;
	private boolean loaded = false;
	public boolean usingPreAO;

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

	public RenderTVF getRenderObject()
	{
		return render;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public void buildRender(RenderEngine engine)
	{
		if (this.render != null && this.loaded)
		{
			this.loaded = false;
			this.render.delete();
		}
		this.loaded = true;
		this.yoffset = (this.voff - ServerPacketTerrain.basePadding) - 1.0F;
		this.usingPreAO = Config.terrainUsePreAO;
		this.render = engine.loadTVFRender(this.tvf, 1.0F, this.usingPreAO);
	}
	
	public void unload()
	{
		if (this.render != null && this.loaded)
		{
			this.render.delete();
			this.loaded = false;
			this.render = null;
		}
	}

	public void render()
	{
		if (this.render != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.chunkx * 16.0F, this.yoffset, this.chunkz * 16.0F);
			this.render.doRender();
			GL11.glPopMatrix();
		}
	}

	public int getHeight(int x, int z)
	{
		x &= 0x0F;
		z &= 0x0F;
		return this.height[x + z * 16];
	}
}