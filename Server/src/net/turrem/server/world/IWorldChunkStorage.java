package net.turrem.server.world;

import java.util.Collection;

public interface IWorldChunkStorage
{	
	public Collection<Chunk> getChunks(Collection<Chunk> list);
}
