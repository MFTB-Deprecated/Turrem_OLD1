package net.turrem.client.game;

import org.lwjgl.input.Mouse;

import org.lwjgl.util.glu.GLU;

import net.turrem.client.Config;
import net.turrem.client.game.world.Chunk;
import net.turrem.client.game.world.ClientWorld;
import net.turrem.utils.geo.EnumDir;
import net.turrem.utils.geo.Point;
import net.turrem.utils.geo.Ray;
import net.turrem.utils.geo.Vector;

public class PlayerFace
{
	public final static float hdamp = 0.9F;

	private final int worldSize;

	protected float camPitch = 30.0F;
	protected float camYaw = 45.0F;
	protected float camDist = 16.0F;

	protected Point camFocus;
	protected Point camLoc;

	private int mouselastx;
	private int mouselasty;
	private float aspect;
	private float znear = 1.0F;
	private float fovy = 60.0F;

	private int requestChunkSelector = 0;

	private long lastChunkRequestTime = 0;
	private int lastChunkRequestX;
	private int lastChunkRequestZ;

	private int pickx;
	private int picky;
	private int pickz;
	private EnumDir pickSide;

	public PlayerFace(ClientWorld world)
	{
		this.reset();
		this.worldSize = world.worldSize;
		this.camFocus = Point.getPoint(0.0D, 128.0D, 0.0D);
		this.camLoc = Point.getPoint(0.0D, 0.0D, 0.0D);
		this.lastChunkRequestX = (int) this.camLoc.xCoord;
		this.lastChunkRequestZ = (int) this.camLoc.zCoord;
		this.doFocus();
		this.updatePars();
	}

	public void updatePars()
	{
		float width = Config.getWidth();
		float height = Config.getHeight();
		this.aspect = width / height;
	}

	public float getFOVY()
	{
		return this.fovy;
	}

	public float getZNear()
	{
		return this.znear;
	}

	public float getAspect()
	{
		return this.aspect;
	}

	public void doGLULook()
	{
		GLU.gluLookAt((float) this.camLoc.xCoord, (float) this.camLoc.yCoord, (float) this.camLoc.zCoord, (float) this.camFocus.xCoord, (float) this.camFocus.yCoord, (float) this.camFocus.zCoord, 0.0F, 1.0F, 0.0F);
	}

	public void reset()
	{
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
	}

	public Point getFocus()
	{
		return this.camFocus;
	}

	public Point getLocation()
	{
		return this.camLoc;
	}

	public Ray pickMouse()
	{
		float mousex = Mouse.getX();
		float mousey = Mouse.getY();

		Vector view = Vector.getVector(this.camLoc, this.camFocus);
		view.normalize();

		Vector h = Vector.cross(view, Vector.getVector(0.0F, 1.0F, 0.0F));
		h.normalize();

		Vector v = Vector.cross(h, view);
		v.normalize();

		float rad = fovy * 3.1415F / 180.0F;
		float vLength = (float) (Math.tan(rad / 2) * znear);
		float hLength = vLength * aspect;

		v.scale(vLength);
		h.scale(hLength);

		int width = Config.getWidth();
		int height = Config.getHeight();

		mousex -= (width / 2.0F);
		mousey -= (height / 2.0F);

		mousex /= (width / 2.0F);
		mousey /= (height / 2.0F);

		double clipx = this.camLoc.xCoord + view.xpart * znear + h.xpart * mousex + v.xpart * mousey;
		double clipy = this.camLoc.yCoord + view.ypart * znear + h.ypart * mousex + v.ypart * mousey;
		double clipz = this.camLoc.zCoord + view.zpart * znear + h.zpart * mousex + v.zpart * mousey;

		Point clip = Point.getPoint(clipx, clipy, clipz);
		return Ray.getRay(this.camLoc, clip);
	}

	public int getTerrainPickX()
	{
		return this.pickx;
	}

	public int getTerrainPickY()
	{
		return this.picky;
	}

	public int getTerrainPickZ()
	{
		return this.pickz;
	}

	public EnumDir getTerrainPickSide()
	{
		return this.pickSide;
	}

