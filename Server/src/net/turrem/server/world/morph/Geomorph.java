package net.turrem.server.world.morph;

import net.turrem.mod.ModInstance;

public abstract class Geomorph implements IGeomorph
{
	private final String id;
	ModInstance mod = null;
	
	public Geomorph(String id)
	{
		this.id = id;
	}
	
	@Override
	public String getId()
	{
		return this.mod.identifier + ":" + this.id;
	}
	
	public String getInternalId()
	{
		return this.id;
	}
	
	public ModInstance getMod()
	{
		return this.mod;
	}
	
	@Override
	public int getOrdering()
	{
		return this.getId().hashCode();
	}
	
	@Override
	public long getSeed(long vertexSeed)
	{
		return vertexSeed ^ this.getOrdering();
	}
	
	@Override
	public int hashCode()
	{
		return ("morph:" + this.getId()).hashCode();
	}
}
