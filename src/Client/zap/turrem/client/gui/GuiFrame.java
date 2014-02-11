package zap.turrem.client.gui;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.texture.TextureIcon;

public class GuiFrame
{
	public TextureIcon edge = new TextureIcon("core.gui.guiedge");
	public TextureIcon back = new TextureIcon("core.gui.guiback");

	public void render()
	{

	}

	
	public void renderBack(int width, int height)
	{
		this.back.start();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		int w = width - 6;
		int h = height - 6;
		
		for (int i = 0; i <= w / 32; i++)
		{
			for (int j = 0; j <= h / 32; j++)
			{
				float W = (w - i * 32);
				if (W > 32)
				{
					W = 32;
				}
				float X = i * 32;
				float H = (h - j * 32);
				if (H > 32)
				{
					H = 32;
				}
				float Y = j * 32;
				
				GL11.glTexCoord2f(0.0F / 32.0F, 0.0F / 32.0F);
				GL11.glVertex2f(X + 3, Y + 3);
				GL11.glTexCoord2f(0.0F / 32.0F, H / 32.0F);
				GL11.glVertex2f(X + 3, Y + H + 3);
				GL11.glTexCoord2f(W / 32.0F, 0.0F / 32.0F);
				GL11.glVertex2f(X + W + 3, Y + H + 3);
				GL11.glTexCoord2f(W / 32.0F, H / 32.0F);
				GL11.glVertex2f(X + W + 3, Y + 3);
			}
		}
		
		GL11.glEnd();
		this.back.end();
	}
	
	public void renderEdge(int width, int height, int type)
	{
		this.edge.start();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

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
		this.back.load(manager);
	}
}
