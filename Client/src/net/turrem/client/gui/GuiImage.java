package net.turrem.client.gui;

import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.texture.TextureIcon;

public class GuiImage extends GuiElement
{
	private TextureIcon img;
	private float umin;
	private float vmin;
	private float umax;
	private float vmax;
	
	public GuiImage(int xpos, int ypos, int width, int height, String texture, float umin, float vmin, float umax, float vmax)
	{
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.img = new TextureIcon(texture);
		this.umin = umin;
		this.umax = umax;
		this.vmin = vmin;
		this.vmax = vmax;
	}
	
	@Override
	public void onStart(RenderManager manager)
	{
		this.img.load(manager);
	}

	@Override
	public void render()
	{
		this.drawTexture(0, 0, this.width, this.height, this.umin, this.vmin, this.umax - this.umin, this.vmax - this.vmin, 0, false, false);
	}
}
