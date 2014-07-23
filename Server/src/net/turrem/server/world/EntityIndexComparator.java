package net.turrem.server.world;

import java.util.Comparator;

import net.turrem.server.entity.IEntity;

public class EntityIndexComparator implements Comparator<IEntity>
{
	@Override
	public int compare(IEntity o1, IEntity o2)
	{
		return Long.compare(o1.getEntityIdentifier(), o2.getEntityIdentifier());
	}
}