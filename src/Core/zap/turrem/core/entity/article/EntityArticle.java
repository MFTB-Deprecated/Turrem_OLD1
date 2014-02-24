package zap.turrem.core.entity.article;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.Entity;
import zap.turrem.utils.geo.Box;

public abstract class EntityArticle
{
	protected void drawAnIcon(float x, float y, float z, ModelIcon icon)
	{
		GL11.glTranslatef(x, y, z);
		icon.render();
		GL11.glTranslatef(-x, -y, -z);
	}
	
	protected void drawAnIcon(double x, double y, double z, ModelIcon icon)
	{
		GL11.glTranslated(x, y, z);
		icon.render();
		GL11.glTranslated(-x, -y, -z);
	}
	
	public abstract void draw(Entity entity);
	
	public abstract void tick(Entity entity);
	
	public String getIdentifier()
	{
		return this.getClass().getName();
	}
	
	public abstract void loadAssets(RenderManager man);
	
	public abstract Box updateBounds();
	
	public abstract void keyEvent(boolean me, Entity entity);
	
	public abstract void mouseEvent(boolean me, Entity entity);
}
