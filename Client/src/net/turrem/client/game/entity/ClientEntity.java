package net.turrem.client.game.entity;

import java.io.DataInput;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.turrem.client.game.world.ClientWorld;
import net.turrem.client.network.server.ServerPacketRemoveEntity.EntityRemoveType;

public abstract class ClientEntity
{
	public final long entityId;
	public ClientWorld theWorld;
	
	protected boolean isPresent = true;
	
	private int removeTime = -1;
	
	protected double xPos;
	protected double yPos;
	protected double zPos;
	
	public ClientEntity(long id, ClientWorld world)
	{
		this.entityId = id;
		this.theWorld = world;
		this.theWorld.pushEntity(this);
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
	
	public void readExtraData(DataInput packet) throws IOException
	{
		
	}
	
	public abstract void renderEntity();
	
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
	
	public abstract int onRemove(boolean death);
}
