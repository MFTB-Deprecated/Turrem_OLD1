package zap.turrem.core.entity;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.tech.idea.Idea;

public abstract class UnitLearn extends EntityUnit implements IEntityLearnNode
{
	private static final float rotSpeed = 3.0F;
	private static final float bobSpeed = 5.0F;
	private static final float bobAmp = 0.05F;
	
	private float noteHeight;
	
	public ArrayList<Idea> newIdeas = new ArrayList<Idea>();

	private ModelIcon idea = new ModelIcon("turrem.interface.idea", 8.0F, 4.0F, 0.0F, 0.5F);
	
	public UnitLearn()
	{
		super();
		this.noteHeight = (float) this.getPickBounds().maxY;
		newIdeas.add(null);
	}

	public void onTick()
	{
		super.onTick();
	}

	public void render()
	{
		super.render();

		if (this.newIdeas.size() > 0)
		{
			float bob = (float) (Math.sin((((this.theWorld.getViewage() * bobSpeed) % 360) * 3.1415F) / 180.0F) * bobAmp);
			
			GL11.glPushMatrix();
			GL11.glTranslated(this.posX, this.posY + bob + this.noteHeight, this.posZ);
			GL11.glRotatef((this.theWorld.getViewage() * rotSpeed) % 360, 0.0F, 1.0F, 0.0F);
			idea.render();
			GL11.glPopMatrix();
		}
	}

	public void loadAssets(RenderManager man)
	{
		super.loadAssets(man);
		man.pushIcon(idea, "interface", RenderObjectHolderSimple.class);
		idea.loadMe();
	}

}
