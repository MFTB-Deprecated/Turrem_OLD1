package zap.turrem.client.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.client.render.object.RenderObject;
import zap.turrem.utils.models.TVFFile;
import zap.turrem.utils.models.TVFFile.TVFFace;
import zap.turrem.utils.models.TVFFile.TVFColor;

public class ModelChunk
{
	private TerrainChunk terc;
	private float peak;
	private float sea;
	
	private int height;
	
	public final int chunkX;
	public final int chunkY;
	
	public RenderObject render;
	
	public ModelChunk(TerrainChunk chunk, float peak, float sea, int height, int cx, int cy, RenderEngine engine)
	{
		this.terc = chunk;
		this.peak = peak;
		this.sea = sea;
		this.height = height;
		this.chunkX = cx;
		this.chunkY = cy;
		
		this.render = engine.addObject(this.makeTVF());
	}
	
	public void render()
	{
		GL11.glTranslatef(this.chunkX, 0.0F, this.chunkY);
		this.render.doRender();
		GL11.glTranslatef(-this.chunkX, 0.0F, -this.chunkY);
	}
	
	protected float getfH(short x, short y)
	{
		if (x < 0 || x >= 16 || y < 0 || y >= 16)
		{
			return Float.NaN;
		}
		return this.terc.ajustSurface(x, y, this.peak, this.sea, 0.0F);
	}
	
	protected int getH(int max, int x, int y, float h)
	{
		if (Float.isNaN(h))
		{
			return 0;
		}
		int H = (int) (max * h);
		if (H < 0)
		{
			H = 0;
		}
		return H;
	}
	
	protected int getH(int max, int x, int y)
	{
		return this.getH(max, (short) x, (short) y);
	}
	
	protected int getH(int max, short x, short y)
	{
		return this.getH(max, x, y, this.getfH(x, y));
	}
	
	protected TVFFile makeTVF()
	{
		TVFColor green = new TVFColor();
		green.id = 0x00;
		green.r = (byte) 0x0F;
		green.g = (byte) 0x5F;
		green.b = (byte) 0x0F;
		
		TVFColor blue = new TVFColor();
		blue.id = 0x01;
		blue.r = (byte) 0x1F;
		blue.g = (byte) 0x1F;
		blue.b = (byte) 0xCF;
		
		TVFColor[] colors = new TVFColor[] {green, blue};
		
		ArrayList<TVFFace> faces = new ArrayList<TVFFace>();
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				byte color = (byte) 0x00;
				float h = this.getfH((short)i, (short)j);
				int H = this.getH(this.height, i, j, h);
				if (h < 0.0F)
				{
					color = (byte) 0x01;
				}
				this.makeSpire(this.getH(this.height, i + 1, j), this.getH(this.height, i - 1, j), this.getH(this.height, i, j + 1), this.getH(this.height, i, j - 1), H, color, i, j, faces);
			}
		}
		
		TVFFace[] far = new TVFFace[faces.size()];
		far = faces.toArray(far);
		return new TVFFile(far, colors);
	}
	
	private void makeSpire(int hxu, int hxd, int hzu, int hzd, int h, byte color, int x, int z, ArrayList<TVFFace> faces)
	{
		TVFFace top = new TVFFace();
		top.color = color;
		top.dir = TVFFace.Dir_YUp;
		top.x = (byte) (x & 0xFF);
		top.z = (byte) (z & 0xFF);
		top.y = (byte) (h & 0xFF);
		faces.add(top);
		
		for (int i = h; i > hxu; i--)
		{
			TVFFace side = new TVFFace();
			side.dir = TVFFace.Dir_XUp;
			side.color = color;
			side.x = (byte) (x & 0xFF);
			side.z = (byte) (z & 0xFF);
			side.y = (byte) (i & 0xFF);
			faces.add(side);
		}
		
		for (int i = h; i > hzu; i--)
		{
			TVFFace side = new TVFFace();
			side.dir = TVFFace.Dir_ZUp;
			side.color = color;
			side.x = (byte) (x & 0xFF);
			side.z = (byte) (z & 0xFF);
			side.y = (byte) (i & 0xFF);
			faces.add(side);
		}
		
		for (int i = h; i > hxd; i--)
		{
			TVFFace side = new TVFFace();
			side.dir = TVFFace.Dir_XDown;
			side.color = color;
			side.x = (byte) (x & 0xFF);
			side.z = (byte) (z & 0xFF);
			side.y = (byte) (i & 0xFF);
			faces.add(side);
		}
		
		for (int i = h; i > hzd; i--)
		{
			TVFFace side = new TVFFace();
			side.dir = TVFFace.Dir_ZDown;
			side.color = color;
			side.x = (byte) (x & 0xFF);
			side.z = (byte) (z & 0xFF);
			side.y = (byte) (i & 0xFF);
			faces.add(side);
		}
	}
}
