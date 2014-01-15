package zap.turrem.client.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import zap.turrem.utils.models.TVFFile;

public class TVFBuffer
{
	public static final int[][] offs = new int[][] { new int[] { 1, 0, 1 }, new int[] { 0, 0, 1 }, new int[] { 0, 0, 0 }, new int[] { 1, 0, 0 }, new int[] { 1, 1, 1 }, new int[] { 0, 1, 1 }, new int[] { 0, 1, 0 }, new int[] { 1, 1, 0 } };
	public static final int[][] offinds = new int[][] { new int[] { 4, 0, 3, 7 }, new int[] { 1, 5, 6, 2 }, new int[] { 4, 7, 6, 5 }, new int[] { 0, 1, 2, 3 }, new int[] { 0, 4, 5, 1 }, new int[] { 2, 6, 7, 3 } };

	private int vaoId = 0;
	private int vboId = 0;
	private int vbocId = 0;
	private int vbonId = 0;

	private int vertnum;
	
	public void bindTVF(TVFFile tvf)
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

				verts[ind + 0] = (x + foffs[0]) / 20.0F;
				verts[ind + 1] = (y + foffs[1]) / 20.0F;
				verts[ind + 2] = (z + foffs[2]) / 20.0F;
				
				switch (f.dir & 0xFF)
				{
					case 1:
						norms[ind + 0] = 1.0F;
						break;
					case 2:
						norms[ind + 0] = -1.0F;
						break;
					case 3:
						norms[ind + 1] = 1.0F;
						break;
					case 4:
						norms[ind + 1] = -1.0F;
						break;
					case 5:
						norms[ind + 2] = 1.0F;
						break;
					case 6:
						norms[ind + 2] = -1.0F;
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
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind) -
		// VERTICES
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind) - COLORS
		vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		vbonId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbonId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
	}

	public void render()
	{
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
		
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbonId);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

		// If you are not using IBOs:
		GL11.glDrawArrays(GL11.GL_QUADS, 0, this.vertnum);
	}
}
