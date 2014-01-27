package zap.turrem.client.game.select;

public class SelectionEventUnAll extends SelectionEvent
{
	@Override
	public boolean getSelected(long uid, boolean current)
	{
		return false;
	}
}
