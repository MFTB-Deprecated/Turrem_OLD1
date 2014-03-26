package net.turrem.client.gui;

import org.lwjgl.opengl.GL11;

public abstract class GuiElement implements IElement
{
	protected int xpos;
	protected int ypos;
	protected int width;
	protected int height;

	protected void drawTexture(float x, float y, float w, float h, float u, float v, float tw, float th, int rot, boolean flipx, boolean flipy)
	{
		if (flipx)
		{
			u += tw;
			tw = -tw;
		}

		if (flipy)
		{
			v += th;
			th = -th;
		}

		x = x + this.xpos;
		y = y + this.ypos;

		for (int i = 0; i < 4; i++)
		{
			switch (i % 4)
			{
				case 0:
					GL11.glTexCoord2f(u, v);
					break;
				case 1:
					GL11.glTexCoord2f(u + tw, v);
					break;
				case 2:
					GL11.glTexCoord2f(u + tw, v + th);
					break;
				case 3:
					GL11.glTexCoord2f(u, v + th);
					break;
			}

			switch ((i + rot) % 4)
			{
				case 0:
					GL11.glVertex2f(x, y);
					break;
				case 1:
					GL11.glVertex2f(x + w, y);
					break;
				case 2:
					GL11.glVertex2f(x + w, y + h);
					break;
				case 3:
					GL11.glVertex2f(x, y + h);
					break;
			}
		}
	}

	@Override
	public int getXPos()
	{
		return this.xpos;
	}

	@Override
	public int getYPos()
	{
		return this.ypos;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	public void setPos(int x, int y)
	{
		this.xpos = x;
		this.ypos = y;
	}

	@Override
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
