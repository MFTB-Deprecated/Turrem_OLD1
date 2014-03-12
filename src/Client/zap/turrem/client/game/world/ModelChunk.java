package zap.turrem.client.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.config.Config;
import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.client.render.object.RenderObject;
import zap.turrem.client.render.object.model.TVFBuffer;
import zap.turrem.utils.graphics.RgbConvert;
import zap.turrem.utils.models.TVFFile;
import zap.turrem.utils.models.TVFFile.TVFFace;
import zap.turrem.utils.models.TVFFile.TVFColor;

public class ModelChunk
{
	private final boolean doNeiAO;

	private Chunk terc;

	public final int chunkX;
	public final int chunkY;

	public RenderObject render;

	public ModelChunk(Chunk chunk, RenderEngine engine)
	{
		this.terc = chunk;
		this.chunkX = chunk.chunkx;
		this.chunkY = chunk.chunky;
		this.doNeiAO = Config.useNeighborOcclusion;
		this.render = engine.addObject(this.makeTVF(), 5.0F, 0.0F, 0.0F, 0.0F);
	}

	public void render()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(this.chunkX * 3.2F, 0.0F, this.chunkY * 3.2F);
		this.render.doRender();
		GL11.glPopMatrix();
	}

	protected TVFFile makeTVF()
	{
		ArrayList<Integer> usedColors = new ArrayList<Integer>();
		ArrayList<TVFFace> faces = new ArrayList<TVFFace>();
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				int color = this.terc.getColor(i, j);
				int id = usedColors.indexOf(color);
				if (id == -1)
				{
					id = usedColors.size();
					usedColors.add(color);
				}
				int H = this.getH(i, j);
				this.makeSpire(this.getH(i + 1, j), this.getH(i - 1, j), this.getH(i, j + 1), this.getH(i, j - 1), H, (byte) id, i, j, faces);
			}
		}

		TVFFace[] far = new TVFFace[faces.size()];
		far = faces.toArray(far);

		TVFColor[] colors = new TVFColor[usedColors.size()];
		for (int i = 0; i < usedColors.size(); i++)
		{
			int c = usedColors.get(i);
			TVFColor tvfc = new TVFColor();
			tvfc.id = (byte) i;
			tvfc.r = (byte) (RgbConvert.getRed(c) & 0xFF);
			tvfc.g = (byte) (RgbConvert.getGreen(c) & 0xFF);
			tvfc.b = (byte) (RgbConvert.getBlue(c) & 0xFF);
			colors[i] = tvfc;
		}
		TVFFile tvf = new TVFFile(far, colors);
		if (this.doNeiAO)
		{
			tvf.prelit = 1;
		}
		return tvf;
	}

	private int getH(int i, int j)
	{
		if (i < 0 || j < 0 || i >= 16 || j >= 16)
		{
			return 0;
		}
		return this.terc.getHeight(i, j);
	}

	private void makeSpire(int hxu, int hxd, int hzu, int hzd, int h, byte color, int x, int z, ArrayList<TVFFace> faces)
	{
		TVFFace top = new TVFFace();
		top.color = color;
		top.dir = TVFFace.Dir_YUp;
		top.x = (byte) (x & 0xFF);
		top.z = (byte) (z & 0xFF);
		top.y = (byte) (h & 0xFF);
		if (this.doNeiAO)
		{
			int[] vinds = TVFBuffer.offinds[TVFFace.Dir_YUp];
			byte[] aos = new byte[4];
			for (int i = 0; i < 4; i++)
			{
				float ao = (this.getVertexAO(x, z, TVFBuffer.offs[vinds[i]], h) + 2) / 5.0F;
				aos[i] = (byte) (ao * 255);
			}
			top.light = aos;
		}
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
	
	public float getVertexAO(int x, int z, int[] verts, int h)
	{
		return this.getVertexAO(this.getH(x, z + verts[0] * 2 - 1) > h, this.getH(x + verts[2] * 2 - 1, z) > h, this.getH(x + verts[2] * 2 - 1, z + verts[0] * 2 - 1) > h);
	}

	public float getVertexAO(boolean side1, boolean side2, boolean corner)
	{
		if (side1 && side2)
		{
			return 0;
		}
		return 3 - ((side1 ? 1 : 0) + (side2 ? 1 : 0) + (corner ? 1 : 0));
	}
}
