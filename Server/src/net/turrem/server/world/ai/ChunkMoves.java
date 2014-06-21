package net.turrem.server.world.ai;

public class ChunkMoves
{
	private byte[] grid = new byte[128];
	
	protected byte getCell(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		int i = x + y * 16;
		byte cell = this.grid[i / 2];
		if (i % 2 != 0)
		{
			cell >>>= 4;
		}
		return (byte) (cell & 0x0F);
	}
	
	protected void setCell(int x, int y, byte cell)
	{
		x &= 0x0F;
		y &= 0x0F;
		int i = x + y * 16;
		byte cur = this.grid[i / 2];
		if (i % 2 == 0)
		{
			cur &= 0xF0;
		}
		else
		{
			cur &= 0x0F;
			cell <<= 4;
		}
		this.grid[i / 2] = (byte) (cur | (cell & 0x0F));
	}
	
	protected void setCell(int x, int y, boolean move, int dir)
	{
		x &= 0x0F;
		y &= 0x0F;
		int i = x + y * 16;
		byte cur = this.grid[i / 2];
		byte bit = 1;
		bit <<= dir % 4;
		if (i % 2 != 0)
		{
			bit <<= 4;
		}
		cur &= ~bit;
		if (move)
		{
			cur |= bit;
		}
		this.grid[i / 2] = cur;
	}
	
	public void setMoveXUp(int x, int y, boolean move)
	{
		this.setCell(x, y, move, 0);
	}
	
	public void setMoveYUp(int x, int y, boolean move)
	{
		this.setCell(x, y, move, 1);
	}
	
	public void setMoveXDown(int x, int y, boolean move)
	{
		this.setCell(x, y, move, 2);
	}
	
	public void setMoveYDown(int x, int y, boolean move)
	{
		this.setCell(x, y, move, 3);
	}
	
	public boolean getMoveXUp(int x, int y)
	{
		byte cell = this.getCell(x, y);
		return ((cell >>> 0) & 0x01) == 1;
	}
	
	public boolean getMoveYUp(int x, int y)
	{
		byte cell = this.getCell(x, y);
		return ((cell >>> 1) & 0x01) == 1;
	}
	
	public boolean getMoveXDown(int x, int y)
	{
		byte cell = this.getCell(x, y);
		return ((cell >>> 2) & 0x01) == 1;
	}
	
	public boolean getMoveYDown(int x, int y)
	{
		byte cell = this.getCell(x, y);
		return ((cell >>> 3) & 0x01) == 1;
	}
}
