package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.client.game.entity.EntityClient;

public class World
{
	public List<EntityClient> entityList = new ArrayList<EntityClient>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();
	
	public void tickEntities()
	{
		for (int i = 0; i < this.entityList.size(); i++)
		{
			EntityClient e = this.entityList.get(i);
			if (e.isDead || !e.isAppear)
			{
				this.entityList.remove(i--);
			}
			else
			{
				e.onTick();
			}
		}
	}
	
	public void cleanEntities()
	{
		for (int i = 0; i < this.entityList.size(); i++)
		{
			EntityClient e = this.entityList.get(i);
			if (e.isDead || !e.isAppear)
			{
				this.entityList.remove(i--);
			}
		}
	}
}
