package zap.turrem.client.game.world;

public class TerrainGenChunk
{
	public float[] surface;
	public float[] cont;
	public float[] mount;

	public TerrainGenChunk(float[] surface, float[] cont, float[] mount)
	{
		this.surface = surface;
		this.cont = cont;
		this.mount = mount;
	}
	
	public float getH(short x, short y)
	{
		int i = (x % 16) + (y % 16) * 16;
		float su = this.surface[i];
		float mo = this.mount[i];
		float co = this.cont[i];
		mo -= 0.5F;
		mo = mo * mo;
		if (mo < 0.0F)
		{
			mo = 0.0F;
		}
		mo *= 2;
		
		float h = (float) Math.sqrt(((su - 0.5F) + mo + 1.0F) / 2.0F * co);
		return h;
	}

	public float ajustSurface(short x, short y, float peak, float sea, float depth)
	{
		float h = this.getH(x, y);
		if (h > sea)
		{
			float p = peak - h;
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
			float d = h - depth;
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
