package net.turrem.app.client.render.icon;

import org.lwjgl.opengl.GL11;

public abstract class RenderIcon2D implements IRenderIcon
{
	protected static float[][] verts = new float[][] { new float[] { 0.0F, 0.0F }, new float[] { 1.0F, 0.0F }, new float[] { 1.0F, 1.0F }, new float[] { 0.0F, 1.0F } };
	protected static int[] inds = new int[] { 0, 1, 2, 2, 3, 0 };
	
	public void render2D(float xmin, float ymin, float xmax, float ymax)
	{
		this.render2D(xmin, ymin, xmax, ymax, 0, false, false);
	}
	
	public void render2D(float xmin, float ymin, float xmax, float ymax, int rotate, boolean flipx, boolean flipy)
	{
		this.start();
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (int i = 0; i < 6; i++)
		{
			this.renderVert2D(xmin, ymin, xmax, ymax, i, rotate, flipx, flipy);
		}
		GL11.glEnd();
		this.end();
	}
	
	private void renderVert2D(float xmin, float ymin, float xmax, float ymax, int num, int rotate, boolean flipx, boolean flipy)
	{
		int ind = inds[num];
		int indt = (ind + rotate) % 4;
		float x = xmin + verts[ind][0] * (xmax - xmin);
		float y = ymin + verts[ind][1] * (ymax - ymin);
		float tx = verts[indt][0];
		float ty = verts[indt][1];
		if (flipx)
		{
			tx = 1.0F - tx;
		}
		if (flipy)
		{
			ty = 1.0F - ty;
		}
		GL11.glTexCoord2f(tx, ty);
		GL11.glVertex2f(x, y);
	}
	
	public abstract void start();
	
	public abstract void end();
}
