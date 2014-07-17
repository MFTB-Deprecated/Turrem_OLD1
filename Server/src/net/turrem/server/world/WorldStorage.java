package net.turrem.server.world;

public class WorldStorage
{
	public ChunkStorage chunks;
	public EntityStorage entities;
	public World theWorld;
	
	public WorldStorage(int chunkWidth, int chunkDepth, World world)
	{
		this.theWorld = world;
		this.chunks = new ChunkStorage(chunkWidth, chunkDepth);
		this.entities = new EntityStorage(world);
	}
	
	public void tickEntities()
	{
		this.entities.worldTick();
	}
	
	public void tickChunks()
	{
		
	}
}
