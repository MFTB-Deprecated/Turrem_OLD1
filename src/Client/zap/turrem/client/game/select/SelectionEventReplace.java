package zap.turrem.client.game.select;

public class SelectionEventReplace extends SelectionEvent
{
	public long uidSelected[];
	
	public SelectionEventReplace(long... id)
	{
		this.uidSelected = id;
	}
	
	@Override
	public boolean getSelected(long uid, boolean current)
	{
		for (long i : this.uidSelected)
		{
			if (i == uid)
			{
				return true;
			}
		}
		
		return false;
	}
}
