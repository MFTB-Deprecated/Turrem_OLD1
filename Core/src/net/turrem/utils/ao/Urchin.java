package net.turrem.utils.ao;

/**
 * Urchin is a ball of 'evenly' spaced rays expanding out from an origin. Named because a sea urchin has lots of outward pointing spikes.
 */
public class Urchin
{
	public AORay[] rays;

	/**The sum of the magnitude of each ray in the positive X direction. Used to create an average magnitude.*/
	public float xu = 0.0F; 
	/**The sum of the magnitude of each ray in the negative X direction. Used to create an average magnitude.*/
	public float xd = 0.0F;
	/**The sum of the magnitude of each ray in the positive Y direction. Used to create an average magnitude.*/
	public float yu = 0.0F;
	/**The sum of the magnitude of each ray in the negative Y direction. Used to create an average magnitude.*/
	public float yd = 0.0F;
	/**The sum of the magnitude of each ray in the positive Z direction. Used to create an average magnitude.*/
	public float zu = 0.0F;
	/**The sum of the magnitude of each ray in the negative Z direction. Used to create an average magnitude.*/
	public float zd = 0.0F;

	/**
	 * Creates a new sphere of uniform(ish)ly spaced vectors/rays. 
	 * @param size
	 * @param raynum
	 */
	public Urchin(int size, int raynum)
	{
		this.rays = new AORay[raynum];

		float inc = (float) (Math.PI * (3 - Math.sqrt(5)));
		float off = 2.0F / raynum;
		for (int i = 0; i < this.rays.length; i++)
		{
			//Picks points on a sphere using the golden spiral
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
