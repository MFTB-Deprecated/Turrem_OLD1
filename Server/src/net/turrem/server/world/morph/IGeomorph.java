package net.turrem.server.world.morph;

import java.util.Random;

import net.turrem.server.world.mesh.VertexGenData;
import net.turrem.server.world.mesh.VertexGenDataWork;
import net.turrem.server.world.mesh.WorldVertex;

public interface IGeomorph
{
	public String getId();
	
	public int getOrdering();
	
	public long getSeed(long vertexSeed);
	
	public void generateUpgrade(VertexGenDataWork newData, VertexGenData oldData, WorldVertex vertex, Random rand);
}
