package zap.turrem.client.game;

import java.util.Random;

import zap.turrem.core.player.Player;
import zap.turrem.core.realm.Realm;

public class RealmClient extends Realm
{
	public Random rand = new Random();

	public RealmClient(Player thePlayer)
	{
		super(thePlayer);
	}
	
	public void onStart()
	{
		
	}
	
	public void onTick()
	{
		
	}
}
