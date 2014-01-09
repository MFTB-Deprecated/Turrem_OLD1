package zap.turrem.core.realm;

import zap.turrem.core.player.Player;

public class Realm
{
	private Player theMaster;

	public Realm(Player thePlayer)
	{
		this.theMaster = thePlayer;
	}

	public Player getTheMaster()
	{
		return this.theMaster;
	}
}
