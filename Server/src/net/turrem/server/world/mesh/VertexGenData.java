package net.turrem.server.world.mesh;

import java.util.Set;

import net.turrem.server.world.morph.IGeomorph;

import com.google.common.collect.ImmutableSet;

public class VertexGenData
{	
	public final float height;
	public ImmutableSet<IGeomorph> morphs;
	
	public VertexGenData(Set<IGeomorph> set, float height)
	{
		this.morphs = ImmutableSet.copyOf(set);
		this.height = height;
	}
	
	public VertexGenData(VertexGenDataWork data)
	{
		this(data.set, data.getHeight());
	}
}