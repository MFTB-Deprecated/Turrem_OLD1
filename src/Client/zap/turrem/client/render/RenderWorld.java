package zap.turrem.client.render;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.glu.GLU;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import units.turrem.Eekysam;
import zap.turrem.client.Turrem;
import zap.turrem.client.game.Game;
import zap.turrem.client.game.WorldClient;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.utils.geo.Point;

public class RenderWorld
{
	protected Game theGame;

	public ModelIcon cursor;

	public boolean canplace = true;

	public RenderWorld(RenderManager man, Game game)
	{
		this.theGame = game;

		this.cursor = new ModelIcon("turrem.interface.3dcursor");
		man.pushIcon(this.cursor, "interface", RenderObjectHolderSimple.class);
		this.cursor.loadMe();
	}

	public void render()
	{
		PlayerFace f = this.getFace();

		Point foc = f.getFocus();
		Point loc = f.getLocation();

		GLU.gluLookAt((float) loc.xCoord, (float) loc.yCoord, (float) loc.zCoord, (float) foc.xCoord, (float) foc.yCoord, (float) foc.zCoord, 0.0F, 1.0F, 0.0F);

		this.doRender();

		Point targ = f.getPickRay().end;
		this.renderTarget(targ.xCoord, targ.yCoord, targ.zCoord);
	}

	public void renderTarget(double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		float off = 0.5F / 4.0F;
		GL11.glTranslated(-off, -off, -off);
		this.cursor.render();
		GL11.glPopMatrix();
	}

	public void doRender()
	{
		for (EntityClient ent : this.theGame.theWorld.entityList)
		{
			ent.render();
		}
		EntityClient picked = this.theGame.theWorld.getEntityPicked();
		if (picked != null)
		{
			picked.drawBox(0.0F, 0.0F, 1.0F);
			if (Mouse.isButtonDown(0))
			{
				picked.setSelected(true);
			}
		}
		else
		{
			if (Mouse.isButtonDown(1))
			{
				Point g = this.getFace().getPickGround();
				if (g != null)
				{
					g.yCoord = 0.0D;
				}
				this.theGame.theWorld.moveTo = g;
			}
			if (Mouse.isButtonDown(0))
			{
				EntityClient.deselect = true;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if (this.canplace)
			{
				Point g = this.getFace().getPickGround();
				if (g != null)
				{
					g.yCoord = 0.0D;
					EntityClient ent = new EntityClient(new Eekysam());
					ent.push(this.theGame.theWorld, Turrem.getTurrem().theRender);
					ent.setPosition(g.xCoord, g.yCoord, g.zCoord);
				}

			}
			this.canplace = false;
		}
		else
		{
			this.canplace = true;
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
