package zap.turrem.client.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.config.Config;
import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.client.render.object.RenderObject;
import zap.turrem.utils.ao.AORay;
import zap.turrem.utils.ao.Urchin;
import zap.turrem.utils.geo.EnumDir;
import zap.turrem.utils.graphics.RgbConvert;
import zap.turrem.utils.models.TVFFile;
import zap.turrem.utils.models.TVFFile.TVFFace;
import zap.turrem.utils.models.TVFFile.TVFColor;

public class ModelChunk
{
	protected static Urchin urchin = new Urchin(Config.tvfOcclusionRaySize, Config.tvfOcclusionRays);
	private final boolean doao;

	private Chunk terc;

	public final int chunkX;
	public final int chunkY;

	public RenderObject render;

	public ModelChunk(Chunk chunk, RenderEngine engine)
	{
		this.terc = chunk;
		this.chunkX = chunk.chunkx;
		this.chunkY = chunk.chunky;
		this.doao = Config.useTvfRayOcclusion;
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
		if (this.doao)
		{
			tvf.prelit = 2;
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
		if (this.doao)
		{
			float ao = this.getAO(urchin, x, h, z, EnumDir.YUp);
			ao *= 2;
			if (ao > 1)
			{
				ao = 1;
			}
			byte bl = (byte) (ao * 255);
			top.light = new byte[] { bl, bl, bl, bl };
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
			if (this.doao)
			{
				float ao = this.getAO(urchin, x, i, z, EnumDir.XUp);
				ao *= 2;
				if (ao > 1)
				{
					ao = 1;
				}
				byte bl = (byte) (ao * 255);
				side.light = new byte[] { bl, bl, bl, bl };
			}
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
			if (this.doao)
			{
				float ao = this.getAO(urchin, x, i, z, EnumDir.ZUp);
				ao *= 2;
				if (ao > 1)
				{
					ao = 1;
				}
				byte bl = (byte) (ao * 255);
				side.light = new byte[] { bl, bl, bl, bl };
			}
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
			if (this.doao)
			{
				float ao = this.getAO(urchin, x, i, z, EnumDir.XDown);
				ao *= 2;
				if (ao > 1)
				{
					ao = 1;
				}
				byte bl = (byte) (ao * 255);
				side.light = new byte[] { bl, bl, bl, bl };
			}
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
			if (this.doao)
			{
				float ao = this.getAO(urchin, x, i, z, EnumDir.ZDown);
				ao *= 2;
				if (ao > 1)
				{
					ao = 1;
				}
				byte bl = (byte) (ao * 255);
				side.light = new byte[] { bl, bl, bl, bl };
			}
			faces.add(side);
		}
	}

	public float getAO(Urchin urchin, int x, int y, int z, EnumDir dir)
	{
		float ao = 0.0F;
		for (int i = 0; i < urchin.rays.length; i++)
		{
			boolean hit = false;
			AORay r = urchin.rays[i];
			for (int j = 0; j < r.points.length; j++)
			{
				int px = r.points[j].x + x;
				int py = r.points[j].y + y;
				int pz = r.points[j].z + z;
				if (px >= 16 || px < 0 || py >= 32 || py < 0 || pz >= 16 || pz < 0)
				{
					break;
				}
				if (this.getH(px, pz) >= py)
				{
					hit = true;
					break;
				}
			}
			if (!hit)
			{
				switch (dir)
				{
					case XUp:
						ao += r.xd;
						break;
					case XDown:
						ao += r.xu;
						break;
					case YUp:
						ao += r.yd;
						break;
					case YDown:
						ao += r.yu;
						break;
					case ZUp:
						ao += r.zd;
						break;
					case ZDown:
						ao += r.zu;
						break;
				}
			}
		}
		switch (dir)
		{
			case XUp:
				return ao / urchin.xd;
			case XDown:
				return ao / urchin.xu;
			case YUp:
				return ao / urchin.yd;
			case YDown:
				return ao / urchin.yu;
			case ZUp:
				return ao / urchin.zd;
			case ZDown:
				return ao / urchin.zu;
			default:
				return 0;
		}
	}
}
