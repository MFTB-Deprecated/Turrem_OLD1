package zap.turrem.client.gui;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.font.FontRender;

public class GuiTextTip
{
	public int xpos;
	public int ypos;
	public int width;
	public int height;
	
	private String text = "";
	public float r = 0.0F;
	public float g = 0.0F;
	public float b = 0.0F;
	
	protected GuiFrame frame;
	protected FontRender font;
	protected float size;
	
	public GuiTextTip(FontRender font, String deftxt, float size)
	{
		this.font = font;
		this.text = deftxt;
		this.size = size;
		this.frame = new GuiFrame((int) (deftxt.length() * this.size* this.font.theFont.getAspect() + 16), (int) (this.size + 16), true, true, 0, "turrem.gui.frames.darkflat", 1.5F);
	}
	
	public void setText(String txt)
	{
		this.frame.width = (int) (txt.length() * this.size* this.font.theFont.getAspect() + 16);
		this.frame.height = (int) (this.size + 16);
		this.text = txt;
	}
	
	public void setPos(int x, int y)
	{
		this.xpos = x;
		this.ypos = y;
		this.frame.setPos(x, y);
	}
	
	public void render()
	{
		this.frame.render();
		this.font.renderText(this.text, this.xpos + 8, this.ypos + 8, this.size);
	}
	
	public void onStart(RenderManager manager)
	{
		this.frame.onStart(manager);
	}
}
