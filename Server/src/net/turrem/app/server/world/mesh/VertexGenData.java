package net.turrem.app.server.world.mesh;

import java.util.Comparator;
import java.util.Set;

import net.turrem.app.server.world.morph.IGeomorph;

import com.google.common.collect.ImmutableSortedSet;

public class VertexGenData
{
	private static class MorphComparator implements Comparator<IGeomorph>
	{
		@Override
		public int compare(IGeomorph morph0, IGeomorph morph1)
		{
			return Integer.compare(morph0.getOrdering(), morph1.getOrdering());
		}
	}
	
	public final float height;
	public ImmutableSortedSet<IGeomorph> morphs;
	
	public VertexGenData(Set<IGeomorph> set, float height)
	{
		this.morphs = ImmutableSortedSet.copyOf(new MorphComparator(), set);
		this.height = height;
	}
	
	public VertexGenData(VertexGenDataWork data)
	{
		this(data.set, data.getHeight());
	}
}