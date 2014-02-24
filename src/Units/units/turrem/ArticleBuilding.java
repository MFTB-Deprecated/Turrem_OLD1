package units.turrem;

import org.lwjgl.input.Keyboard;

import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.article.EntityArticle;

public abstract class ArticleBuilding extends EntityArticle
{
	@Override
	public void tick(Entity entity)
	{
	}

	@Override
	public void keyEvent(boolean me, Entity entity)
	{
		if (me)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_DELETE)
			{
				entity.kill();
			}
		}
	}

	@Override
	public void mouseEvent(boolean me, Entity entity)
	{
	}
}
