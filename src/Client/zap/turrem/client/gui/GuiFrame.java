package zap.turrem.client.gui;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.texture.TextureIcon;

public class GuiFrame
{
	public TextureIcon edge = new TextureIcon("core.gui.guiedge");

	public void render()
	{

	}

	public void renderEdge(boolean dir, boolean side, int width, int height, int type)
	{
		this.edge.start();
		GL11.glBegin(GL11.GL_QUADS);

		int w = width - 12;
		int h = height - 12;
		
		int tx;
		tx = type;
		for (int i = 0; i <= h / 20; i++)
		{
			float H = (h - i * 20);
			if (H > 20)
			{
				H = 20;
			}
			float Y = i * 20;
			
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(0, Y + 6);
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(0, Y + H + 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(6, Y + H + 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(6, Y + 6);
		}
		
		tx = type;
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 0.0F / 32.0F);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 0.0F / 32.0F);
		GL11.glVertex2f(6, 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
		GL11.glVertex2f(6, 6);
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
		GL11.glVertex2f(0, 6);
		
		tx = type;
		for (int i = 0; i <= w / 20; i++)
		{
			float W = (w - i * 20);
			if (W > 20)
			{
				W = 20;
			}
			float X = i * 20;
			
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(X + 6, 0);
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(X + W + 6, 0);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(X + W + 6, 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(X + 6, 6);
		}
		
		tx = type ;
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 32.0F / 32.0F);
		GL11.glVertex2f(0, height - 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 32.0F / 32.0F);
		GL11.glVertex2f(6, height - 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
		GL11.glVertex2f(6, height - 6);
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
		GL11.glVertex2f(0, height - 6);
		
		tx = (type + 2) % 4;
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 32.0F / 32.0F);
		GL11.glVertex2f(width - 0, 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 32.0F / 32.0F);
		GL11.glVertex2f(width - 6, 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
		GL11.glVertex2f(width - 6, 6);
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
		GL11.glVertex2f(width - 0, 6);
		
		tx = (type + 2) % 4;
		for (int i = 0; i <= h / 20; i++)
		{
			float H = (h - i * 20);
			if (H > 20)
			{
				H = 20;
			}
			float Y = i * 20;
			
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(width - 0, Y + 6);
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(width - 0, Y + H + 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(width - 6, Y + H + 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(width - 6, Y + 6);
		}
		
		tx = (type + 2) % 4;
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 0.0F / 32.0F);
		GL11.glVertex2f(width - 0, height - 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 0.0F / 32.0F);
		GL11.glVertex2f(width - 6, height - 0);
		GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
		GL11.glVertex2f(width - 6, height - 6);
		GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
		GL11.glVertex2f(width - 0, height - 6);
		
		tx = (type + 2) % 4;
		for (int i = 0; i <= w / 20; i++)
		{
			float W = (w - i * 20);
			if (W > 20)
			{
				W = 20;
			}
			float X = i * 20;
			
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(X + 6, height);
			GL11.glTexCoord2f((0.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(X + W + 6, height);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 26.0F / 32.0F);
			GL11.glVertex2f(X + W + 6, height - 6);
			GL11.glTexCoord2f((6.0F + tx * 6) / 24.0F, 6.0F / 32.0F);
			GL11.glVertex2f(X + 6, height - 6);
		}
		
		GL11.glEnd();
		this.edge.end();
	}

	public void loadAssets(RenderManager manager)
	{
		this.edge.load(manager);
	}
}
