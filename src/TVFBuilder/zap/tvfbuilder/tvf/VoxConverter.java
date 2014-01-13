package zap.tvfbuilder.tvf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

import zap.tvfbuilder.vxl.VoxelGrid;

public class VoxConverter
{
	public static void convertFile(String fni, String fno)
	{
		try
		{
			File filein = new File(fni);
			DataInputStream input = new DataInputStream(new FileInputStream(filein));

			VoxelGrid vox = VoxelGrid.getGrid(input);

			input.close();

			File fileout = new File(fno);
			fileout.createNewFile();
			DataOutputStream output = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(fileout)));

			TVFWriter tvf = new TVFWriter(vox);
			tvf.build();
			tvf.write(output);

			output.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
