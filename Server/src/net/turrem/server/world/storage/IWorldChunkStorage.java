package net.turrem.server.world.storage;

import java.util.Collection;

import net.turrem.server.world.Chunk;

public interface IWorldChunkStorage
{	
	public Collection<Chunk> getChunks(Collection<Chunk> list);
	
	public void removeMe(int U, int V);
}
