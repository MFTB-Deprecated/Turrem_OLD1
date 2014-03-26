package net.turrem.utils.ao;

public class Urchin
{
	public AORay[] rays;

	public float xu = 0.0F;
	public float xd = 0.0F;
	public float yu = 0.0F;
	public float yd = 0.0F;
	public float zu = 0.0F;
	public float zd = 0.0F;

	public Urchin(int size, int raynum)
	{
		this.rays = new AORay[raynum];

		float inc = (float) (Math.PI * (3 - Math.sqrt(5)));
		float off = 2.0F / raynum;
		for (int i = 0; i < this.rays.length; i++)
		{
			float y = i * off - 1 + (off / 2);
			float r = (float) Math.sqrt(1 - y * y);
			float phi = i * inc;
			AORay ray = new AORay((float) Math.cos(phi) * r, y, (float) Math.sin(phi) * r, size, 0.2F);
			this.rays[i] = ray;
			this.xu += ray.xu;
			this.xd += ray.xd;
			this.yu += ray.yu;
			this.yd += ray.yd;
			this.zu += ray.zu;
			this.zd += ray.zd;
		}
	}
}
