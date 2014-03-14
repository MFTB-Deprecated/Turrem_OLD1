package zap.turrem.core.entity;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.config.Config;
import zap.turrem.client.game.world.WorldClient;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.utils.geo.Box;
import zap.turrem.utils.geo.Point;

public abstract class Entity
{
	public static long nextUID = 0;

	public boolean isDead = false;
	public boolean isAppear = true;

	public double posX;
	public double posY;
	public double posZ;

	public final long uid;

	public WorldClient theWorld;

	public int rotation;
	public float yawangle = 0.0F;

	public Entity()
	{
		this.uid = nextUID++;
	}

	public Box getPickBounds()
	{
		Box b = this.pickBounds();
		if (b != null)
		{
			return b.moveNew(this.posX, this.posY, this.posZ).yawThis(this.rotation, this.posX, this.posY, this.posZ);
		}
		return Box.getBox(-0.5F, 0.0F, -0.5F, 0.5F, 1.0F, 0.5F).moveThis(this.posX, this.posY, this.posZ).yawThis(this.rotation, this.posX, this.posY, this.posZ);
	}

	public void push(WorldClient world, RenderManager man)
	{
		this.loadAssets(man);
		world.entityList.add(this);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.theWorld = world;
	}

	public void keyEvent(boolean me)
	{

	}

	public void mouseEvent(boolean me)
	{

	}

	public void loadAssets(RenderManager man)
	{
		
	}

	public abstract Box pickBounds();

	public void render()
	{
		GL11.glPushMatrix();
		GL11.glTranslated(this.posX, this.posY, this.posZ);
		GL11.glRotatef(this.rotation * 90 + this.yawangle, 0.0F, 1.0F, 0.0F);
		this.renderEntity();
		GL11.glPopMatrix();

		if (Config.drawBounds)
		{
			this.drawBox(0.0F, 0.0F, 0.0F);
		}
	}

	public void disappear()
	{
		this.isAppear = false;
	}

	public void kill()
	{
		this.isDead = true;
	}

	public void onTick()
	{

	}

	public Point getLocation()
	{
		return Point.getPoint(this.posX, this.posY, this.posZ);
	}

	public void move(double dx, double dy, double dz)
	{
		this.setPosition(this.posX + dx, this.posY + dy, this.posZ + dz);
	}

	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public void setPosition(Point p)
	{
		this.setPosition(p.xCoord, p.yCoord, p.zCoord);
	}

	public void drawBox(float r, float g, float b)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_QUADS);

		Box box = this.getPickBounds();

		float xmax = (float) box.maxX;
		float ymax = (float) box.maxY;
		float zmax = (float) box.maxZ;
		float xmin = (float) box.minX;
		float ymin = (float) box.minY;
		float zmin = (float) box.minZ;

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
		GL11.glPopMatrix();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Entity)
		{
			return ((Entity) obj).uid == this.uid;
		}
		return false;
	}

	protected abstract void renderEntity();

	protected void drawAnIcon(float x, float y, float z, ModelIcon icon)
	{
		GL11.glTranslatef(x, y, z);
		icon.render();
		GL11.glTranslatef(-x, -y, -z);
	}

	protected void drawAnIcon(double x, double y, double z, ModelIcon icon)
	{
		GL11.glTranslated(x, y, z);
		icon.render();
		GL11.glTranslated(-x, -y, -z);
	}
}