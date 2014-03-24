package net.turrem.client.gui;

import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.font.FontRender;

import org.lwjgl.opengl.GL11;

public class GuiTextTip extends GuiElement
{
	private String text = "";
	private float r = 0.0F;
	private float g = 0.0F;
	private float b = 0.0F;

	protected GuiFrame frame;
	protected FontRender font;
	protected float size;

	public GuiTextTip(FontRender font, String deftxt, float size)
	{
		this.font = font;
		this.text = deftxt;
		this.size = size;
		this.frame = new GuiFrame((int) (deftxt.length() * this.size * this.font.theFont.getAspect() + 16), (int) (this.size + 16), true, true, 0, "turrem.gui.frames.darkflat", 1.5F);
	}

	public void setText(String txt)
	{
		this.frame.width = (int) (txt.length() * this.size * this.font.theFont.getAspect() + 16);
		this.frame.height = (int) (this.size + 16);
		this.text = txt;
	}

	@Override
	public void setPos(int x, int y)
	{
		this.xpos = x;
		this.ypos = y;
		this.frame.setPos(x, y);
	}

	@Override
	public void render()
	{
		this.frame.render();
		GL11.glColor3f(this.r, this.g, this.b);
		this.font.renderText(this.text, this.xpos + 8, this.ypos + 8, this.size);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}

	@Override
	public void onStart(RenderManager manager)
	{
		this.frame.onStart(manager);
	}

	public void setTextColor(float r, float g, float b)
	{
		this.r = r;
		this.b = b;
		this.g = g;
	}
}