	protected void rayPickTerrain(Point origin, Vector direction, float radius, ClientWorld world)
	{
		// Cube containing origin point.
		int x = (int) origin.xCoord;
		int y = (int) origin.yCoord;
		int z = (int) origin.zCoord;
		// Break out direction vector.
		float dx = direction.xpart;
		float dy = direction.ypart;
		float dz = direction.zpart;
		// Direction to increment x,y,z when stepping.
		int stepX = signum(dx);
		int stepY = signum(dy);
		int stepZ = signum(dz);
		// See description above. The initial values depend on the fractional
		// part of the origin.
		float tMaxX = intbound((float) origin.xCoord, dx);
		float tMaxY = intbound((float) origin.yCoord, dy);
		float tMaxZ = intbound((float) origin.zCoord, dz);
		// The change in t when taking a step (always positive).
		float tDeltaX = stepX / dx;
		float tDeltaY = stepY / dy;
		float tDeltaZ = stepZ / dz;

		EnumDir face = null;

		if (dx == 0 && dy == 0 && dz == 0)
		{
			return;
		}
		
		if (dy > 0)
		{
			this.pickx = -1;
			this.picky = -1;
			this.pickz = -1;
			this.pickSide = null;
			return;
		}

		radius /= direction.length();

		while ((stepX > 0 ? x < this.worldSize : x >= 0) && (stepY > 0 ? y < this.worldSize : y >= 0) && (stepZ > 0 ? z < this.worldSize : z >= 0))
		{
			if (!(x < 0 || y < 0 || z < 0 || x >= this.worldSize || y >= this.worldSize || z >= this.worldSize))
			{
				if (testPickTerrain(x, y, z, face, world))
				{
					this.pickx = x;
					this.picky = y;
					this.pickz = z;
					this.pickSide = face;
					return;
				}
			}

			// tMaxX stores the t-value at which we cross a cube boundary along
			// the
			// X axis, and similarly for Y and Z. Therefore, choosing the least
			// tMax
			// chooses the closest cube boundary. Only the first case of the
			// four
			// has been commented in detail.
			if (tMaxX < tMaxZ)
			{
				if (tMaxX < tMaxY)
				{
					if (tMaxX > radius)
					{
						break;
					}
					// Update which cube we are now in.
					x += stepX;
					// Adjust tMaxX to the next X-oriented boundary crossing.
					tMaxX += tDeltaX;
					// Record the normal vector of the cube face we entered.
					if (stepX > 0)
					{
						face = EnumDir.XDown;
					}
					else
					{
						face = EnumDir.XUp;
					}
				}
				else
				{
					if (tMaxY > radius)
					{
						break;
					}
					y += stepY;
					tMaxY += tDeltaY;
					face = EnumDir.YUp;
				}
			}
			else
			{
				if (tMaxZ < tMaxY)
				{
					if (tMaxZ > radius)
					{
						break;
					}
					z += stepZ;
					tMaxZ += tDeltaZ;
					if (stepZ > 0)
					{
						face = EnumDir.ZDown;
					}
					else
					{
						face = EnumDir.ZUp;
					}
				}
				else
				{
					// Identical to the second case, repeated for simplicity in
					// the conditionals.
					if (tMaxY > radius)
					{
						break;
					}
					y += stepY;
					tMaxY += tDeltaY;
					face = EnumDir.YUp;
				}
			}
		}
		this.pickx = -1;
		this.picky = -1;
		this.pickz = -1;
		this.pickSide = null;
	}

	private float intbound(float s, float ds)
	{
		// Find the smallest positive t such that s+t*ds is an integer.
		if (ds < 0)
		{
			return intbound(-s, -ds);
		}
		else
		{
			s -= (int) s;
			if (s < 0)
			{
				s += 1;
			}
			// problem is now s+t*ds = 1
			return (1 - s) / ds;
		}
	}

	private int signum(float x)
	{
		return x > 0 ? 1 : x < 0 ? -1 : 0;
	}

	private boolean testPickTerrain(int x, int y, int z, EnumDir face, ClientWorld world)
	{
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		if (chunk == null)
		{
			return false;
		}
		return chunk.getHeight(x, z) > y;
	}

	public Ray pickCamera()
	{
		return Ray.getRay(this.camFocus, this.camLoc);
	}

