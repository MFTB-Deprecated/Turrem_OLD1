package net.turrem.client.render;

import net.turrem.client.render.object.RenderObjectTexture;

public class RenderIconTexture extends RenderIcon2D
{
	private final String name;
	public RenderObjectTexture object;
	
	public RenderIconTexture(String name)
	{
		this.name = name;
	}
	
	@Override
	public void load(RenderEngine engine)
	{
		this.object = engine.renderStore2D.loadObject(this.name);
	}

	@Override
	public void start()
	{
		if (this.loaded())
		{
			this.object.bind();
		}
	}

	@Override
	public void end()
	{
		if (this.loaded())
		{
			this.object.unbind();
		}
	}
	
	public boolean loaded()
	{
		return this.object != null && this.object.isLoaded();
	}
}
