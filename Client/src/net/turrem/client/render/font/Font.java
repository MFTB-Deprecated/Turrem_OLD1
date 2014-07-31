package net.turrem.client.render.font;

import net.turrem.client.render.RenderEngine;
import net.turrem.client.render.icon.TextureIcon;

public class Font
{
	protected float aspect;

	protected TextureIcon ico;

	public int width;
	public int height;

	public final String name;

	public Font(String name)
	{
		this.name = name;
	}

	public void loadTexture(String texture, RenderEngine render)
	{
		this.ico = new TextureIcon(texture, true);
		this.ico.load(render);
		this.aspect = this.ico.getAspect();
		this.height = this.ico.getHeight();
		this.width = this.ico.getWidth();
	}

	public final float getAspect()
	{
		return this.aspect;
	}

	public void start()
	{
		if (this.ico != null)
		{
			this.ico.start();
		}
	}

	public void end()
	{
		if (this.ico != null)
		{
			this.ico.end();
		}
	}
}
