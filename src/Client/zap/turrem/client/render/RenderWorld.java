package zap.turrem.client.render;

import org.lwjgl.util.glu.GLU;

import zap.turrem.client.game.Game;
import zap.turrem.client.game.WorldClient;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.utils.geo.Point;

public class RenderWorld
{
	protected Game theGame;

	public ModelIcon[] models;

	public RenderWorld(RenderManager man, Game game)
	{
		this.theGame = game;
		this.models = new ModelIcon[] { new ModelIcon("turrem.entity.human.eekysam"), new ModelIcon("turrem.entity.vehicle.wooden_cart"), new ModelIcon("turrem.structure.science.collider.atlas") };

		for (ModelIcon ico : this.models)
		{
			man.pushIcon(ico, "testrenders", RenderObjectHolderSimple.class);
		}

		this.models[0].loadMe();
	}

	public void render()
	{
		PlayerFace f = this.getFace();
		
		Point foc = f.getFocus();
		Point loc = f.getLocation();
		
		GLU.gluLookAt((float) loc.xCoord, (float) loc.yCoord,(float)  loc.zCoord, (float) foc.xCoord, (float) foc.yCoord, (float) foc.zCoord, 0.0F, 1.0F, 0.0F);

		this.doRender();
	}
	
	public void doRender()
	{
		for (ModelIcon ico : this.models)
		{
			ico.render();
		}
	}

	protected PlayerFace getFace()
	{
		return this.theGame.face;
	}

	protected WorldClient getWorld()
	{
		return this.theGame.theWorld;
	}
}
