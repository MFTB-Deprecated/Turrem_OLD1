package zap.turrem.client.game.player.face;

import org.lwjgl.input.Mouse;

import zap.turrem.client.config.Config;

public class PlayerFace
{
	protected float camPitch = -20.0F;
	protected float camYaw = 45.0F;
	protected float camX = 0.0F;
	protected float camY = -1.0F;
	protected float camZ = 0.0F;
	protected float camDist = 5.0F;
	
	private int mouselastx;
	private int mouselasty;
	
	public PlayerFace()
	{
		
	}
	
	public void reset()
	{
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
	}
	
	public void tickCamera()
	{
		int wm = Mouse.getDWheel();
		if (Mouse.isButtonDown(2))
		{
			this.camYaw += (Mouse.getX() - this.mouselastx) * Config.getMouseSpeed();
			this.camPitch += (Mouse.getY() - this.mouselasty) * Config.getMouseSpeed();
			if (this.camPitch > -10.0F)
			{
				this.camPitch = -10.0F;
			}
			if (this.camPitch < -90.0F)
			{
				this.camPitch = -90.0F;
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
			this.camX += dx * cos;
			this.camX += dy * sin;
			this.camZ -= dy * cos;
			this.camZ += dx * sin;
		}
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
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

	public final float getCamX()
	{
		return camX;
	}

	public final void setCamX(float camX)
	{
		this.camX = camX;
	}

	public final float getCamY()
	{
		return camY;
	}

	public final void setCamY(float camY)
	{
		this.camY = camY;
	}

	public final float getCamZ()
	{
		return camZ;
	}

	public final void setCamZ(float camZ)
	{
		this.camZ = camZ;
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
