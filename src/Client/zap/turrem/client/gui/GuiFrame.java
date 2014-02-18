package zap.turrem.client.gui;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.texture.TextureIcon;

public class GuiFrame extends GuiElement
{
	private TextureIcon front;
	private TextureIcon back;

	private boolean hasback;
	private boolean backin;
	private int edge;
	private String texture;
	
	private float scale;

	public GuiFrame(int width, int height, boolean hasback, boolean backin, int edge, String texture, float scale)
	{
		this.width = width;
		this.height = height;
		this.hasback = hasback;
		this.backin = backin;
		this.edge = edge;
		this.texture = texture;
		this.scale = scale;

		this.front = new TextureIcon(this.texture);
		if (this.hasback)
		{
			this.back = new TextureIcon(this.texture + "_bk");
		}
	}

	public void renderBack()
	{
		this.back.start();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		float s = 16 * this.scale;
		float es = 3 * this.scale;
		
		float w = width - es * 2;
		float h = height - es * 2;
		
		for (int i = 0; i <= w / s; i++)
		{
			for (int j = 0; j <= h / s; j++)
			{
				float W = (w - i * s);
				if (W > s)
				{
					W = s;
				}
				float X = i * s;
				float H = (h - j * s);
				if (H > s)
				{
					H = s;
				}
				float Y = j * s;

				this.drawTexture(es + X, es + Y, W, H, 0, 0, W / s, H / s, 0, false, false);
			}
		}

		GL11.glEnd();
		this.back.end();
	}

	private void renderEdge()
	{
		this.front.start();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		float s = 6 * this.scale;
		
		float w = this.width - s * 2;
		float h = this.height - s * 2;
		
		float bl = 20 * this.scale;
		
		int tx;
		tx = this.edge;
		for (int i = 0; i <= h / bl; i++)
		{
			float H = (h - i * bl);
			if (H > bl)
			{
				H = bl;
			}
			float Y = i * bl;

			this.drawTexture(0, Y + s, s, H, tx / 4.0F, 6 / 32.0F, 6 / 24.0F, 20 / 32.0F, 0, false, false);
		}

		tx = edge;
		this.drawTexture(0, 0, s, s, tx / 4.0F, 0, 6 / 24.0F, 6 / 32.0F, 0, false, false);

		tx = edge;
		for (int i = 0; i <= w / bl; i++)
		{
			float W = (w - i * bl);
			if (W > bl)
			{
				W = bl;
			}
			float X = i * bl;

			this.drawTexture(X + s, 0, W, s, tx / 4.0F, 6 / 32.0F, 6 / 24.0F, 20 / 32.0F, 1, false, false);
		}

		tx = (edge + 2) % 4;;
		this.drawTexture(0, this.height, s, -s, tx / 4.0F, 26 / 32.0F, 6 / 24.0F, 6 / 32.0F, 1, false, false);

		tx = edge;
		this.drawTexture(this.width, 0, -s, s, tx / 4.0F, 26 / 32.0F, 6 / 24.0F, 6 / 32.0F, 1, false, false);

		tx = (edge + 2) % 4;
		for (int i = 0; i <= h / bl; i++)
		{
			float H = (h - i * bl);
			if (H > bl)
			{
				H = bl;
			}
			float Y = i * bl;

			this.drawTexture(this.width, Y + s, -s, H, tx / 4.0F, 6 / 32.0F, 6 / 24.0F, 20 / 32.0F, 0, false, false);
		}

		tx = (edge + 2) % 4;
		this.drawTexture(this.width, this.height, -s, -s, tx / 4.0F, 0, 6 / 24.0F, 6 / 32.0F, 0, false, false);

		tx = (edge + 2) % 4;
		for (int i = 0; i <= w / bl; i++)
		{
			float W = (w - i * bl);
			if (W > bl)
			{
				W = bl;
			}
			float X = i * bl;

			this.drawTexture(X + s, this.height, W, -s, tx / 4.0F, 6 / 32.0F, 6 / 24.0F, 20 / 32.0F, 1, false, false);
		}

		GL11.glEnd();
		this.front.end();
	}

	@Override
	public void onStart(RenderManager manager)
	{
		this.front.load(manager);
		if (this.back != null)
		{
			this.back.load(manager);
		}
	}

	@Override
	public void render()
	{
		if (this.hasback)
		{
			if (this.backin)
			{
				this.renderBack();
			}
		}
		
		this.renderEdge();
	}
}
