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

import net.turrem.server.entity.Entity;
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

	public void entityChunkTick(int mincx, int mincz, int maxcx, int maxcz, int px, int pz)
	{
		mincx -= this.groupx * 64;
		mincz -= this.groupy * 64;
		maxcx -= this.groupx * 64;
		maxcz -= this.groupy * 64;
		mincx = mincx < 0 ? 0 : mincx;
		mincx = mincx >= 64 ? 64 : mincx;
		mincz = mincz < 0 ? 0 : mincz;
		mincz = mincz >= 64 ? 64 : mincz;
		maxcx = maxcx < 0 ? 0 : maxcx;
		maxcx = maxcx >= 64 ? 64 : maxcx;
		maxcz = maxcz < 0 ? 0 : maxcz;
		maxcz = maxcz >= 64 ? 64 : maxcz;
		for (int i = mincx; i < maxcx; i++)
		{
			for (int j = mincz; j < maxcz; j++)
			{
				int k = i + (j << 6);
				Chunk c = this.chunks[k];
				if (c == null)
				{
					c = this.provide(i + this.groupx * 64, j + this.groupy * 64);
					this.chunks[k] = c;
				}
				c.onEntityTick();
			}
		}
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
		/*
		 * try { this.saveChunkFile(k); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		int chunkx = this.chunks[k].chunkx;
		int chunkz = this.chunks[k].chunkz;
		chunkx *= 16;
		chunkz *= 16;
		for (Entity ent : this.theWorld.entities)
		{
			if (ent.x >= chunkx && ent.x < chunkx + 16 && ent.z >= chunkz && ent.z < chunkz + 16)
			{
				ent.unload();
			}
		}
		this.chunks[k] = null;
	}

	public Chunk doGetChunk(int chunkx, int chunky)
	{
		int i = chunkx & 0x3F;
		int j = chunky & 0x3F;
		int k = i + (j << 6);
		Chunk c = this.chunks[k];
		if (c == null)
		{
			c = this.provide(chunkx, chunky);
			this.chunks[k] = c;
		}
		return c;
	}

	protected Chunk provide(int chunkx, int chunky)
	{
		Chunk c = null;
		try
		{
			c = this.loadChunkFile(chunkx, chunky);
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
			return this.generateChunk(chunkx, chunky);
		}
	}

	protected Chunk generateChunk(int chunkx, int chunky)
	{
		Chunk chunk = new Chunk(chunkx, chunky, this.theWorld.theWorldGen.generateChunk(chunkx, chunky));
		chunk.decorate(this.theWorld.theWorldGen, this.theWorld.theTurrem);
		return chunk;
	}

	protected boolean saveChunkFile(int k) throws IOException
	{
		Chunk chunk = this.chunks[k];
		if (chunk == null)
		{
			return false;
		}
		int chunkx = chunk.chunkx;
		int chunky = chunk.chunkz;
		String fn = this.theWorld.saveLoc + chunkx + "_" + chunky + ".dat";
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
		NBTCompound nbt = new NBTCompound();
		nbt.setCompound("dat", chunk.writeToNBT());
		nbt.writeAsRoot(out);
		out.close();
		return true;
	}

	protected Chunk loadChunkFile(int chunkx, int chunky) throws IOException
	{
		String fn = this.theWorld.saveLoc + chunkx + "_" + chunky + ".dat";
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
		Chunk chunk = Chunk.readFromNBT(nbt.getCompound("dat"), chunkx, chunky);
		return chunk;
	}
}