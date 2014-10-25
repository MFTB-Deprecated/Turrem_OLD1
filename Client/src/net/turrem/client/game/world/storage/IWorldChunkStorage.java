package net.turrem.client.game.world.storage;

import java.util.Collection;

import net.turrem.client.game.world.Chunk;

public interface IWorldChunkStorage
{
	public Collection<Chunk> getChunks(Collection<Chunk> list);
	
	public void removeMe(int U, int V);
}
