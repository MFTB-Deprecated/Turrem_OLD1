package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import org.lwjgl.input.Keyboard;

import units.turrem.Eekysam;
import units.turrem.Hut;
import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.game.player.PlayerClient;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.game.world.WorldClient;
import zap.turrem.client.gui.GuiTechGrid;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.font.FontRender;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;
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

	public RealmClient myRealm;
	
	public GuiTechGrid techs;
	
	public boolean mat = true;

	public Game(Turrem turrem)
	{
		this.face = new PlayerFace();
		this.theTurrem = turrem;
		this.theWorld = new WorldClient(this);
		this.theRender = new RenderGame(this, this.theTurrem.theRender);
		this.rand = new Random();
		this.lastTickTime = System.currentTimeMillis();
	}

	public void onStart()
	{
		this.theRender.start();
		this.face.reset();

		Entity me = (new Entity(new Hut()));
		me.setPosition(-8.0F, 0.6F, -12.0F);
		me.push(this.theWorld, this.theTurrem.theRender);

		this.myRealm = new RealmClient(new PlayerClient());
		this.theWorld.realms.add(this.myRealm);
		this.myRealm.onStart();
		
		this.techs = new GuiTechGrid(this.theRender.testFont, 40, 8, 10, 10, 20, 20);
		this.techs.onStart(this.theRender.theManager);
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
		
		this.techs.grid = (ArrayList<TechItem>) TechList.getTechList();
	}

	public void render()
	{
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
					Entity me = (new Entity(new Eekysam()));
					me.rotation = (short) this.rand.nextInt(4);
					me.setPosition(p);
					me.push(this.theWorld, this.theTurrem.theRender);
				}
			}
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
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			font.renderText("\'S\' - Create new entity at cursor\n\'T\' - Toggle material\n\'L-Click\' - Select entity\n\'Ctrl + L-Click\' - Add selection\n\'R-Click\' - Move selected entities\n\'R-Click Entity\' - Rotate entity\n\'L-Click & Drag\' - Pan camera\n\'M-Click & Drag\' - Orbit camera\n\'Scroll\' - Zoom\n\'F3\' - Toggle this info", 170.0F, 10.0F, 20.0F);
			String fps = Toolbox.getFloat(this.fpsstore, 1);
			font.renderText("FPS: " + fps, Config.getWidth() - 100.0F, 10.0F, 20.0F);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
		}
		
		//this.techs.render();
	}

}
