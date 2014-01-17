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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.render.RenderObject;
import zap.turrem.client.render.TVFBuffer;
import zap.turrem.utils.models.TVFFile;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateGame implements IState
{
	private Turrem theTurrem;

	private RenderObject eekysam;
	private RenderObject cart;

	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModelAmbient;

	private float angley = 0.0F;
	private float anglex = -20.0F;
	private float camx = 0.0F;
	private float camz = 1.0F;
	private float dist = 5.0F;

	private int mouselastx;
	private int mouselasty;

	private float fov = 60.0F;
	
	private int boxPointer;

	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, Config.getWidth() / Config.getHeight(), 0.1F, 20.0F);
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

		TVFBuffer tvfeekysam = new TVFBuffer();
		this.eekysam = new RenderObject("eekysam", 0);
		tvfeekysam.bindTVF(tvf, this.eekysam);

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

		TVFBuffer tvfcart = new TVFBuffer();
		this.cart = new RenderObject("cart", 1);
		tvfcart.bindTVF(tvf, this.cart);
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
		int wm = Mouse.getDWheel();
		if (Mouse.isButtonDown(2))
		{
			this.angley += (Mouse.getX() - this.mouselastx) * Config.getMouseSpeed();
			this.anglex += (Mouse.getY() - this.mouselasty) * Config.getMouseSpeed();
			if (this.anglex > -10.0F)
			{
				this.anglex = -10.0F;
			}
			if (this.anglex < -90.0F)
			{
				this.anglex = -90.0F;
			}
			this.angley %= 360.0F;
		}
		else if (wm != 0)
		{
			this.dist -= wm * 0.01F;
			if (this.dist < 2.0F)
			{
				this.dist = 2.0F;
			}
			if (this.dist > 20.0F)
			{
				this.dist = 20.0F;
			}
		}
		if (Mouse.isButtonDown(0))
		{
			float dx = (Mouse.getX() - this.mouselastx) * 0.002F * this.dist;
			float dy = (Mouse.getY() - this.mouselasty) * 0.002F * this.dist;
			float angrad = this.angley / 180.0F * 3.14F;
			float cos = (float) Math.cos(angrad);
			float sin = (float) Math.sin(angrad);
			this.camx += dx * cos;
			this.camx += dy * sin;
			this.camz -= dy * cos;
			this.camz += dx * sin;
		}
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
		GL11.glClearColor(0.5F, 0.5F, 0.5F, 1.0F);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		GL11.glTranslated(0.0F, 0.0F, -this.dist);
		GL11.glRotatef(this.anglex, -1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.angley, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(this.camx, -1.0F, this.camz);
		this.eekysam.doRender();
		this.cart.doRender();
		GL11.glPopMatrix();
	}
}
