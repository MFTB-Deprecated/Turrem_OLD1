package zap.turrem.client.game.entity;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.config.Config;
import zap.turrem.client.game.WorldClient;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Point;

public class EntityClient extends Entity
{
	public boolean isDead = false;
	public boolean isAppear = true;

	public double posX;
	public double posY;
	public double posZ;

	public float motionX;
	public float motionY;
	public float motionZ;
	public int stop;
	public int progress;
	private boolean inMotion;

	public EntityArticle article;

	private boolean selected = false;
	public static boolean deselect = true;
	public static int numselected = 0;

	public EntityClient(EntityArticle article)
	{
		this.article = article;
	}

	public void push(WorldClient world, RenderManager man)
	{
		this.setBounds(this.article.updateBounds());
		this.article.loadAssets(man);
		world.entityList.add(this);
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void render()
	{
		this.article.draw(this);

		if (this.isSelected())
		{
			this.drawBox(1.0F, 0.8F, 0.0F);
		}
		else if (Config.drawBounds)
		{
			this.drawBox(0.0F, 0.0F, 0.0F);
		}
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	public void setSelected(boolean sel)
	{
		if (sel)
		{
			deselect = false;
			if (!this.selected)
			{
				numselected++;
			}
		}
		else
		{
			if (this.selected)
			{
				numselected--;
			}
		}
		this.selected = sel;
	}

	public void disappear()
	{
		this.isAppear = false;
	}

	public void kill()
	{
		this.isDead = true;
		if (this.selected)
		{
			this.selected = false;
			numselected--;
		}
	}

	public void onTick()
	{
		if (deselect)
		{
			this.selected = false;
			numselected = 0;
		}
		super.onTick();
		this.article.clientTick(this);
		this.doMotion();
	}

	public void doMotion()
	{
		if (this.inMotion)
		{
			if (this.progress < this.stop)
			{
				this.progress++;
				this.posX += this.motionX;
				this.posY += this.motionY;
				this.posZ += this.motionZ;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
			else
			{
				this.cancelMotion();
			}
		}
	}

	public Point getLocation()
	{
		return Point.getPoint(this.posX, this.posY, this.posZ);
	}

	public void setMotion(Point start, Point end, int time)
	{
		this.setPosition(start);
		this.progress = 0;
		this.motionX = (float) (end.xCoord - start.xCoord) / time;
		this.motionY = (float) (end.yCoord - start.yCoord) / time;
		this.motionZ = (float) (end.zCoord - start.zCoord) / time;
		this.inMotion = true;
		this.stop = time;
	}

	public void cancelMotion()
	{
		this.inMotion = false;
		this.motionX = 0.0F;
		this.motionY = 0.0F;
		this.motionZ = 0.0F;
		this.progress = 0;
	}

	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		super.setPosition(x, y, z);
	}

	public void setPosition(Point p)
	{
		this.setPosition(p.xCoord, p.yCoord, p.zCoord);
	}

	public void drawBox(float r, float g, float b)
	{
		GL11.glPushMatrix();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_QUADS);

		float xmax = (float) this.boundingBox.maxX;
		float ymax = (float) this.boundingBox.maxY;
		float zmax = (float) this.boundingBox.maxZ;
		float xmin = (float) this.boundingBox.minX;
		float ymin = (float) this.boundingBox.minY;
		float zmin = (float) this.boundingBox.minZ;

		GL11.glVertex3f(xmax, ymax, zmin);
		GL11.glVertex3f(xmin, ymax, zmin);
		GL11.glVertex3f(xmin, ymax, zmax);
		GL11.glVertex3f(xmax, ymax, zmax);

		GL11.glVertex3f(xmax, ymin, zmax);
		GL11.glVertex3f(xmin, ymin, zmax);
		GL11.glVertex3f(xmin, ymin, zmin);
		GL11.glVertex3f(xmax, ymin, zmin);

		GL11.glVertex3f(xmax, ymax, zmax);
		GL11.glVertex3f(xmin, ymax, zmax);
		GL11.glVertex3f(xmin, ymin, zmax);
		GL11.glVertex3f(xmax, ymin, zmax);

		GL11.glVertex3f(xmax, ymin, zmin);
		GL11.glVertex3f(xmin, ymin, zmin);
		GL11.glVertex3f(xmin, ymax, zmin);
		GL11.glVertex3f(xmax, ymax, zmin);

		GL11.glVertex3f(xmin, ymax, zmax);
		GL11.glVertex3f(xmin, ymax, zmin);
		GL11.glVertex3f(xmin, ymin, zmin);
		GL11.glVertex3f(xmin, ymin, zmax);

		GL11.glVertex3f(xmax, ymax, zmin);
		GL11.glVertex3f(xmax, ymax, zmax);
		GL11.glVertex3f(xmax, ymin, zmax);
		GL11.glVertex3f(xmax, ymin, zmin);

		GL11.glEnd();
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}