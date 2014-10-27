package net.turrem.app.client.render.object;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.turrem.app.client.render.RenderEngine;
import net.turrem.app.utils.graphics.ImgUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class RenderObjectTexture implements IRenderObject
{
	private String source;
	private String identifier;
	private int glSample;
	private int textureId = -1;
	
	private int width = -1;
	private int height = -1;
	private float aspect = Float.NaN;
	
	public RenderObjectTexture(String source, String id, boolean pixel)
	{
		this.source = source;
		this.identifier = id;
		if (pixel)
		{
			this.glSample = GL11.GL_NEAREST;
		}
		else
		{
			this.glSample = GL11.GL_LINEAR;
		}
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public float getAspect()
	{
		return this.aspect;
	}
	
	@Override
	public boolean isLoaded()
	{
		return this.textureId != -1;
	}
	
	@Override
	public boolean load(RenderEngine render)
	{
		if (this.isLoaded())
		{
			return false;
		}
		BufferedImage img = render.loadTexture(this);
		if (img != null)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
			this.aspect = (float) this.width / (float) this.height;
			ByteBuffer bytes = ImgUtils.imgToByteBuffer(img);
			
			int texId = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.glSample);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			
			this.textureId = texId;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean reload(RenderEngine render)
	{
		if (this.isLoaded())
		{
			this.unload(render);
		}
		BufferedImage img = render.loadTexture(this);
		if (img != null)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
			this.aspect = (float) this.width / (float) this.height;
			ByteBuffer bytes = ImgUtils.imgToByteBuffer(img);
			
			int texId = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.glSample);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			
			this.textureId = texId;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean unload(RenderEngine render)
	{
		GL11.glDeleteTextures(this.textureId);
		this.textureId = -1;
		return true;
	}
	
	public boolean bind()
	{
		GL11.glPushMatrix();
		if (this.textureId != -1)
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
			return true;
		}
		return false;
	}
	
	public void unbind()
	{
		if (this.textureId != -1)
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	public String getSource()
	{
		return this.source;
	}
	
	@Override
	public String getIdentifier()
	{
		return this.identifier;
	}
}
