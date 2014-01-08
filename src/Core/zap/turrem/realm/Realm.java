package zap.turrem.realm;

import zap.turrem.player.Player;

public class Realm
{
	private Player theMaster;
	
	public Realm(Player thePlayer)
	{
		this.theMaster = thePlayer;
	}

	public Player getTheMaster()
	{
		return theMaster;
	}
}
