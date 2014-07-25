package net.turrem.client.game.world;

import org.lwjgl.opengl.GL11;

import net.turrem.client.Config;
import net.turrem.client.network.server.ServerPacketTerrain;
import net.turrem.client.render.engine.RenderEngine;
import net.turrem.client.render.object.RenderObject;
import net.turrem.utils.models.TVFFile;

public class Chunk
{
	public final int chunkx;
	public final int chunkz;
	private TVFFile tvf;
	private short voff;
	protected short[] height;
	private RenderObject render = null;
	private boolean loaded = false;

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

	public RenderObject getRenderObject()
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
			this.render.doDelete();
		}
		this.loaded = true;
		this.render = engine.makeObject(this.tvf, 1.0F, 0.0F, (this.voff - ServerPacketTerrain.basePadding) * -1.0F, 0.0F);
	}

	public boolean checkRebuildRender(RenderEngine engine)
	{
		if (this.render != null && this.loaded)
		{
			if (this.render.getRenderSettingsVersion() != Config.renderSettingsVersion)
			{
				this.render.doDelete();
				this.render = engine.makeObject(this.tvf, 1.0F, 0.0F, (this.voff - ServerPacketTerrain.basePadding) * -1.0F, 0.0F);
				return true;
			}
		}
		return false;
	}

	public void unload()
	{
		if (this.render != null && this.loaded)
		{
			this.render.doDelete();
			this.loaded = false;
			this.render = null;
		}
	}

	public void render()
	{
		if (this.render != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.chunkx * 16.0F, -1.0F, this.chunkz * 16.0F);
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