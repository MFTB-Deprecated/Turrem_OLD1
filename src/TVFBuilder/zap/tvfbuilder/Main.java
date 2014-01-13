package zap.tvfbuilder;

import zap.tvfbuilder.tvf.VoxConverter;

public class Main
{
	public static void main(String[] args)
	{
		String fni = "C:/Users/Sam Sartor/Turrem/TVFBuilder/Test VOX/eekysam.vox";
		String fno = "C:/Users/Sam Sartor/Turrem/TVFBuilder/Test VOX/eekysam.tvf";
		
		VoxConverter.convertFile(fni, fno);
	}
}
