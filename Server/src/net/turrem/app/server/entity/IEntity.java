package net.turrem.app.server.entity;

public interface IEntity
{
	public int getEntityIdentifier();
	
	public boolean isAlive();
	
	public void kill();
}
