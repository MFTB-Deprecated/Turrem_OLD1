package net.turrem.client.game.world;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.turrem.client.Config;
import net.turrem.client.game.ClientGame;
import net.turrem.client.game.PlayerFace;
import net.turrem.client.game.world.material.MaterialList;
import net.turrem.client.network.GameConnection;
import net.turrem.client.network.client.request.RequestChunk;
import net.turrem.client.network.server.ServerPacket;
import net.turrem.client.network.server.ServerPacketMaterialSync;
import net.turrem.client.network.server.ServerPacketStartingInfo;
import net.turrem.client.network.server.ServerPacketTerrain;
import net.turrem.client.render.engine.RenderEngine;
import net.turrem.utils.geo.Point;

public class ClientWorld
{
	public ClientGame theGame;

	public ChunkStorage chunks;

	public long worldTime = 0;

	public RenderEngine chunkRender;

	public GameConnection theConnection;

	public ClientWorld(ClientGame game)
	{
		this.theGame = game;
		this.chunkRender = new RenderEngine();
		this.chunks = new ChunkStorage(Config.chunkStorageWidth);
	}

	public void render()
	{
		this.worldTime++;
		Point foc = this.theGame.getFace().getFocus();
		int px = (int) foc.xCoord;
		int pz = (int) foc.zCoord;
		int mincx = (px >> 4) - Config.chunkCheckRenderDistance;
		int mincz = (pz >> 4) - Config.chunkCheckRenderDistance;
		int maxcx = (px >> 4) + Config.chunkCheckRenderDistance;
		int maxcz = (pz >> 4) + Config.chunkCheckRenderDistance;
		ChunkGroup[] aro = this.chunks.getChunksAround(px >> 4, pz >> 4);
		for (int i = 0; i < 4; i++)
		{
			ChunkGroup cg = aro[i];
			if (cg != null)
			{
				cg.renderChunks(mincx, mincz, maxcx, maxcz, px, pz, this);
			}
		}
		this.renderEntities();

		this.theConnection.processPackets();

		if (!this.theConnection.isRunning())
		{
			this.theGame.stopGame();
		}
	}

	public void renderChunk(Chunk chunk, int px, int pz)
	{
		int dist = Config.chunkRenderDistance * Config.chunkRenderDistance;
		int x = chunk.chunkx * 16 + 8;
		int z = chunk.chunkz * 16 + 8;
		x -= px;
		z -= pz;
		if (x * x + z * z < dist)
		{
			chunk.render();
		}
	}

	public void renderEntities()
	{
		
	}

	public Chunk getChunk(int chunkx, int chunkz)
	{
		return this.chunks.getChunk(chunkx, chunkz);
	}

	public int getHeight(int x, int z, int empty)
	{
		Chunk c = this.getChunk(x >> 4, z >> 4);
		if (c == null)
		{
			return empty;
		}
		return c.getHeight(x, z);
	}

	public int getRayHeight(double x1, double z1, double x2, double z2, double extend)
	{
		double x = x1;
		double z = z1;
		double dx = (x2 - x1);
		double dz = (z2 - z1);
		double adx = Math.abs(dx);
		double adz = Math.abs(dz);
		if (adx < 1.0 && adz < 1.0)
		{
			return this.getHeight((int) x, (int) z);
		}
		double length = Math.sqrt(dx * dx + dz * dz);
		dx /= length;
		dz /= length;
		double l = 0.0;
		double dl = 1.0 / Math.max(adz, adx);
		dz *= dl;
		dx *= dl;
		int height = Integer.MIN_VALUE;
		double lim = length + extend;
		while (l < lim)
		{
			l += dl;
			x += dx;
			z += dz;
			int h = this.getHeight((int) x, (int) z, Integer.MIN_VALUE);
			if (h > height)
			{
				height = h;
			}
		}
		return height;
	}

	public int requestNullChunks(int centerx, int centerz, int select, int num)
	{
		if (!Config.shouldRequestChunks)
		{
			return 0;
		}
		int chunkx = centerx >> 4;
		int chunkz = centerz >> 4;
		int ind = 0;
		for (int dist = 1; dist <= Config.chunkRequestDistance * 2; dist += 2)
		{
			int hal = dist / 2;

			for (int i = -hal; i <= hal; i++)
			{
				if (this.getChunk(chunkx + i, chunkz + hal) == null)
				{
					if (select <= ind)
					{
						this.requestChunk(chunkx + i, chunkz + hal);
					}
					ind++;
					if (ind - select == num)
					{
						return select += num;
					}
				}
			}

			for (int i = -hal + 1; i < hal; i++)
			{
				if (this.getChunk(chunkx + i, chunkz - hal) == null)
				{
					if (select <= ind)
					{
						this.requestChunk(chunkx + i, chunkz - hal);
					}
					ind++;
					if (ind - select == num)
					{
						return select += num;
					}
				}
			}

			for (int i = -hal; i < hal; i++)
			{
				if (this.getChunk(chunkx + hal, chunkz + i) == null)
				{
					if (select <= ind)
					{
						this.requestChunk(chunkx + hal, chunkz + i);
					}
					ind++;
					if (ind - select == num)
					{
						return select += num;
					}
				}
			}

			for (int i = -hal; i < hal; i++)
			{
				if (this.getChunk(chunkx - hal, chunkz + i) == null)
				{
					if (select <= ind)
					{
						this.requestChunk(chunkx - hal, chunkz + i);
					}
					ind++;
					if (ind - select == num)
					{
						return select += num;
					}
				}
			}
		}

		return 0;
	}

	private void requestChunk(int chunkx, int chunkz)
	{
		RequestChunk req = new RequestChunk(chunkx, chunkz);
		this.theConnection.addToSendQueue(req.getPacket());
	}

	public int getHeight(int x, int z)
	{
		return this.getHeight(x, z, -1);
	}

	public void startNetwork(String username) throws IOException
	{
		Socket socket = new Socket(Config.turremServerHost, Config.turremServerPort);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF(username);
		this.theConnection = new GameConnection(socket, this);
	}

	public void end()
	{
		if (this.theConnection != null)
		{
			this.theConnection.shutdown("Client closed");
		}
	}

	public void processPacket(ServerPacket pack)
	{
		if (pack instanceof ServerPacketMaterialSync)
		{
			ServerPacketMaterialSync sync = (ServerPacketMaterialSync) pack;
			MaterialList.put(sync);
		}
		if (pack instanceof ServerPacketTerrain)
		{
			ServerPacketTerrain terr = (ServerPacketTerrain) pack;
			Chunk c = terr.buildChunk();
			this.chunks.setChunk(c);
			c.buildRender(this.chunkRender);
		}
		if (pack instanceof ServerPacketStartingInfo)
		{
			ServerPacketStartingInfo sti = (ServerPacketStartingInfo) pack;
			PlayerFace face = this.theGame.getFace();
			face.setFocus(Point.getPoint(sti.startx + 0.5D, 128.0D, sti.startz + 0.5D));
		}
	}
}
