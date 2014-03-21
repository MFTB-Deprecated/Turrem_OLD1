package zap.turrem.client.game;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import org.lwjgl.input.Keyboard;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.game.player.PlayerClient;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.game.world.WorldClient;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.font.FontRender;
import zap.turrem.utils.Toolbox;
import zap.turrem.utils.geo.Ray;

public class Game
{
	private PlayerFace face;

	public WorldClient theWorld;
	public RenderGame theRender;

	protected Turrem theTurrem;

	protected Ray pickRay;

	protected Random rand;

	private float fpsstore = 60.0F;
	private float tpsstore = 10.0F;
	private long lastTickTime;
	private long lastFrameTime;
	private int secticks = 0;
	private int secframes = 0;
	
	public RealmClient myRealm;

	public boolean mat = true;

	public Game(Turrem turrem)
	{
		this.face = new PlayerFace();
		this.theTurrem = turrem;
		this.theWorld = new WorldClient(this, turrem);
		this.theRender = new RenderGame(this, this.theTurrem.theRender);
		this.rand = new Random();
	}

	public void onStart()
	{
		this.theRender.start();
		this.face.reset();

		this.myRealm = new RealmClient(new PlayerClient());
		this.theWorld.realms.add(this.myRealm);
		this.myRealm.onStart();
		
		this.lastTickTime = System.currentTimeMillis();
		this.lastFrameTime = System.currentTimeMillis();
	}

	public void updateGL()
	{

	}

	public void tickGame()
	{
		this.face.tickCamera();
		this.pickRay = this.face.pickMouse().setLength(48.0F);
		this.theWorld.tickWorld();

		long dif = System.currentTimeMillis() - this.lastTickTime;
		if (dif > 1000)
		{
			this.tpsstore = this.secticks * (1000.0F / dif);
			this.lastTickTime += dif;
			this.secticks = 0;
		}
		this.secticks++;
	}

	public void render()
	{
		long dif = System.currentTimeMillis() - this.lastFrameTime;
		if (dif > 1000)
		{
			this.fpsstore = this.secframes * (1000.0F / dif);
			this.lastFrameTime += dif;
			this.secframes = 0;
		}
		this.secframes++;
		this.theRender.render();
	}

	public void renderWorld()
	{
		this.face.doGLULook();
		this.theRender.doLighting();
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
		this.theWorld.doMouseEvent();
	}

	public void keyEvent()
	{
		this.theWorld.doKeyEvent();
		if (Keyboard.getEventKeyState())
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_F3)
			{
				Config.debugInfo = !Config.debugInfo;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_T)
			{
				this.mat = !this.mat;
			}
		}
	}

	public void renderIngameGui()
	{
		FontRender font = this.theRender.testFont;

		if (Config.debugInfo)
		{
			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			font.renderText("\'T\' - Toggle material\n\'L-Click & Drag\' - Pan camera\n\'M-Click & Drag\' - Orbit camera\n\'Scroll\' - Zoom\n\'F3\' - Toggle this info", 20.0F, 10.0F, 20.0F);
			String fps = Toolbox.getFloat(this.fpsstore, 1);
			String tps = Toolbox.getFloat(this.tpsstore, 1);
			font.renderText("FPS: " + fps + "\nTPS: " + tps, Config.getWidth() - 100.0F, 10.0F, 20.0F);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
		}

		// this.techs.render();
	}

}
