package net.turrem.app.server.world.storage;

import java.util.Collection;

import net.turrem.app.server.world.Chunk;

public interface IWorldChunkStorage
{
	public Collection<Chunk> getChunks(Collection<Chunk> list);
	
	public void removeMe(int U, int V);
}
