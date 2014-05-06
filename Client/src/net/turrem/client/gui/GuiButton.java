package net.turrem.client.gui;

import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.font.FontRender;

public class GuiButton extends GuiElement implements IInteractable
{
	protected GuiFrame frame;
	protected int edge;
	protected boolean mousein;

	protected GuiTextTip description = null;

	public GuiButton(int width, int height, int edge, String texture, float scale)
	{
		this.width = width;
		this.height = height;
		this.edge = edge;
		this.frame = new GuiFrame(width, height, true, true, edge, texture, scale);
	}

	public GuiButton(int width, int height, int edge, String texture, float scale, FontRender font, String description, float size)
	{
		this(width, height, edge, texture, scale);
		this.description = new GuiTextTip(font, description, size);
	}

	public void setDescription(String text, float r, float g, float b)
	{
		this.description.setText(text);
		this.description.setTextColor(r, g, b);
	}

	@Override
	public void setPos(int x, int y)
	{
		this.xpos = x;
		this.ypos = y;
		this.frame.setPos(x, y);
	}

	@Override
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.frame.setSize(width, height);
	}

	@Override
	public void onStart(RenderManager manager)
	{
		this.frame.onStart(manager);
		if (this.description != null)
		{
			this.description.onStart(manager);
		}
	}

	@Override
	public void render()
	{
		if (this.mousein)
		{
			if (this.description != null)
			{
				this.description.render();
			}
			this.frame.setEdge(this.edge + 2);
		}
		else
		{
			this.frame.setEdge(this.edge);
		}
		this.frame.render();
	}

	@Override
	public boolean mouseEvent()
	{
		return false;
	}

	@Override
	public boolean keyEvent()
	{
		return false;
	}

	@Override
	public boolean mouseAt(int x, int y)
	{
		this.mousein = this.isClickSpot(x, y);
		if (this.mousein && this.description != null)
		{
			this.description.setPos(x, y);
		}
		return this.mousein;
	}

	@Override
	public boolean isClickSpot(int x, int y)
	{
		return x >= this.xpos && x < this.width + this.xpos && y >= this.ypos && y < this.height + this.ypos;
	}

	@Override
	public boolean isInteractableAt(int x, int y)
	{
		return this.isClickSpot(x, y);
	}
}
