package zap.turrem.client.states;

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
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.util.zip.GZIPInputStream;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.render.TVFBuffer;
import zap.turrem.utils.models.TVFFile;

public class StateGame implements IState
{
	private Turrem theTurrem;

	private TVFBuffer eekysam;
	private TVFBuffer cart;

	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModelAmbient;
	
	private float angle = 0.0F;

	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(70.0F, Config.getWidth() / Config.getHeight(), 1.0F, 20.0F);
		glMatrixMode(GL_MODELVIEW);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);

		initLightArrays();
		glShadeModel(GL_FLAT);

		glLight(GL_LIGHT0, GL_POSITION, lightPosition); // sets light position
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight); // sets specular light to
														// white
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight); // sets diffuse light to
													// white
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient); // global ambient
																// light

		glEnable(GL_LIGHTING); // enables lighting
		glEnable(GL_LIGHT0); // enables light0

		glEnable(GL_COLOR_MATERIAL); // enables opengl to use glColor3f to
										// define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

		this.eekysam = new TVFBuffer();
		TVFFile tvf = null;

		try
		{
			String fno = this.theTurrem.getDir() + "eekysam.tvf";

			File filein = new File(fno);
			DataInputStream input;

			input = new DataInputStream(new GZIPInputStream(new FileInputStream(filein)));

			tvf = TVFFile.read(input);

			input.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.eekysam.bindTVF(tvf);

		this.cart = new TVFBuffer();
		tvf = null;

		try
		{
			String fno = this.theTurrem.getDir() + "cart.tvf";

			File filein = new File(fno);
			DataInputStream input;
			input = new DataInputStream(new GZIPInputStream(new FileInputStream(filein)));

			tvf = TVFFile.read(input);

			input.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.cart.bindTVF(tvf);
	}

	private void initLightArrays()
	{
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(5.0f).put(5.0f).put(5.0f).put(0.0f).flip();

		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.2f).put(1.2f).put(1.2f).put(1.0f).flip();

		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.8f).put(0.8f).put(0.8f).put(1.0f).flip();
	}
	
	@Override
	public void end()
	{
		glClear(GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void tick()
	{
		GL11.glClearColor(0.5F, 0.5F, 0.5F, 1.0F);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		GL11.glRotatef(this.angle, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(3.0F * Math.sin(this.angle / 180.0F * Math.PI), -1.0F, -3.0F * Math.cos(this.angle / 180.0F * Math.PI));
		this.eekysam.render();
		this.cart.render();
		GL11.glPopMatrix();
		this.angle += 0.5F;
	}
}
