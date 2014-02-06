package zap.turrem.core.network;

import zap.turrem.client.game.network.OperationClient;
import zap.turrem.server.network.OperationServer;

public interface IOperation
{
	public Class<? extends OperationClient> getClient();
	
	public Class<? extends OperationServer> getServer();
}
