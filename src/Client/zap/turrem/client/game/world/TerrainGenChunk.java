package zap.turrem.client.game.world;

public class TerrainGenChunk
{
	public float[] surface;
	public float[] water;
	public float[] dry;

	public float[] heat;
	public float[] humid;

	public TerrainGenChunk(float[] surface, float[] water, float[] dry, float[] heat, float[] humid)
	{
		this.surface = surface;
		this.water = water;
		this.dry = dry;
		this.heat = heat;
		this.humid = humid;
	}

	public float getRiver(short x, short y, float enviorment, float width, float arid, float point)
	{
		int i = (x % 16) + (y % 16) * 16;
		float riv = Math.abs(this.water[i] - enviorment);
		if (riv < width - this.dry[i] * point)
		{
			if (this.dry[i] > arid)
			{
				return 1.0F - (riv / width);
			}
		}
		return 0.0F;
	}

	public float ajustSurface(short x, short y, float peak, float sea, float depth)
	{
		int i = (x % 16) + (y % 16) * 16;
		if (this.surface[i] > sea)
		{
			float p = peak - this.surface[i];
			if (p < 0)
			{
				p = 0.0F;
			}
			p /= peak - sea;
			p = 1.0F - p;
			return p;
		}
		else
		{
			float d = this.surface[i] - depth;
			if (d < 0)
			{
				d = 0.0F;
			}
			d /= sea - depth;
			d = 1.0F - d;
			return -d;
		}
	}
}
