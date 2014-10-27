package net.turrem.app.client.render.object;

import net.turrem.app.client.render.RenderEngine;
import net.turrem.app.client.render.TVFLayerBuffer;
import net.turrem.tvf.ITVFRenderInterface;
import net.turrem.tvf.TVFFile;
import net.turrem.tvf.color.EnumDynamicColorMode;
import net.turrem.tvf.layer.TVFLayer;
import net.turrem.tvf.layer.TVFLayerFaces;
import net.turrem.tvf.rendermodel.TVFRenderModel;

public class RenderTVF implements ITVFRenderInterface
{
	public TVFLayerBuffer[] buffers;
	public TVFRenderModel model;
	public final Object[] parameters = new Object[256];
	public RenderEngine theEngine;
	
	public RenderTVF(TVFFile tvf, float scale, boolean doAO, RenderEngine engine)
	{
		this.theEngine = engine;
		this.parameters[0] = true;
		this.buffers = new TVFLayerBuffer[tvf.layers.size()];
		for (int i = 0; i < this.buffers.length; i++)
		{
			TVFLayer l = tvf.layers.get(i);
			if (l != null && l instanceof TVFLayerFaces)
			{
				TVFLayerBuffer buf = new TVFLayerBuffer();
				buf.bindTVF((TVFLayerFaces) l, scale, doAO);
				this.buffers[i] = buf;
			}
		}
		this.model = tvf.getRender();
	}
	
	public boolean delete()
	{
		boolean flag = false;
		for (int i = 0; i < this.buffers.length; i++)
		{
			if (this.buffers[i] != null)
			{
				flag |= this.buffers[i].delete();
			}
		}
		return flag;
	}
	
	public void doRender()
	{
		this.model.render(this, this.parameters);
	}
	
	public void setParameter(int channel, Object par)
	{
		this.parameters[channel] = par;
	}
	
	public void setParameters(int startChannel, Object[] pars)
	{
		for (int i = 0; i < pars.length; i++)
		{
			int j = i + startChannel;
			if (j >= 256)
			{
				break;
			}
			else
			{
				this.parameters[j] = pars[i];
			}
		}
	}
	
	@Override
	public void setDynamicColor(EnumDynamicColorMode mode, Object color)
	{
	}
	
	@Override
	public void clearDynamicColor()
	{
	}
	
	@Override
	public void setShader(String shader, Object uniforms)
	{
	}
	
	@Override
	public void clearShader()
	{
	}
	
	@Override
	public void renderLayer(int layerindex)
	{
		if (this.buffers[layerindex] != null)
		{
			this.buffers[layerindex].doRender();
		}
	}
}
