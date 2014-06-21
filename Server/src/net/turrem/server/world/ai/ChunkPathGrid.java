package net.turrem.server.world.ai;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ChunkPathGrid
{
	private float[] weights;
	protected float[] grid = new float[256];

	protected static class GridPoint
	{
		public int x;
		public int y;
		public float dist;
	}

	protected Queue<GridPoint> calc = new LinkedList<GridPoint>();
	protected ChunkMoves moves;

	private ChunkPathGrid(float[] weights, ChunkMoves moves)
	{
		Arrays.fill(grid, Float.NaN);
		this.weights = weights;
		this.moves = moves;
	}

	protected void runOnPoint(int x, int y)
	{
		GridPoint p = new GridPoint();
		p.x = x & 0x0F;
		p.y = y & 0x0F;
		p.dist = 0.0F;
		this.calc.add(p);
		this.run();
	}

	protected void run()
	{
		while (!this.calc.isEmpty())
		{
			GridPoint p = this.calc.poll();
			if (p.x >= 0 && p.y >= 0 && p.x < 16 && p.y < 16)
			{
				this.tickPoint(p);
			}
		}
	}

	private void tickPoint(GridPoint point)
	{
		float dist = point.dist;
		int i = point.x + point.y * 16;
		if (this.weights[i] > 0.0F)
		{
			dist += this.weights[i];
		}
		else
		{
			dist += 1.0F;
		}
		if (Float.isNaN(this.grid[i]) || dist < this.grid[i])
		{
			this.grid[i] = dist;
			byte moves = this.moves.getCell(point.x, point.y);
			if ((moves & 0x01) == 1)
			{
				GridPoint p = new GridPoint();
				p.x = point.x + 1;
				p.y = point.y;
				p.dist = dist;
				this.calc.add(p);
			}
			moves >>>= 1;
			if ((moves & 0x01) == 1)
			{
				GridPoint p = new GridPoint();
				p.x = point.x;
				p.y = point.y + 1;
				p.dist = dist;
				this.calc.add(p);
			}
			moves >>>= 1;
			if ((moves & 0x01) == 1)
			{
				GridPoint p = new GridPoint();
				p.x = point.x - 1;
				p.y = point.y;
				p.dist = dist;
				this.calc.add(p);
			}
			moves >>>= 1;
			if ((moves & 0x01) == 1)
			{
				GridPoint p = new GridPoint();
				p.x = point.x;
				p.y = point.y - 1;
				p.dist = dist;
				this.calc.add(p);
			}
		}
	}
}
