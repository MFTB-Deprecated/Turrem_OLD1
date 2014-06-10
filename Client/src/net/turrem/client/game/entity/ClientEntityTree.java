package net.turrem.client.game.entity;

import net.turrem.client.game.world.ClientWorld;
import net.turrem.client.load.control.GameEntity;
import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.object.model.ModelIcon;

@GameEntity(id = "tree")
public class ClientEntityTree extends ClientEntity
{
	public final static String assetfold = "turrem.entity.feature.tree.";
	public final static String[] assettrees = new String[] { "juniper_1", "juniper_2"};
	public final static float[] scaletrees = new float[] { 6.0F, 4.0F, 4.0F };
	private static ModelIcon[] trees;
	private static boolean loaded = false;

	static
	{
		trees = new ModelIcon[assettrees.length];
		for (int i = 0; i < trees.length; i++)
		{
			trees[i] = new ModelIcon(assetfold + assettrees[i], scaletrees[i], 0.5F, 0.0F, 0.5F);
		}
	}

	public ClientEntityTree(long id, ClientWorld world)
	{
		super(id, world);
	}

	@Override
	public void renderEntity()
	{
		trees[this.getModelNumber(trees.length)].render();
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		if (!loaded)
		{
			loaded = true;
			for (int i = 0; i < trees.length; i++)
			{
				man.pushIcon(trees[i], "tree");
				trees[i].load(man);
			}
		}
	}

	@Override
	public int onRemove(boolean death)
	{
		return 0;
	}
}
