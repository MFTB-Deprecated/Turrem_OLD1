package zap.turrem.client.game.world;

import java.util.Random;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.EntitySolid;
import zap.turrem.utils.geo.Box;

public class Tree extends EntitySolid
{
	public final static String assetfold = "turrem.entity.feature.tree.";
	public final static String[] assettrees = new String[] {"juniper_1", "juniper_2"};
	private static ModelIcon[] trees;
	private static boolean loaded = false;
	static
	{
		trees = new ModelIcon[assettrees.length];
		for (int i = 0; i < trees.length; i++)
		{
			trees[i] = new ModelIcon(assetfold + assettrees[i], 12.0F, 0.0F, 0.0F, 0.0F);
		}
	}

	private int thistree;
	
	public Tree()
	{
		super();
		this.thistree = (new Random()).nextInt(trees.length);
	}
	
	public void loadAssets(RenderManager man)
	{
		super.loadAssets(man);
		if (!loaded)
		{
			loaded = true;
			for (int i = 0; i < trees.length; i++)
			{
				man.pushIcon(trees[i], "tree", RenderObjectHolderSimple.class);
			}
			man.getHolder("tree").load();
		}
	}
	
	@Override
	public Box pickBounds()
	{
		return null;
	}

	@Override
	protected void renderEntity()
	{
		trees[this.thistree].render();
	}
}
