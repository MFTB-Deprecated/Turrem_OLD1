package net.turrem.client.render.font;

import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.texture.TextureIcon;

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

	public void loadTexture(String texture, RenderManager manager)
	{
		this.ico = new TextureIcon(texture);
		this.ico.load(manager);
		this.aspect = this.ico.getAspect();
		this.height = this.ico.getHeight();
		this.width = this.ico.getWidth();
	}

	public void push()
	{
		FontList.fonts.put(this.name, this);
	}

	public void unload()
	{
		if (this.ico != null)
		{
			this.ico.unload();
		}
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
