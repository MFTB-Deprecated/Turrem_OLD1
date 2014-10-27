package net.turrem.app.client.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.turrem.blueutils.geo.VoxelGeoUtils;
import net.turrem.tvf.color.TVFColor;
import net.turrem.tvf.color.TVFPaletteColor;
import net.turrem.tvf.face.TVFFace;
import net.turrem.tvf.layer.TVFLayerFaces;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TVFLayerBuffer
{
	public static final int[][] offs = VoxelGeoUtils.vertexOffset;
	public static final int[][] offinds = VoxelGeoUtils.vertexOffsetIndices;
	
	public static final int[][] triangulate = new int[][] { new int[] { 0, 1, 2, 2, 3, 0 }, new int[] { 3, 0, 1, 1, 2, 3 } };
	
	private int vaoId = -1;
	/**
	 * VBO vertex object id
	 */
	private int vboId = -1;
	/**
	 * VBO color object id
	 */
	private int vbocId = -1;
	/**
	 * VBO normal object id
	 */
	private int vbonId = -1;
	
	/**
	 * Number of verticies in this VBO
	 */
	private int vertnum;
	
	public void bindTVF(TVFLayerFaces tvf, float scale, boolean doAO)
	{
		this.delete();
		int facenum = tvf.faces.size();
		this.vertnum = facenum * 6;
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
			
			boolean flipped = false;
			
			if (f.lighting.length == 4)
			{
				flipped = (f.lighting[0] & 0xFF) + (f.lighting[2] & 0xFF) < (f.lighting[1] & 0xFF) + (f.lighting[3] & 0xFF);
			}
			
			for (int j = 0; j < 6; j++)
			{
				int k = triangulate[flipped ? 0 : 1][j];
				int ind = ((i * 6) + j) * 3;
				colors[ind + 0] = c.getRed();
				colors[ind + 1] = c.getGreen();
				colors[ind + 2] = c.getBlue();
				
				float x = f.x & 0xFF;
				float y = f.y & 0xFF;
				float z = f.z & 0xFF;
				
				int[] foffs = offs[foffinds[k]];
				
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
			this.vbocId = -1;
		}
		
		this.vbonId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbonId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_BYTE, true, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
	}
	
	public boolean delete()
	{
		boolean flag = false;
		if (this.vaoId != -1)
		{
			GL15.glDeleteBuffers(this.vboId);
			if (this.vbocId != 0)
			{
				GL15.glDeleteBuffers(this.vbocId);
			}
			GL15.glDeleteBuffers(this.vbonId);
			GL30.glDeleteVertexArrays(this.vaoId);
			flag = true;
		}
		this.vboId = -1;
		this.vbocId = -1;
		this.vbonId = -1;
		this.vertnum = 0;
		this.vaoId = -1;
		return flag;
	}
	
	public void doRender()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		
		if (this.vbocId != -1)
		{
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbocId);
			GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, 0, 0);
		}
		
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbonId);
		GL11.glNormalPointer(GL11.GL_BYTE, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertnum);
	}
	
	public int getVboVertsId()
	{
		return this.vboId;
	}
	
	public int getVboColorsId()
	{
		return this.vbocId;
	}
	
	public int getVboNormalsId()
	{
		return this.vbonId;
	}
	
	public int getVertCount()
	{
		return this.vertnum;
	}
	
	public int getVaoId()
	{
		return this.vaoId;
	}
}
