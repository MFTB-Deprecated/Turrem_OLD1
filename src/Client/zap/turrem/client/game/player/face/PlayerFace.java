package zap.turrem.client.game.player.face;

import org.lwjgl.input.Mouse;

import zap.turrem.client.config.Config;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;
import zap.turrem.utils.geo.Vector;

public class PlayerFace
{
	protected float camPitch = 20.0F;
	protected float camYaw = 45.0F;
	protected float camDist = 5.0F;

	protected Point camFocus;
	protected Point camLoc;

	private int mouselastx;
	private int mouselasty;

	public static float reachDistance = 5.0F;
	public static float fovy = 60.0F;
	public static float znear = 0.1F;
	public static float aspect;

	public PlayerFace()
	{
		this.camFocus = Point.getPoint(0.0D, 0.0D, 0.0D);
		this.camLoc = Point.getPoint(0.0D, 0.0D, 0.0D);
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

	public Ray getRay(float length)
	{
		Ray ray = Ray.getRay(this.getLocation(), Point.addVector(this.getLocation(), this.pickMouse()));
		ray.setLength(length);
		return ray;
	}

	public Vector pickMouse()
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
		
		float clipx = h.xpart * mousex + v.xpart * mousey;
		float clipy = h.ypart * mousex + v.ypart * mousey;
		float clipz = h.zpart * mousex + v.zpart * mousey;
		
		Point clip = Point.getPoint(clipx, clipy, clipz);
		
		Vector pick = Vector.getVector(this.camLoc, clip);
		pick.normalize();
		return pick;
	}

	public void tickCamera()
	{
		int wm = Mouse.getDWheel();
		if (Mouse.isButtonDown(2))
		{
			this.camYaw -= (Mouse.getX() - this.mouselastx) * Config.getMouseSpeed();
			this.camPitch -= (Mouse.getY() - this.mouselasty) * Config.getMouseSpeed();
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
			this.camDist -= wm * 0.01F;
			if (this.camDist < 2.0F)
			{
				this.camDist = 2.0F;
			}
			if (this.camDist > 20.0F)
			{
				this.camDist = 20.0F;
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
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
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
