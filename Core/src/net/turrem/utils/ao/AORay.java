package net.turrem.utils.ao;

public class AORay
{
	public static class Offset
	{
		public float depth;
		public int x;
		public int y;
		public int z;
	}

	public Offset[] points;

	public float xu;
	public float xd;
	public float yu;
	public float yd;
	public float zu;
	public float zd;

	public AORay(float x, float y, float z, int size, float inc)
	{
		float l = (float) Math.sqrt(x * x + y * y + z * z);
		x /= l;
		y /= l;
		z /= l;
		float dx = x * inc;
		float dy = y * inc;
		float dz = z * inc;

		this.xu = x < 0 ? -x : 0.0F;
		this.xd = x > 0 ? x : 0.0F;
		this.yu = y < 0 ? -y : 0.0F;
		this.yd = y > 0 ? y : 0.0F;
		this.zu = z < 0 ? -z : 0.0F;
		this.zd = z > 0 ? z : 0.0F;

		this.points = new Offset[size];

		int cx = 0;
		int cy = 0;
		int cz = 0;
		int i = 0;
		float xpos = 0.0F;
		float ypos = 0.0F;
		float zpos = 0.0F;
		float d = 0.0F;
		while (i < size)
		{
			xpos += dx;
			ypos += dy;
			zpos += dz;
			d += inc;

			int xi = (int) xpos;
			int yi = (int) ypos;
			int zi = (int) zpos;

			if (xi != cx || yi != cy || zi != cz)
			{
				cx = xi;
				cy = yi;
				cz = zi;
				Offset o = new Offset();
				o.x = xi;
				o.y = yi;
				o.z = zi;
				o.depth = d;
				this.points[i] = o;
				i++;
			}
		}
	}
}
