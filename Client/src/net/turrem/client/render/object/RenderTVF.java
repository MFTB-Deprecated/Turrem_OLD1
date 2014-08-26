package net.turrem.client.render.object;

import net.turrem.client.render.TVFLayerBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class RenderTVF
{
	private int verts = -1;
	private int normals = -1;
	private int colors = -1;
	private int vertnum = -1;
	private int vao = -1;
	
	public boolean delete()
	{
		boolean flag = false;
		if (this.vao != -1)
		{
			GL15.glDeleteBuffers(this.verts);
			GL15.glDeleteBuffers(this.colors);
			GL15.glDeleteBuffers(this.normals);
			GL30.glDeleteVertexArrays(this.vao);
			flag = true;
		}
		this.verts = -1;
		this.colors = -1;
		this.normals = -1;
		this.vertnum = -1;
		this.vao = -1;
		return flag;
	}
	
	public void doRender()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.verts);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colors);
		GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, 0, 0);

		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.normals);
		GL11.glNormalPointer(GL11.GL_BYTE, 0, 0);

		GL11.glDrawArrays(GL11.GL_QUADS, 0, this.vertnum);
	}
	
	public void push(TVFLayerBuffer buffer)
	{
		this.verts = buffer.getVboVertsId();
		this.colors = buffer.getVboColorsId();
		this.normals = buffer.getVboNormalsId();
		this.vertnum = buffer.getVertCount();
		this.vao = buffer.getVaoId();
	}
}
