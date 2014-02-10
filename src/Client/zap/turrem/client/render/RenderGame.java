package zap.turrem.client.render;

import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import org.lwjgl.util.glu.GLU;

import org.lwjgl.BufferUtils;

import zap.turrem.client.config.Config;
import zap.turrem.client.game.Game;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.font.Font;
import zap.turrem.client.render.font.FontRender;

public class RenderGame
{
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModelAmbient;

	public Game theGame;

	public FontRender testFont;

	public RenderManager theManager;

	public RenderGame(Game game, RenderManager manager)
	{
		this.theGame = game;
		this.theManager = manager;
	}

	public void start()
	{
		Font font = new Font("basic");
		font.loadTexture("core.fonts.basic", this.theManager);
		font.push();
		this.testFont = new FontRender(font);
	}

	public void doPerspective(PlayerFace face)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(face.getFOVY(), face.getAspect(), face.getZNear(), 64.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		this.doLighting();
	}

	public void doOrtho()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	public void doLighting()
	{
		this.initLightArrays();
		GL11.glShadeModel(GL11.GL_FLAT);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, this.whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.whiteLight);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.lModelAmbient);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
	}

	public void end()
	{

	}

	private void initLightArrays()
	{
		this.lightPosition = BufferUtils.createFloatBuffer(4);
		this.lightPosition.put(5.0f).put(5.0f).put(5.0f).put(0.0f).flip();

		this.whiteLight = BufferUtils.createFloatBuffer(4);
		this.whiteLight.put(2.2f).put(2.2f).put(2.2f).put(1.0f).flip();

		this.lModelAmbient = BufferUtils.createFloatBuffer(4);
		this.lModelAmbient.put(1.2f).put(1.2f).put(1.2f).put(1.0f).flip();
	}

	public void render()
	{
		GL11.glClearColor(0.9F, 0.9F, 0.9F, 1.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();

		this.doPerspective(this.theGame.getFace());
		this.theGame.renderWorld();

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		this.doOrtho();
		this.theGame.renderIngameGui();

		GL11.glPopMatrix();
	}
}
