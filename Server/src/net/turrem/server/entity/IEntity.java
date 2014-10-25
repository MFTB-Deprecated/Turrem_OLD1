package net.turrem.server.entity;

public interface IEntity
{
	public int getEntityIdentifier();
	
	public boolean isAlive();
	
	public void kill();
}
