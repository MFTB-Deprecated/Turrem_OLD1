package zap.turrem.client.render;

import org.lwjgl.opengl.GL11;

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
		GL11.glTranslated(0.0F, 0.0F, -f.getCamDist());
		GL11.glRotatef(f.getCamPitch(), -1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f.getCamYaw(), 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(f.getCamX(), f.getCamY(), f.getCamZ());

		this.doRender();

		Point targ = f.getLookEnd(2.0F);
		this.renderTarget(-targ.xCoord, -targ.yCoord, -targ.zCoord);
	}

	public void renderTarget(double x, double y, double z)
	{
		GL11.glColor3f(1.0f, 0.0f, 0.0f);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x + 0.1F, y, z);
		GL11.glVertex3d(x + 0.1F, y + 0.1F, z);
		GL11.glVertex3d(x, y + 0.1F, z);
		GL11.glEnd();
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
