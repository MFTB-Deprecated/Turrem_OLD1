package zap.turrem.client.render;

import org.lwjgl.opengl.GL11;

import org.lwjgl.input.Mouse;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.control.ControlList;
import zap.turrem.client.control.IBoolControl;
import zap.turrem.client.control.IDeltaValueControl;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;

public class RenderWorld
{
	protected float angley = 45.0F;
	protected float anglex = -20.0F;
	protected float camx = 0.0F;
	protected float camz = 0.0F;
	protected float dist = 5.0F;
	
	private int mouselastx;
	private int mouselasty;
	
	protected RenderGame theRenderGame;
	
	public ModelIcon[] models;
	
	private int leftclick;
	private int midclick;
	private int wheel;
	
	public RenderWorld(RenderManager man)
	{
		this.models = new ModelIcon[] {new ModelIcon("turrem.entity.human.eekysam"), new ModelIcon("turrem.entity.vehicle.wooden_cart"), new ModelIcon("turrem.structure.science.collider.atlas")};
		
		for (ModelIcon ico : this.models)
		{
			man.pushIcon(ico, "testrenders", RenderObjectHolderSimple.class);
		}
		
		this.models[0].loadMe();
		
		Turrem t = Turrem.getTurrem();
		this.leftclick = t.theControlList.getControlIndex("CLICK_BUTTON0");
		this.midclick = t.theControlList.getControlIndex("CLICK_BUTTON2");
		this.wheel = t.theControlList.getControlIndex("WHEEL");
	}
	
	public void render()
	{
		this.tickCamera();
		
		GL11.glTranslated(0.0F, 0.0F, -this.dist);
		GL11.glRotatef(this.anglex, -1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.angley, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(this.camx, -1.0F, this.camz);
		
		this.doRender();
	}
	
	public void doRender()
	{
		for (ModelIcon ico : this.models)
		{
			ico.render();
		}
	}
	
	protected void setRenderGame(RenderGame game)
	{
		this.theRenderGame = game;
	}
	
	public void tickCamera()
	{
		int wm = ((IDeltaValueControl)ControlList.instance().getControl(wheel)).getDelta();
		if (((IBoolControl)ControlList.instance().getControl(midclick)).getBool())
		{
			this.angley += (Mouse.getX() - this.mouselastx) * Config.getMouseSpeed();
			this.anglex += (Mouse.getY() - this.mouselasty) * Config.getMouseSpeed();
			if (this.anglex > -10.0F)
			{
				this.anglex = -10.0F;
			}
			if (this.anglex < -90.0F)
			{
				this.anglex = -90.0F;
			}
			this.angley %= 360.0F;
		}
		else if (wm != 0)
		{
			this.dist -= wm * 0.01F;
			if (this.dist < 2.0F)
			{
				this.dist = 2.0F;
			}
			if (this.dist > 20.0F)
			{
				this.dist = 20.0F;
			}
		}
		if (((IBoolControl)ControlList.instance().getControl(leftclick)).getBool())
		{
			float dx = (Mouse.getX() - this.mouselastx) * 0.002F * this.dist;
			float dy = (Mouse.getY() - this.mouselasty) * 0.002F * this.dist;
			float angrad = this.angley / 180.0F * 3.14F;
			float cos = (float) Math.cos(angrad);
			float sin = (float) Math.sin(angrad);
			this.camx += dx * cos;
			this.camx += dy * sin;
			this.camz -= dy * cos;
			this.camz += dx * sin;
		}
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
	}
}
