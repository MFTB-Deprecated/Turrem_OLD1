package net.turrem.client.game;

import net.turrem.client.render.object.RenderObject;

public class Chunk
{
	public final int chunkx;
	public final int chunkz;
	protected RenderObject render;
	
	public Chunk(int chunkx, int chunkz, RenderObject render)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.render = render;
	}
}