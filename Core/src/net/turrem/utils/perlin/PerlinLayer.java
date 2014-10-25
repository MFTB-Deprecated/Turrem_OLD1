package net.turrem.utils.perlin;

public abstract class PerlinLayer implements IPerlinLayer
{
	protected IPerlinGroup parent;
	
	protected Perlin perlin;
	protected int layer;
	protected byte corner;
	
	protected int xpos;
	protected int ypos;
	
	public PerlinLayer(PerlinWorld parent, Perlin perlin, long seed, int xpos, int ypos)
	{
		
	}
	
	public PerlinLayer(PerlinLayer parent, Perlin perlin, int layer, byte corner, long seed, int xpos, int ypos)
	{
		
	}
	
	protected PerlinLayer()
	{
		
	}
	
	public abstract float getValue(int x, int y, byte corner);
	
	public abstract void buildGrid();
	
	@Override
	public abstract float[] getChunk(float u, float v);
	
	public abstract void removeme(byte corner);
}
