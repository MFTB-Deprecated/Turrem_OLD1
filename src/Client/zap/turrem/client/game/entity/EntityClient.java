package zap.turrem.client.game.entity;

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

	public EntityClient(EntityArticle article)
	{
		this.article = article;
	}
	
	public void push(WorldClient world, RenderManager man)
	{
		this.article.loadAssets(man);
		world.entityList.add(this);
	}
	
	public void render()
	{
		this.article.draw(this);
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
		super.onTick();
		this.article.clientTick(this);
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
}