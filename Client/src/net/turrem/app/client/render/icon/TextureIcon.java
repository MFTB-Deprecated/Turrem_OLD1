package net.turrem.app.client.render.icon;

import net.turrem.app.client.render.RenderEngine;
import net.turrem.app.client.render.object.RenderObjectTexture;

public class TextureIcon extends RenderIcon2D
{
	private final String name;
	public final boolean isPixelArt;
	public RenderObjectTexture object;
	
	public TextureIcon(String name, boolean isPixelArt)
	{
		this.name = name;
		this.isPixelArt = isPixelArt;
	}
	
	@Override
	public void load(RenderEngine engine)
	{
		this.object = engine.renderStore2D.loadObject(this);
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
	
	public float getAspect()
	{
		if (this.loaded())
		{
			return this.object.getAspect();
		}
		return Float.NaN;
	}
	
	public int getWidth()
	{
		if (this.loaded())
		{
			return this.object.getWidth();
		}
		return -1;
	}
	
	public int getHeight()
	{
		if (this.loaded())
		{
			return this.object.getHeight();
		}
		return -1;
	}
	
	@Override
	public String getSource()
	{
		return this.name;
	}
	
	@Override
	public String getIdentifier()
	{
		return this.name + (this.isPixelArt ? "[pix1" : "[pix0");
	}
}
