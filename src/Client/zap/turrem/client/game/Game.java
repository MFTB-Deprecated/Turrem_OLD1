package zap.turrem.client.game;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import units.turrem.Eekysam;
import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.game.entity.EntitySelectable;
import zap.turrem.client.game.operation.OperationMove;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.game.select.SelectionEventAdd;
import zap.turrem.client.game.select.SelectionEventReplace;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.font.FontRender;
import zap.turrem.utils.Toolbox;
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

	private float fpsstore = 0.0F;
	private long lastTickTime;

	public Game(Turrem turrem)
	{
		this.face = new PlayerFace();
		this.theTurrem = turrem;
		this.theWorld = new WorldClient(this);
		this.theRender = new RenderGame(this);
		this.rand = new Random();
		this.lastTickTime = System.currentTimeMillis();
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

		long time = System.currentTimeMillis();
		int ms = (int) (time - this.lastTickTime);
		this.lastTickTime = time;
		float fs = 1000.0F / (float) ms;
		if (fs < 200.0F && fs > 0.0001F)
		{
			this.fpsstore *= 0.9F;
			this.fpsstore += fs * 0.1F;
		}
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
			if (Keyboard.getEventKey() == Keyboard.KEY_F3)
			{
				Config.debugInfo = !Config.debugInfo;
			}
		}
	}

	public void renderIngameGui()
	{
		FontRender font = this.theRender.testFont;
		if (Config.debugInfo)
		{
			font.renderText("\'S\' - Create new entity at cursor\n\'L-Click\' - Select entity\n\'Ctrl + L-Click\' - Add selection\n\'R-Click\' - Move selected entities\n\'R-Click Entity\' - Rotate entity\n\'L-Click & Drag\' - Pan camera\n\'M-Click & Drag\' - Orbit camera\n\'Scroll\' - Zoom\n\'F3\' - Toggle this info", 10.0F, 10.0F, 20.0F);
			String fps = Toolbox.getFloat(this.fpsstore, 1);
			font.renderText("FPS: " + fps, Config.getWidth() - 100.0F, 10.0F, 20.0F);
		}
	}
}
