package net.turrem.server.world.biome;

import net.turrem.mod.ModInstance;
import net.turrem.server.world.morph.IGeomorph;

public abstract class Biome implements IGeomorph
{
	private final String id;
	ModInstance mod = null;
	
	public Biome(String id)
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
		return ("biome:" + this.getId()).hashCode();
	}
}
