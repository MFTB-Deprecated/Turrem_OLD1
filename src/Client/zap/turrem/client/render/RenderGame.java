package zap.turrem.client.render;

import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.glu.GLU;

import org.lwjgl.BufferUtils;

import zap.turrem.client.config.Config;
import zap.turrem.client.game.Game;
import zap.turrem.client.game.player.face.PlayerFace;

public class RenderGame
{
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModelAmbient;
	
	public Game theGame;
	
	public RenderGame(Game game)
	{
		this.theGame = game;
	}
	
	public void start()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		PlayerFace.aspect = (float) Config.getWidth() / (float) Config.getHeight();
		GLU.gluPerspective(PlayerFace.fovy, PlayerFace.aspect, PlayerFace.znear, 64.0F);
		glMatrixMode(GL_MODELVIEW);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);

		this.initLightArrays();
		glShadeModel(GL_FLAT);

		glLight(GL_LIGHT0, GL_POSITION, this.lightPosition); 
		glLight(GL_LIGHT0, GL_SPECULAR, this.whiteLight); 
		glLight(GL_LIGHT0, GL_DIFFUSE, this.whiteLight); 
		glLightModel(GL_LIGHT_MODEL_AMBIENT, this.lModelAmbient);								

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);

		glEnable(GL_COLOR_MATERIAL);	
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
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
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		
		this.theGame.renderWorld();
		
		GL11.glPopMatrix();
	}
}
