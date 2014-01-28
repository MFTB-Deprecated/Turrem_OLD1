package zap.turrem.client.render.font;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import zap.turrem.utils.graphics.ImgUtils;

public class Font
{
	protected float aspect;
	protected int textureId;
	
	public int width;
	public int height;
	
	public final String name;

	public Font(String name)
	{
		this.name = name;
	}

	public int loadTextureFile(File fontfile) throws IOException
	{
		BufferedImage img = ImageIO.read(fontfile);
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.aspect = (float) this.width / (float) this.height;
		ByteBuffer bytes = ImgUtils.imgToByteBuffer(img);

		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		this.textureId = texId;
		return this.textureId;
	}
	
	public void push()
	{
		FontList.fonts.put(this.name, this);
	}
	
	public void unload()
	{
		GL11.glDeleteTextures(this.textureId);
	}

	public final float getAspect()
	{
		return aspect;
	}

	public final int getTextureId()
	{
		return textureId;
	}
}
