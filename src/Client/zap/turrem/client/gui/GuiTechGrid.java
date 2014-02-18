package zap.turrem.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import org.lwjgl.input.Mouse;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.font.FontRender;
import zap.turrem.client.render.texture.TextureIcon;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;

public class GuiTechGrid extends GuiElement implements IInteractable
{
	public static TextureIcon[] techicons;

	public ArrayList<TechItem> grid = new ArrayList<TechItem>();
	public GuiTextTip techtip;
	public FontRender font;

	public int tilesize;
	public int tileedge;
	public int gridw;
	public int gridh;

	public GuiTechGrid(FontRender font, int tilesize, int tileedge, int gridw, int gridh, int xpos, int ypos)
	{
		this.font = font;
		this.tilesize = tilesize;
		this.tileedge = tileedge;
		this.gridw = gridw;
		this.gridh = gridh;
		int full = this.tilesize + this.tileedge * 2;
		this.setSize(this.gridw * full, this.gridh * full);
		this.setPos(xpos, ypos);
	}

	@Override
	public void onStart(RenderManager manager)
	{
		if (techicons == null)
		{
			techicons = new TextureIcon[TechList.getSize()];

			TextureIcon nulltech = new TextureIcon("core.gui.nullTech");
			nulltech.load(manager);

			for (int i = 0; i < techicons.length; i++)
			{
				TechItem ti = TechList.get(i);
				String[] tech = ti.getIdentifier().split("\\.");
				String mod = tech[1];
				String tstr = tech[0];
				tech[0] = mod;
				tech[1] = tstr;
				tech[tech.length - 1] = tech[tech.length - 1].replace('#', '_');
				String texture = "";
				for (int j = 0; j < tech.length; j++)
				{
					String strp = tech[j];
					if (j != 0)
					{
						texture += ".";
					}
					texture += strp;
				}
				System.out.println(texture);
				if (manager.assets.doesTextureFileExist(texture))
				{
					techicons[i] = new TextureIcon(texture);
					try
					{
						techicons[i].load(manager);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					techicons[i] = nulltech;
				}
			}
		}

		this.techtip = new GuiTextTip(this.font, "null", 20.0F);
		this.techtip.onStart(manager);
	}

	@Override
	public void render()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		int x  = 0;
		int y = 0;
		int full = this.tilesize + this.tileedge * 2;
		
		int i = 0;
		int j = 0;
		int a = this.tileedge;
		int b = this.tileedge + this.tilesize;
		for (TechItem tech : this.grid)
		{
			TextureIcon techico = techicons[tech.getId()];

			GL11.glPushMatrix();
			techico.start();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex2f(x + a, y + a);
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex2f(x + a, y + b);
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex2f(x + b, y + b);
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex2f(x + b, y + a);
			
			x += full;
			i++;
			
			if (i < this.gridw)
			{
				x = 0;
				y += full;
				j++;
				i = 0;
			}
			
			if (j >= this.gridh)
			{
				break;
			}

			GL11.glEnd();
			techico.end();
			GL11.glPopMatrix();
		}

		int mx = Mouse.getX();
		int my = Mouse.getX();
		
		TechItem mouset = this.getTechAtPos(mx - this.xpos, my - this.ypos);
		
		this.techtip.setPos(mx, my);
		
		if (mouset != null)
		{
			this.techtip.setText(mouset.getName());
			this.techtip.render();
		}
	}

	@Override
	public boolean mouseEvent()
	{
		int x = Mouse.getEventX();
		int y = Mouse.getEventY();
		if (this.isInteractableAt(x, y))
		{
			int ind = this.getTechIndAtPos(x, y);
			if (ind == -1)
			{
				return false;
			}
			TechItem it = this.grid.get(ind);
			this.mouseEventTech(it, ind);
			return true;
		}
		return false;
	}
	
	public void mouseEventTech(TechItem item, int gridIndex)
	{
		
	}

	@Override
	public boolean keyEvent()
	{
		return false;
	}

	@Override
	public boolean isInteractableAt(int x, int y)
	{
		return x >= 0 && y >= 0 && x < this.width && y < this.height;
	}

	@Override
	public boolean isClickSpot(int x, int y)
	{
		return this.getTechIndAtPos(x, y) != -1;
	}

	public int getTechIndAtPos(int x, int y)
	{
		int full = this.tilesize + this.tileedge * 2;
		if (x < 0 || y < 0)
		{
			return -1;
		}
		if (x >= this.width)
		{
			return -1;
		}
		if (y >= this.height)
		{
			return -1;
		}
		int i = (x / full) + (y / full) * this.gridw;
		if (i < this.grid.size() && i >= 0)
		{
			return i;
		}
		return -1;
	}

	public TechItem getTechAtPos(int x, int y)
	{
		int i = this.getTechIndAtPos(x, y);
		if (i != -1)
		{
			return this.grid.get(i);
		}
		return null;
	}
}
