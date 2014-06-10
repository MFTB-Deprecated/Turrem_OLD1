package net.turrem.client.game.entity;

import org.lwjgl.opengl.GL11;

import net.turrem.client.game.world.ClientWorld;
import net.turrem.client.network.server.ServerPacketRemoveEntity.EntityRemoveType;
import net.turrem.client.render.engine.RenderManager;
import net.turrem.utils.nbt.NBTCompound;

public abstract class ClientEntity
{
	public final long entityId;
	public ClientWorld theWorld;

	protected boolean isPresent = true;

	private int removeTime = -1;

	protected double xPos;
	protected double yPos;
	protected double zPos;

	protected int modelNumber = -1;

	protected double moveX;
	protected double moveY;
	protected double moveZ;

	protected int moveTime = 0;

	public ClientEntity(long id, ClientWorld world)
	{
		this.entityId = id;
		this.theWorld = world;
	}

	public boolean isPresent()
	{
		return this.isPresent;
	}

	public void setPosition(double x, double y, double z)
	{
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
	}

	public abstract void renderEntity();
	
	public abstract void loadAssets(RenderManager man);

	public void render()
	{
		if (this.removeTime > 0)
		{
			this.removeTime--;
		}
		if (this.removeTime == 0)
		{
			this.isPresent = false;
			this.removeTime = -1;
		}

		if (this.moveTime > 0)
		{
			this.xPos += this.moveX;
			this.yPos += this.moveY;
			this.zPos += this.moveZ;
			this.moveTime--;
		}

		GL11.glPushMatrix();
		GL11.glTranslated(this.xPos, this.yPos, this.zPos);
		this.renderEntity();
		GL11.glPopMatrix();
	}

	public void remove(EntityRemoveType reason)
	{
		if (reason == EntityRemoveType.KILL)
		{
			this.removeTime = this.onRemove(true);
		}
		else
		{
			this.onRemove(false);
			this.isPresent = false;
		}
	}
	
	public void onOverride()
	{
		this.onRemove(false);
		this.isPresent = false;
	}

	public abstract int onRemove(boolean death);
	
	public int getModelNumber(int mod)
	{
		return ((this.modelNumber % mod) + mod) % mod;
	}

	public void setMove(float x, float y, float z, int time)
	{
		if (time == 0)
		{
			this.xPos = x;
			this.yPos = y;
			this.zPos = z;
			this.moveTime = 0;
		}
		else
		{
			double dx = x - this.xPos;
			double dy = y - this.yPos;
			double dz = z - this.zPos;
			this.moveX = dx / time;
			this.moveY = dy / time;
			this.moveZ = dz / time;
			this.moveTime = time;
		}
	}

	public void readNBT(NBTCompound data)
	{
		this.modelNumber = data.getInt("ModelNumber");
	}
}
