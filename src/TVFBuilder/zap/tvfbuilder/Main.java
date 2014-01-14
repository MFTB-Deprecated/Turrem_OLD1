package zap.tvfbuilder;

import zap.tvfbuilder.tvf.VoxConverter;

public class Main
{
	public static void main(String[] args)
	{
		VoxConverter.convertFile(args[0], args[1]);
	}
}
