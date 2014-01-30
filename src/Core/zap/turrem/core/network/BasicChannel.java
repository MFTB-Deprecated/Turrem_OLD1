package zap.turrem.core.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class BasicChannel extends ChannelHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		try
		{
			
		}
		finally
		{
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		ctx.close();
	}
}
