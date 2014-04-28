package net.turrem.server.entity;

public class EntityTree extends SolidEntity
{
	@Override
	public void onEnter()
	{
	}

	@Override
	public short loadRadius()
	{
		return -1;
	}

	@Override
	public String getEntityType()
	{
		return "tree";
	}
}
