package net.turrem.utils.ao;

/**
 * AORay is a series of points on a 3D integer grid that form a line or ray. By checking for open voxels at these points, the occlusion of this ray can be calculated.
 */
public class AORay
{
	/**
	 * Offset is a single point on a 3D integer grid. The distance of this point along the ray is also stored.
	 */
	public static class Offset
	{
		public float depth;
		public int x;
		public int y;
		public int z;
	}

	public Offset[] points;

	/**The magnitude of the ray in the positive X direction*/
	public float xu = 0.0F; 
	/**The magnitude of the ray in the negative X direction*/
	public float xd = 0.0F;
	/**The magnitude of the ray in the positive Y direction*/
	public float yu = 0.0F;
	/**The magnitude of the ray in the negative Y direction*/
	public float yd = 0.0F;
	/**The magnitude of the ray in the positive Z direction*/
	public float zu = 0.0F;
	/**The magnitude of the ray in the negative Z direction*/
	public float zd = 0.0F;

	/**
	 * Constructs a new ray
	 * @param x X component
	 * @param y Y component
	 * @param z Z component
	 * @param size The number of points on the ray
	 * @param inc The length along the ray between each point
	 */
	public AORay(float x, float y, float z, int size, float inc)
	{
		//Re-normalizes the vector
		float l = (float) Math.sqrt(x * x + y * y + z * z);
		x /= l;
		y /= l;
		z /= l;
		float dx = x * inc;
		float dy = y * inc;
		float dz = z * inc;

		//Splits the components into positive and negitive
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

			//Only adds a point if it is different from the last point
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
