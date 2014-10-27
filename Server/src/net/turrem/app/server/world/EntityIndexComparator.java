package net.turrem.app.server.world;

import java.util.Comparator;

import net.turrem.app.server.entity.IEntity;

public class EntityIndexComparator implements Comparator<IEntity>
{
	@Override
	public int compare(IEntity o1, IEntity o2)
	{
		return Long.compare(o1.getEntityIdentifier(), o2.getEntityIdentifier());
	}
}