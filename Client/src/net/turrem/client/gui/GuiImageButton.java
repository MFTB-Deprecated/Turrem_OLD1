package net.turrem.client.gui;

import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.font.FontRender;

public class GuiImageButton extends GuiButton
{
	protected GuiImage image;
	protected int imgxoff;
	protected int imgyoff;

	public GuiImageButton(int width, int height, int edge, String texture, float scale, GuiImage img)
	{
		super(width, height, edge, texture, scale);
		this.image = img;
		this.imgxoff = img.xpos;
		this.imgyoff = img.ypos;
	}

	@Override
	public void setPos(int x, int y)
	{
		super.setPos(x, y);
		this.image.setPos(x + this.imgxoff, y + this.imgyoff);
	}

	public GuiImageButton(int width, int height, int edge, String texture, float scale, FontRender font, float size, GuiImage img)
	{
		super(width, height, edge, texture, scale, font, size);
		this.image = img;
	}

	@Override
	public void onStart(RenderManager manager)
	{
		super.onStart(manager);
		this.image.onStart(manager);
	}

	@Override
	public void render()
	{
		if (this.mousein)
		{
			this.frame.setEdge(this.edge + 2);
		}
		else
		{
			this.frame.setEdge(this.edge);
		}
		this.frame.render();
		this.image.render();
		if (this.mousein && this.description != null)
		{
			this.description.render();
		}
	}
}
