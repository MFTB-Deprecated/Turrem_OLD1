package network.turrem.operation;

import zap.turrem.client.game.network.OperationClient;
import zap.turrem.core.network.IOperation;
import zap.turrem.server.network.OperationServer;

public class OperationMove implements IOperation
{
	public class ClientMove extends OperationClient
	{
		
	}

	public class ServerMove extends OperationServer
	{
		
	}
	
	@Override
	public Class<? extends OperationClient> getClient()
	{
		return ClientMove.class;
	}

	@Override
	public Class<? extends OperationServer> getServer()
	{
		return ServerMove.class;
	}
}
