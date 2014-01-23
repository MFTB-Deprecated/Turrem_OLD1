package zap.turrem.client.render;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.face.PlayerFace;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;

public class RenderWorld
{	
	protected RenderGame theRenderGame;
	
	public ModelIcon[] models;
	
	public RenderWorld(RenderManager man)
	{
		this.models = new ModelIcon[] {new ModelIcon("turrem.entity.human.eekysam"), new ModelIcon("turrem.entity.vehicle.wooden_cart"), new ModelIcon("turrem.structure.science.collider.atlas")};
		
		for (ModelIcon ico : this.models)
		{
			man.pushIcon(ico, "testrenders", RenderObjectHolderSimple.class);
		}
		
		this.models[0].loadMe();
	}
	
	public void render()
	{
		this.tickCamera();
		
		PlayerFace f = this.theRenderGame.theGame.face;
		GL11.glTranslated(0.0F, 0.0F, -f.getCamDist());
		GL11.glRotatef(f.getCamPitch(), -1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f.getCamYaw(), 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(f.getCamX(), f.getCamY(), f.getCamZ());
		
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
		this.theRenderGame.theGame.face.tickCamera();
	}
}
