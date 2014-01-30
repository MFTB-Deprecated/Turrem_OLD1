package zap.turrem.core.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class BasketDecoder extends ByteToMessageDecoder
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
	{
		if (in.readableBytes() < 5)
		{
			return;
		}
		
		in.markReaderIndex();
		
		char magic = in.readChar();
		if (magic != 'F')
		{
			in.resetReaderIndex();
			throw new CorruptedFrameException("Invalid magic number: " + magic);
		}
		
		int length = in.readInt();
		
		if (in.readableBytes() < length)
		{
			in.resetReaderIndex();
			return;
		}
		
		out.add(in.readBytes(length));
	}
}
