package net.turrem.client.render.object.model;

import java.nio.FloatBuffer;

import net.turrem.client.render.object.RenderObject;
import net.turrem.utils.models.TVFFile;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Binds a TVF file to a LWJGL VBO (Vertex Buffer Object) for rendering
 */
public class TVFBuffer
{
	/**
	 * A list of vertex offsets to be used when converting faces in the file to
	 * vertices in the VBO
	 */
	public static final int[][] offs = new int[][] { new int[] { 1, 0, 1 }/*0*/, new int[] { 0, 0, 1 }/*1*/, new int[] { 0, 0, 0 }/*2*/, new int[] { 1, 0, 0 }/*3*/, new int[] { 1, 1, 1 }/*4*/, new int[] { 0, 1, 1 }/*5*/, new int[] { 0, 1, 0 }/*6*/, new int[] { 1, 1, 0 }/*7*/ };
	/**
	 * A list specifing which vericies in offs belong to which faces on a cube
	 */
	public static final int[][] offinds = new int[][] { new int[] { 4, 7, 3, 0 }/*XUp*/, new int[] { 5, 6, 2, 1 }/*XDown*/, new int[] { 4, 7, 6, 5 }/*YUp*/, new int[] { 0, 3, 2, 1 }/*YDown*/, new int[] { 4, 0, 1, 5 }/*ZUp*/, new int[] { 7, 3, 2, 6 }/*ZDown*/ };

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

	/**
	 * Binds a TVF file to a VBO
	 * 
	 * @param tvf The TVF file
	 * @param obj The render object to push to
	 */
	public void bindTVF(TVFFile tvf, RenderObject obj, float scale, float xo, float yo, float zo)
	{
		this.vertnum = tvf.faceNum * 4;
		float[] verts = new float[this.vertnum * 3];
		float[] colors = new float[this.vertnum * 3];
		float[] norms = new float[this.vertnum * 3];

		for (int i = 0; i < tvf.faceNum; i++)
		{
			TVFFile.TVFFace f = tvf.faces[i];
			int cind = 0;
			int cid = f.color & 0xFF;
			for (int j = 0; j < tvf.colorNum; j++)
			{
				if ((tvf.colors[j].id & 0xFF) == cid)
				{
					cind = j;
					break;
				}
			}
			TVFFile.TVFColor c = tvf.colors[cind];

			int[] foffinds = offinds[(f.dir & 0xFF) - 1];

			for (int j = 0; j < 4; j++)
			{
				int ind = ((i * 4) + j) * 3;
				colors[ind + 0] = (c.r & 0xFF) / 255.0F;
				colors[ind + 1] = (c.g & 0xFF) / 255.0F;
				colors[ind + 2] = (c.b & 0xFF) / 255.0F;

				float x = f.x & 0xFF;
				float y = f.y & 0xFF;
				float z = f.z & 0xFF;

				int[] foffs = offs[foffinds[j]];

				verts[ind + 0] = (x + foffs[0] - xo) / scale;
				verts[ind + 1] = (y + foffs[1] - yo) / scale;
				verts[ind + 2] = (z + foffs[2] - zo) / scale;

				float length = (f.light[j] & 0xFF) / 255.0F;
				switch (f.dir & 0xFF)
				{
					case 1:
						norms[ind + 0] = length;
						break;
					case 2:
						norms[ind + 0] = -length;
						break;
					case 3:
						norms[ind + 1] = length;
						break;
					case 4:
						norms[ind + 1] = -length;
						break;
					case 5:
						norms[ind + 2] = length;
						break;
					case 6:
						norms[ind + 2] = -length;
						break;
				}
			}
		}

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verts.length);
		verticesBuffer.put(verts);
		verticesBuffer.flip();

		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();

		FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(norms.length);
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

		// Create a new VBO for the indices and select it (bind) - COLORS
		this.vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.vbonId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbonId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
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
