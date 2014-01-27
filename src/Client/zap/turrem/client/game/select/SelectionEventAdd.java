package zap.turrem.client.game.select;

public class SelectionEventAdd extends SelectionEvent
{
	public long uidSelected[];
	
	public SelectionEventAdd(long... id)
	{
		this.uidSelected = id;
	}

	@Override
	public boolean getSelected(long uid, boolean current)
	{
		if (current)
		{
			return true;
		}
		
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