	public void tickCamera(ClientWorld world)
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime - this.lastChunkRequestTime > Config.chunkRequestTimeLimit || this.lastChunkRequestTime == 0)
		{
			int dx = this.lastChunkRequestX - (int) this.camLoc.xCoord;
			int dz = this.lastChunkRequestZ - (int) this.camLoc.zCoord;
			if (dx * dx + dz * dz > 256)
			{
				this.lastChunkRequestX = (int) this.camLoc.xCoord;
				this.lastChunkRequestZ = (int) this.camLoc.zCoord;
				this.requestChunkSelector = 0;
			}
			this.lastChunkRequestTime = currentTime;
			this.requestChunkSelector = world.requestNullChunks((int) this.camLoc.xCoord, (int) this.camLoc.zCoord, this.requestChunkSelector, 8);
		}

		int wm = Mouse.getDWheel();
		if (Mouse.isButtonDown(2))
		{
			this.camYaw -= (Mouse.getX() - this.mouselastx) * Config.mouseSpeedX;
			this.camPitch -= (Mouse.getY() - this.mouselasty) * Config.mouseSpeedY;
			if (this.camPitch < 10.0F)
			{
				this.camPitch = 10.0F;
			}
			if (this.camPitch > 90.0F)
			{
				this.camPitch = 90.0F;
			}
			this.camYaw %= 360.0F;
		}
		else if (wm != 0)
		{
			this.camDist -= wm * Config.scrollSpeed;
			if (this.camDist < Config.camDistMin)
			{
				this.camDist = Config.camDistMin;
			}
			if (this.camDist > Config.camDistMax)
			{
				this.camDist = Config.camDistMax;
			}
		}
		if (Mouse.isButtonDown(0))
		{
			float dx = (Mouse.getX() - this.mouselastx) * 0.002F * this.camDist;
			float dy = (Mouse.getY() - this.mouselasty) * 0.002F * this.camDist;
			float angrad = this.camYaw / 180.0F * 3.14F;
			float cos = (float) Math.cos(angrad);
			float sin = (float) Math.sin(angrad);
			double dX = 0.0D;
			double dZ = 0.0D;
			dX -= dx * cos;
			dX += dy * sin;
			dZ += dy * cos;
			dZ += dx * sin;
			this.camFocus.moveDelta(dX, 0.0D, dZ);
		}
		this.doFocus();
		int iheight = this.getLocalHorizon(world);
		if (iheight != Integer.MIN_VALUE)
		{
			double height = iheight + 5.0F;
			this.camFocus.yCoord *= hdamp;
			this.camFocus.yCoord += height * (1.0F - hdamp);
			this.camLoc.yCoord *= hdamp;
			this.camLoc.yCoord += height * (1.0F - hdamp);
		}
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
		Ray mouse = this.pickMouse();
		this.rayPickTerrain(mouse.start, mouse.getVector().returnNormalized(), 128, world);
	}

	public int getLocalHorizon(ClientWorld world)
	{
		double x1 = this.camFocus.xCoord;
		double z1 = this.camFocus.zCoord;
		double x2 = this.camLoc.xCoord;
		double z2 = this.camLoc.zCoord;
		return world.getRayHeight(x1, z1, x2, z2, 5.0F);
	}

	public void reverseFocus()
	{
		double dx = this.camLoc.xCoord - this.camFocus.xCoord;
		double dy = this.camLoc.yCoord - this.camFocus.yCoord;
		double dz = this.camLoc.zCoord - this.camFocus.zCoord;
		double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
		this.camDist = (float) dist;
		dx /= this.camDist;
		dy /= this.camDist;
		dz /= this.camDist;
		float pitchrad = (float) Math.asin(dy);
		this.camPitch = (pitchrad / 3.14F) * 180.0F;
		double scalev = Math.cos(pitchrad);
		dx /= scalev;
		dz /= scalev;
		float yawrad = (float) Math.asin(dx);
		this.camYaw = (yawrad / 3.14F) * 180.0F;
	}

	public void setFocus(Point point)
	{
		this.camFocus = point;
		this.doFocus();
	}

	public void doFocus()
	{
		float yawrad = this.camYaw / 180.0F * 3.14F;
		float pitchrad = this.camPitch / 180.0F * 3.14F;

		double scalev = Math.cos(pitchrad);

		double dx = this.camDist * Math.sin(yawrad) * scalev;
		double dz = this.camDist * Math.cos(yawrad) * scalev;

		double dy = this.camDist * Math.sin(pitchrad);

		dx += this.camFocus.xCoord;
		dy += this.camFocus.yCoord;
		dz += this.camFocus.zCoord;

		this.camLoc.setPoint(dx, dy, dz);
	}

	public final float getCamPitch()
	{
		return camPitch;
	}

	public final void setCamPitch(float camPitch)
	{
		this.camPitch = camPitch;
	}

	public final float getCamYaw()
	{
		return camYaw;
	}

	public final void setCamYaw(float camYaw)
	{
		this.camYaw = camYaw;
	}

	public final float getCamDist()
	{
		return camDist;
	}

	public final void setCamDist(float camDist)
	{
		this.camDist = camDist;
	}
}
