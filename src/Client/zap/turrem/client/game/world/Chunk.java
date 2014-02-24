package zap.turrem.client.game.world;

public class Chunk
{
	public final int chunkx;
	public final int chunky;
	
	public Chunk(int chunkx, int chunky)
	{
		this.chunkx = chunkx;
		this.chunky = chunky;
	}
	
	public int getIndex(int x, int y)
	{
		return x % 16 * 16 + y % 16;
	}
	
	public void renderChunk()
	{
		
	}
}
