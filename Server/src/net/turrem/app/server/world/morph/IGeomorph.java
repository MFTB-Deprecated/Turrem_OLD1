package net.turrem.app.server.world.morph;

import java.util.Random;

import net.turrem.app.server.world.mesh.VertexGenData;
import net.turrem.app.server.world.mesh.VertexGenDataWork;
import net.turrem.app.server.world.mesh.WorldVertex;

public interface IGeomorph
{
	public String getId();
	
	public int getOrdering();
	
	public long getSeed(long vertexSeed);
	
	public void generateUpgrade(VertexGenDataWork newData, VertexGenData oldData, WorldVertex vertex, Random rand);
}
