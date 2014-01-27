package zap.turrem.client.game.entity;

import zap.turrem.client.game.operation.Operation;
import zap.turrem.client.game.select.SelectionEvent;

public interface IEntitySelectable
{
	public void runSelect(SelectionEvent event);
	public boolean isSelected();
	public void drawBox(float r, float g, float b);
	public void doOperation(Operation op);
}
