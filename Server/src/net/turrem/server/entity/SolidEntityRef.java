package net.turrem.server.entity;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.world.Chunk;
import net.turrem.server.world.storage.WorldStorage;

public class SolidEntityRef extends EntityRef
{
	public int entityIdentifier;
	public int chunkX;
	public int chunkZ;
	
	public SolidEntityRef(int entityIdentifier, int chunkX, int chunkZ)
	{
		this.entityIdentifier = entityIdentifier;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	@Override
	public void write(DataOutput out) throws IOException
	{
		int id = (this.entityIdentifier * -1) - 1;
		out.write(id);
		out.write(this.chunkX);
		out.write(this.chunkZ);
	}

	@Override
	public IEntity getEntity(WorldStorage world)
	{
		Chunk chunk = world.chunks.findChunk(this.chunkX, this.chunkZ);
		if (chunk != null)
		{
			return chunk.entities.getSolidEntity(this.entityIdentifier);
		}
		return null;
	}
	
	public SolidEntity getSolidEntity(WorldStorage world)
	{
		Chunk chunk = world.chunks.findChunk(this.chunkX, this.chunkZ);
		if (chunk != null)
		{
			return chunk.entities.getSolidEntity(this.entityIdentifier);
		}
		return null;
	}
}
