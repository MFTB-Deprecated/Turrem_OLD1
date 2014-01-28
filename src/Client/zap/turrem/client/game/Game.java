package zap.turrem.client.game;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import units.turrem.Eekysam;
import zap.turrem.client.Turrem;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.game.entity.EntitySelectable;
import zap.turrem.client.game.operation.OperationMove;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.game.select.SelectionEventAdd;
import zap.turrem.client.game.select.SelectionEventReplace;
import zap.turrem.client.render.RenderGame;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class Game
{
	private PlayerFace face;

	public WorldClient theWorld;
	public RenderGame theRender;

	protected Turrem theTurrem;

	protected Ray pickRay;

	private Random rand;

	public Game(Turrem turrem)
	{
		this.face = new PlayerFace();
		this.theTurrem = turrem;
		this.theWorld = new WorldClient(this);
		this.theRender = new RenderGame(this);
		this.rand = new Random();
	}

	public void onStart()
	{
		this.theRender.start();
		this.face.reset();
	}

	public void updateGL()
	{

	}

	public void tickGame()
	{
		this.face.tickCamera();
		this.pickRay = this.face.pickMouse().setLength(48.0F);
		this.theWorld.tickWorld();
	}

	public void render()
	{
		this.theRender.render();
	}

	public void renderWorld()
	{
		this.face.doGLULook();
		this.theWorld.render();
	}

	public PlayerFace getFace()
	{
		return this.face;
	}

	public Ray getPickRay()
	{
		return this.pickRay;
	}

	public void mouseEvent()
	{
		if (!Mouse.getEventButtonState())
		{
			if (Mouse.getEventButton() == 0)
			{
				EntityClient picked = this.theWorld.getEntityPicked();
				if (picked != null)
				{
					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
					{
						(new SelectionEventAdd(picked.uid)).push(this.theWorld);
					}
					else
					{
						(new SelectionEventReplace(picked.uid)).push(this.theWorld);
					}
				}
			}
			if (Mouse.getEventButton() == 1)
			{
				EntityClient picked = this.theWorld.getEntityPicked();
				if (picked != null)
				{
					picked.rotation++;
					picked.rotation %= 4;
				}
				else
				{
					Ray r = this.getPickRay().duplicate();
					if (r.end.yCoord < r.start.yCoord)
					{
						Point p = Point.getSlideWithYValue(r.start, r.end, 0.0F);
						(new OperationMove(p)).push(this.theWorld);
					}
				}
			}
		}
	}

	public void keyEvent()
	{
		if (Keyboard.getEventKeyState())
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_S)
			{
				Ray r = this.getPickRay().duplicate();
				if (r.end.yCoord < r.start.yCoord)
				{
					Point p = Point.getSlideWithYValue(r.start, r.end, 0.0F);
					EntityClient me = (new EntitySelectable(new Eekysam()));
					me.rotation = (short) this.rand.nextInt(4);
					me.setPosition(p);
					me.push(this.theWorld, this.theTurrem.theRender);
				}
			}
		}
	}
}
