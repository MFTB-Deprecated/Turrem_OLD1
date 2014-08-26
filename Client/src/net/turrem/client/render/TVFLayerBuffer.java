package net.turrem.client.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.turrem.client.Config;
import net.turrem.client.render.object.RenderTVF;
import net.turrem.tvf.color.TVFColor;
import net.turrem.tvf.color.TVFPaletteColor;
import net.turrem.tvf.face.EnumLightingType;
import net.turrem.tvf.face.TVFFace;
import net.turrem.tvf.layer.TVFLayerFaces;
import net.turrem.utils.geo.FaceUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import org.lwjgl.BufferUtils;

public class TVFLayerBuffer
{

	public static final int[][] offs = FaceUtils.vertexOffset;
	public static final int[][] offinds = FaceUtils.vertexOffsetIndices;

	private int vaoId = 0;
	/**
	 * VBO vertex object id
	 */
	private int vboId = 0;
	/**
	 * VBO color object id
	 */
	private int vbocId = 0;
	/**
	 * VBO normal object id
	 */
	private int vbonId = 0;

	/**
	 * Number of verticies in this VBO
	 */
	private int vertnum;

	public void bindTVF(TVFLayerFaces tvf, RenderTVF obj, float scale, boolean doAO)
	{
		int facenum = tvf.faces.size();
		this.vertnum = facenum * 4;
		float[] verts = new float[this.vertnum * 3];
		byte[] colors = new byte[this.vertnum * 3];
		byte[] norms = new byte[this.vertnum * 3];

		TVFPaletteColor pal = null;
		if (tvf.palette instanceof TVFPaletteColor)
		{
			pal = (TVFPaletteColor) tvf.palette;
		}

		TVFColor defc = new TVFColor(0xFFFFF);

		for (int i = 0; i < facenum; i++)
		{
			TVFFace f = tvf.faces.get(i);
			TVFColor c = defc;
			if (pal != null)
			{
				c = pal.palette[f.color & 0xFF];
				if (c == null)
				{
					c = defc;
				}
			}

			int[] foffinds = offinds[(f.direction & 0xFF)];

			for (int j = 0; j < 4; j++)
			{
				float aoMult = 1.0F;
				if (tvf.prelightType == EnumLightingType.SMOOTH)
				{
					aoMult = (f.lighting[j] & 0xFF) / 255.0F;
				}
				else if (tvf.prelightType == EnumLightingType.SOLID)
				{
					aoMult = (f.lighting[0] & 0xFF) / 255.0F;
				}
				aoMult *= Config.finalAoSampleMult;
				aoMult += (1.0F - Config.finalAoSampleMult);

				int ind = ((i * 4) + j) * 3;
				if (doAO)
				{
					colors[ind + 0] = (byte) (int) (c.getRedInt() * aoMult);
					colors[ind + 1] = (byte) (int) (c.getGreenInt() * aoMult);
					colors[ind + 2] = (byte) (int) (c.getBlueInt() * aoMult);
				}
				else
				{
					colors[ind + 0] = c.getRed();
					colors[ind + 1] = c.getGreen();
					colors[ind + 2] = c.getBlue();
				}

				float x = f.x & 0xFF;
				float y = f.y & 0xFF;
				float z = f.z & 0xFF;

				int[] foffs = offs[foffinds[j]];

				verts[ind + 0] = (x + foffs[0]) / scale;
				verts[ind + 1] = (y + foffs[1]) / scale;
				verts[ind + 2] = (z + foffs[2]) / scale;

				norms[ind + ((f.direction & 0xFF) / 2)] = Byte.MAX_VALUE;
			}
		}

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verts.length);
		verticesBuffer.put(verts);
		verticesBuffer.flip();

		ByteBuffer colorsBuffer = BufferUtils.createByteBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();

		ByteBuffer normalsBuffer = BufferUtils.createByteBuffer(norms.length);
		normalsBuffer.put(norms);
		normalsBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		this.vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind) -
		// VERTICES
		this.vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		if (tvf.palette instanceof TVFPaletteColor && tvf.palette != null)
		{
			// Create a new VBO for the indices and select it (bind) - COLORS
			this.vbocId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbocId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 3, GL11.GL_BYTE, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
		else
		{
			this.vbocId = 0;
		}

		this.vbonId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbonId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_BYTE, true, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		obj.push(this);
	}

	public final int getVboVertsId()
	{
		return this.vboId;
	}

	public final int getVboColorsId()
	{
		return this.vbocId;
	}

	public final int getVboNormalsId()
	{
		return this.vbonId;
	}

	public final int getVertCount()
	{
		return this.vertnum;
	}

	public final int getVaoId()
	{
		return this.vaoId;
	}
}
