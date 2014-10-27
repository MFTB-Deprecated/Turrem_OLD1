package net.turrem.app.client.render.font;

import org.lwjgl.opengl.GL11;

public class FontRender
{
	public Font theFont;
	public float size;
	
	public FontRender(Font font)
	{
		this.theFont = font;
	}
	
	private void start()
	{
		this.theFont.start();
		GL11.glBegin(GL11.GL_QUADS);
	}
	
	private void end()
	{
		GL11.glEnd();
		this.theFont.end();
	}
	
	public void renderText(String text, float x, float y, float size)
	{
		this.size = size;
		this.doRenderText(text, x, y);
	}
	
	public void renderTextCentered(String text, float x, float y, float size)
	{
		x -= (this.theFont.aspect * this.size) * text.length() / 2;
		this.renderText(text, x, y, size);
	}
	
	private void doRenderText(String txt, float x, float y)
	{
		this.start();
		float w = this.theFont.aspect * this.size;
		float h = this.size;
		float X = x;
		float Y = y;
		for (int i = 0; i < txt.length(); i++)
		{
			char car = txt.charAt(i);
			if (car == '\n')
			{
				X = x;
				Y += h;
			}
			else
			{
				int c = (car) % 256;
				this.drawChar(c, X, Y);
				X += w;
			}
		}
		this.end();
	}
	
	private void drawChar(int c, float x, float y)
	{
		float w = this.theFont.aspect * this.size;
		float h = this.size;
		float X = x + w;
		float Y = y + h;
		
		float sex = 1.0F / 16.0F;
		
		float i = (c % 16) / 16.0F;
		float j = (c / 16) / 16.0F;
		float I = i + sex;
		float J = j + sex;
		
		GL11.glTexCoord2f(i, j);
		GL11.glVertex2f(x, y);
		
		GL11.glTexCoord2f(i, J);
		GL11.glVertex2f(x, Y);
		
		GL11.glTexCoord2f(I, J);
		GL11.glVertex2f(X, Y);
		
		GL11.glTexCoord2f(I, j);
		GL11.glVertex2f(X, y);
	}
}
