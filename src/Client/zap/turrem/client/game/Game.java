package zap.turrem.client.game;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import units.turrem.Eekysam;
import units.turrem.Hut;
import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.game.entity.EntitySelectable;
import zap.turrem.client.game.operation.OperationMove;
import zap.turrem.client.game.player.PlayerClient;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.game.select.SelectionEventAdd;
import zap.turrem.client.game.select.SelectionEventReplace;
import zap.turrem.client.game.world.WorldClient;
import zap.turrem.client.gui.GuiFrame;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.font.FontRender;
import zap.turrem.client.render.texture.TextureIcon;
import zap.turrem.core.tech.branch.BranchList;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;
import zap.turrem.utils.Toolbox;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class Game
{
	public TextureIcon[] techicons;

	private PlayerFace face;

	public WorldClient theWorld;
	public RenderGame theRender;

	protected Turrem theTurrem;

	protected Ray pickRay;

	private Random rand;

	private float fpsstore = 0.0F;
	private long lastTickTime;

	GuiFrame testframe = new GuiFrame(160, 600, true, true, 0, "turrem.gui.frames.plain", 2.0F);

	public RealmClient myRealm;

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
		this.testframe.onStart(this.theTurrem.theRender);
		this.theRender.start();
		this.face.reset();

		EntityClient me = (new EntityClient(new Hut()));
		me.setPosition(-5.0F, 0.0F, 5.0F);
		me.push(this.theWorld, this.theTurrem.theRender);

		this.myRealm = new RealmClient(new PlayerClient());
		this.theWorld.realms.add(this.myRealm);
		this.myRealm.onStart();

		this.techicons = new TextureIcon[TechList.getSize()];

		TextureIcon nulltech = new TextureIcon("core.gui.nullTech");
		nulltech.load(this.theTurrem.theRender);

		for (int i = 0; i < this.techicons.length; i++)
		{
			TechItem ti = TechList.get(i);
			String[] tech = ti.getIdentifier().split("\\.");
			String mod = tech[1];
			String tstr = tech[0];
			tech[0] = mod;
			tech[1] = tstr;
			tech[tech.length - 1] = tech[tech.length - 1].replace('#', '_');
			String texture = "";
			for (int j = 0; j < tech.length; j++)
			{
				String strp = tech[j];
				if (j != 0)
				{
					texture += ".";
				}
				texture += strp;
			}
			System.out.println(texture);
			if (this.theTurrem.theAssets.doesTextureFileExist(texture))
			{
				this.techicons[i] = new TextureIcon(texture);
				try
				{
					this.techicons[i].load(this.theTurrem.theRender);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				this.techicons[i] = nulltech;
			}
		}
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
				if (Mouse.getEventX() > 160)
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
				else
				{
					int tech = ((Config.getHeight() - Mouse.getEventY()) - 29) / 58;
					if (tech < this.myRealm.branches.size() && tech >= 0)
					{
						this.myRealm.gainTech(TechList.get(BranchList.get(this.myRealm.branches.get(tech)).getTechs()[0]), false);
					}
				}
			}
			if (Mouse.getEventButton() == 1)
			{
				EntityClient picked = this.theWorld.getEntityPicked();
				if (picked != null && picked instanceof EntitySelectable)
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

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glPushMatrix();
		this.testframe.render();
		GL11.glPopMatrix();

		int ticox = 64;
		int ticoy = 30;
		
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		font.renderTextCentered("Available Techs:", ticox + 16, 8, 20);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		
		for (int bid : this.myRealm.branches)
		{
			TechItem tech = TechList.get(BranchList.get(bid).getTechs()[0]);

			TextureIcon techico = this.techicons[tech.getId()];

			GL11.glPushMatrix();
			techico.start();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex2f(ticox + 0, ticoy + 0);
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex2f(ticox + 0, ticoy + 32);
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex2f(ticox + 32, ticoy + 32);
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex2f(ticox + 32, ticoy + 0);

			GL11.glEnd();
			techico.end();
			GL11.glPopMatrix();

			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			font.renderTextCentered(tech.getName(), ticox + 16, ticoy + 36, 20);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);

			ticoy += 58;
			
			if (ticoy > 600 - 24 - 58)
			{
				GL11.glColor3f(0.0F, 0.0F, 0.0F);
				font.renderTextCentered("...More...", ticox + 16, 600 - 24, 20);
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
				break;
			}
		}

		if (Config.debugInfo)
		{
			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			font.renderText("\'S\' - Create new entity at cursor\n\'L-Click\' - Select entity\n\'Ctrl + L-Click\' - Add selection\n\'R-Click\' - Move selected entities\n\'R-Click Entity\' - Rotate entity\n\'L-Click & Drag\' - Pan camera\n\'M-Click & Drag\' - Orbit camera\n\'Scroll\' - Zoom\n\'F3\' - Toggle this info", 170.0F, 10.0F, 20.0F);
			String fps = Toolbox.getFloat(this.fpsstore, 1);
			font.renderText("FPS: " + fps, Config.getWidth() - 100.0F, 10.0F, 20.0F);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
		}
	}
}
