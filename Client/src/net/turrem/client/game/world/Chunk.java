package net.turrem.client.game.world;

import org.lwjgl.opengl.GL11;

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

	public void buildRender(RenderEngine engine)
	{
		if (this.render != null)
		{
			this.render.doDelete();
		}
		this.render = engine.makeObject(this.tvf, 16.0F, 8.0F, this.voff * -1.0F, 8.0F);
	}
	
	public void render()
	{
		if (this.render != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.chunkx * 1.0F, 0.0F, this.chunkz * 1.0F);
			this.render.doRender();
			GL11.glPopMatrix();
		}
	}
}