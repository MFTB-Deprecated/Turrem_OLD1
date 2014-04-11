package net.turrem.server.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.turrem.utils.nbt.NBTCompound;

public class ChunkGroup
{
	public World theWorld;
	public Chunk[] chunks = new Chunk[4096];
	public final int groupx;
	public final int groupy;

	public ChunkGroup(int groupx, int groupy, World world)
	{
		this.groupx = groupx;
		this.groupy = groupy;
		this.theWorld = world;
	}

	public Chunk getChunk(int chunkx, int chunky)
	{
		chunkx &= 0x3F;
		chunky &= 0x3F;
		return this.doGetChunk(chunkx, chunky);
	}

	public Chunk getChunkClamped(int chunkx, int chunky)
	{
		if (chunkx < (groupx * 64) || chunky < (groupy * 64) || chunkx >= (groupx * 64 + 64) || chunky >= (groupy * 64 + 64))
		{
			return null;
		}
		return this.getChunk(chunkx, chunky);
	}

	public void onTick()
	{

	}

	public void unloadAll()
	{
		for (int i = 0; i < this.chunks.length; i++)
		{
			this.unloadChunk(i);
		}
	}

	public void tickUnload()
	{
		for (int i = 0; i < this.chunks.length; i++)
		{
			if (this.chunks[i] != null)
			{
				if (this.chunks[i].tickUnload(2))
				{
					this.unloadChunk(i);
				}
			}
		}
	}

	private void unloadChunk(int k)
	{
		try
		{
			this.saveChunkFile(k);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.chunks[k] = null;
	}

	public Chunk doGetChunk(int i, int j)
	{
		int ind = i + (j << 6);
		Chunk c = this.chunks[ind];
		if (c == null)
		{
			c = this.provide(i, j);
			this.chunks[ind] = c;
		}
		return c;
	}

	protected Chunk provide(int chunkx, int chunky)
	{
		int ind = chunkx + (chunky << 6);
		Chunk c = null;
		try
		{
			c = this.loadChunkFile(ind);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (c != null)
		{
			return c;
		}
		else
		{
			return new Chunk(chunkx, chunky);
		}
	}

	protected boolean saveChunkFile(int k) throws IOException
	{
		Chunk chunk = this.chunks[k];
		if (chunk == null)
		{
			return false;
		}
		String fn = this.theWorld.saveLoc + k + ".chunk";
		File file = new File(fn);
		file.createNewFile();
		DataOutputStream out;
		try
		{
			out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		chunk.writeToNBT().writeAsRoot(out);
		out.close();
		return true;
	}

	protected Chunk loadChunkFile(int k) throws IOException
	{
		String fn = this.theWorld.saveLoc + k + ".chunk";
		File file = new File(fn);
		if (!file.exists())
		{
			return null;
		}
		DataInputStream in;
		try
		{
			in = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
		NBTCompound nbt = NBTCompound.readAsRoot(in);
		in.close();
		int chunkx = k & 0x3F;
		int chunky = k >> 6;
		Chunk chunk = Chunk.readFromNBT(nbt, chunkx, chunky);
		return chunk;
	}
}
