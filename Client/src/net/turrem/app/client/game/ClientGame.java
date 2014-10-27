package net.turrem.app.client.game;

import java.io.IOException;

import java.nio.FloatBuffer;

import net.turrem.app.client.Config;
import net.turrem.app.client.Turrem;
import net.turrem.app.client.game.world.ClientWorld;
import net.turrem.app.client.render.RenderEngine;
import net.turrem.app.client.render.font.Font;
import net.turrem.app.client.render.font.FontRender;
import net.turrem.app.client.render.icon.TextureIcon;
import net.turrem.blueutils.geo.EnumDir;
import net.turrem.blueutils.geo.Point;
import net.turrem.blueutils.geo.Ray;
import net.turrem.blueutils.geo.VoxelGeoUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ClientGame
{
	public ClientWorld theWorld;
	protected Turrem theTurrem;
	
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModelAmbient;
	
	public RenderEngine theRender;
	
	private PlayerFace face;
	
	public long mine = -1;
	protected FontRender debugFont;
	
	protected TextureIcon terrselect;
	protected TextureIcon toppin;
	
	public static final int[][] vertOffs = VoxelGeoUtils.vertexOffset;
	public static final int[][] vertOffinds = VoxelGeoUtils.vertexOffsetIndices;
	
	public ClientGame(RenderEngine engine, Turrem turrem)
	{
		this.theTurrem = turrem;
		this.theRender = engine;
		this.theWorld = new ClientWorld(this);
		this.face = new PlayerFace(this.theWorld);
		
		try
		{
			this.theWorld.startNetwork(turrem.theSession.username);
		}
		catch (IOException e)
		{
			System.out.println(e);
			this.theWorld.end();
		}
	}
	
	public void render()
	{
		this.face.tickCamera(this.theWorld);
		
		boolean useFrame = this.theRender.useRenderFrame();
		
		if (useFrame)
		{
			this.theRender.startFrame();
		}
		
		GL11.glClearColor(0.18F, 0.18F, 0.23F, 1.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glPushMatrix();
		this.doPerspective(this.getFace());
		this.renderWorld();
		GL11.glPopMatrix();
		
		if (useFrame)
		{
			this.theRender.endFrame();
			
			GL11.glPushMatrix();
			this.theRender.renderFrame();
			GL11.glPopMatrix();
		}
		
		GL11.glPushMatrix();
		this.doOrtho();
		this.renderInterface();
		GL11.glPopMatrix();
	}
	
	public void renderInterface()
	{
		if (this.face.getTerrainPickSide() != null)
		{
			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			this.debugFont.renderText("@(" + this.face.getTerrainPickX() + ", " + this.face.getTerrainPickY() + ", " + this.face.getTerrainPickZ() + ", " + this.face.getTerrainPickSide().name() + ")", 16, 16, 16);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
		}
	}
	
	public void renderWorld()
	{
		this.face.doGLULook();
		this.doLighting();
		this.theTurrem.staticEventRegistry.onPreWorldRender(this.theWorld);
		this.theWorld.render();
		this.theTurrem.staticEventRegistry.onPostWorldRender(this.theWorld);
		this.renderSelect();
	}
	
	public void renderSelect()
	{
		EnumDir side = this.face.getTerrainPickSide();
		if (side != null)
		{
			float off = 1.0F / 16.0F;
			int[] inds = vertOffinds[side.ind];
			int posx = this.face.getTerrainPickX();
			int posy = this.face.getTerrainPickY();
			int posz = this.face.getTerrainPickZ();
			this.terrselect.start();
			GL11.glBegin(GL11.GL_QUADS);
			float addx = side.xoff * off;
			float addy = side.yoff * off;
			float addz = side.zoff * off;
			for (int i = 0; i < 4; i++)
			{
				this.renderSelectVert(posx, posy, posz, vertOffs[inds[i]], i, addx, addy, addz);
			}
			GL11.glEnd();
			this.terrselect.end();
			this.toppin.start();
			GL11.glBegin(GL11.GL_QUADS);
			inds = vertOffinds[EnumDir.YUp.ind];
			posy = this.theWorld.getHeight(posx, posz) - 1;
			for (int i = 0; i < 4; i++)
			{
				this.renderSelectVert(posx, posy, posz, vertOffs[inds[i]], i, 0.0F, off, 0.0F);
			}
			GL11.glEnd();
			this.toppin.end();
		}
	}
	
	private void renderSelectVert(int posx, int posy, int posz, int vert[], int i, float addx, float addy, float addz)
	{
		GL11.glTexCoord2f(((i + 1) / 2) % 2, (i / 2) % 2);
		GL11.glVertex3f(posx + vert[0] + addx, posy + vert[1] + addy, posz + vert[2] + addz);
	}
	
	private void initLightArrays()
	{
		this.lightPosition = BufferUtils.createFloatBuffer(4);
		this.lightPosition.put(5.0f).put(10.0f).put(8.0f).put(0.0f).flip();
		
		this.whiteLight = BufferUtils.createFloatBuffer(4);
		this.whiteLight.put(0.5f).put(0.5f).put(0.5f).put(0.75f).flip();
		
		this.lModelAmbient = BufferUtils.createFloatBuffer(4);
		this.lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.75f).flip();
	}
	
	public void doPerspective(PlayerFace face)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(face.getFOVY(), face.getAspect(), face.getZNear(), 256.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void doOrtho()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}
	
	public void doLighting()
	{
		this.initLightArrays();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.whiteLight);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.lModelAmbient);
		
		GL11.glEnable(GL11.GL_LIGHT0);
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 0.0F);
	}
	
	public void end()
	{
		
	}
	
	public void start()
	{
		Font font = new Font();
		font.loadTexture("app:font.screen", this.theTurrem.theRender, false);
		this.debugFont = new FontRender(font);
		this.terrselect = new TextureIcon("app:misc.terrselect", true);
		this.terrselect.load(this.theTurrem.theRender);
		this.toppin = new TextureIcon("app:misc.toppin", true);
		this.toppin.load(this.theTurrem.theRender);
	}
	
	public void mouseEvent()
	{
		if (Mouse.getEventButtonState())
		{
			if (Mouse.getEventButton() == 1)
			{
				Ray pick = this.face.pickMouse();
				Point location = Point.getSlideWithYValue(pick.start, pick.end, 96.0D);
				System.out.println("R-Click @ " + location);
			}
		}
	}
	
	public void keyEvent()
	{
		if (Keyboard.getEventKeyState())
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_M)
			{
				
			}
		}
	}
	
	public PlayerFace getFace()
	{
		return this.face;
	}
	
	public void stopGame()
	{
		this.theTurrem.shutdown();
	}
}
