package zap.turrem.client.game.entity;

import zap.turrem.client.game.select.SelectionEvent;
import zap.turrem.core.entity.article.EntityArticle;

public class EntitySelectable extends EntityClient implements IEntitySelectable
{
	private boolean selected = false;
	
	public EntitySelectable(EntityArticle article)
	{
		super(article);
	}
	
	public void runSelect(SelectionEvent event)
	{
		this.selected = event.getSelected(this.uid, this.selected);
	}
	
	public boolean isSelected()
	{
		return this.selected;
	}
	
	public void render()
	{
		super.render();
		
		if (this.isSelected())
		{
			this.drawBox(1.0F, 0.8F, 0.0F);
		}
	}
}
