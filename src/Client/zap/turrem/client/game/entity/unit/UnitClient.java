package zap.turrem.client.game.entity.unit;

import zap.turrem.client.game.RealmClient;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.core.entity.article.EntityArticle;

public class UnitClient extends EntityClient
{
	public UnitClient(EntityArticle article)
	{
		super(article);
	}

	RealmClient owner;
}
