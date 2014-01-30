package zap.turrem.server.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class BasketDecoder extends ByteToMessageDecoder
{
	private int length = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
	{
		if (this.length == -1)
		{
			if (in.readableBytes() < 4)
			{
				return;
			}
			this.length = in.readInt();
		}
		if (in.readableBytes() < this.length)
		{
			return;
		}
		out.add(in.readBytes(this.length));
		this.length = -1;
	}

	@Override
	protected void handlerRemoved0(ChannelHandlerContext ctx)
	{
		this.length = -1;
	}
}
