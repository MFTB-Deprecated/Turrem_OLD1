package net.turrem.client.game.entity;

import net.turrem.client.game.world.ClientWorld;
import net.turrem.client.load.control.GameEntity;
import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.object.model.ModelIcon;

@GameEntity(id = "citizen")
public class ClientEntityCitizen extends ClientEntity
{
	public final static String assetfold = "turrem.entity.unit.citizen.";
	public final static String[] assetpeople = new String[] { "cave", "eekysam" };
	private static ModelIcon[] people;
	private static boolean loaded = false;

	static
	{
		people = new ModelIcon[assetpeople.length];
		for (int i = 0; i < people.length; i++)
		{
			people[i] = new ModelIcon(assetfold + assetpeople[i], 2.0F, 0.5F, 0.0F, 0.5F);
		}
	}

	public ClientEntityCitizen(long id, ClientWorld world)
	{
		super(id, world);
		if (this.theWorld.theGame.mine == -1)
		{
			this.theWorld.theGame.mine = this.entityId;
		}
	}

	@Override
	public void renderEntity()
	{
		people[this.getModelNumber(people.length)].render();
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		if (!loaded)
		{
			loaded = true;
			for (int i = 0; i < people.length; i++)
			{
				man.pushIcon(people[i], "citizen");
				people[i].load(man);
			}
		}
	}

	@Override
	public int onRemove(boolean death)
	{
		return 0;
	}
}
