package net.turrem.server.world.gen;

import net.turrem.server.TurremServer;
import net.turrem.server.world.Chunk;

public abstract class WorldGen
{
	public abstract void decorateChunk(Chunk chunk, TurremServer turrem);
	
	public abstract Chunk generateChunk(int chunkx, int chunkz);
}
