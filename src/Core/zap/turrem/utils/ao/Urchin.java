package zap.turrem.utils.ao;

public class Urchin
{
	public AORay[] rays;
	
	public Urchin(int size, int raynum)
	{
		this.rays = new AORay[raynum];
		
		float inc = (float) (Math.PI * (3 - Math.sqrt(5)));
		float off = 2.0F / raynum;
		for (int i = 0; i < this.rays.length; i++)
		{
			float y = raynum * off - 1 + (off / 2);
			float r = (float) Math.sqrt(1 - y * y);
			float phi = i * inc;
			this.rays[i] = new AORay((float) Math.cos(phi) * r, y, (float) Math.sin(phi) * r, size, 0.2F);
		}
	}
}
