package zap.turrem.core.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

public class NBTCompound extends NBTTag
{
	private HashMap<String, NBTTag> payload = new HashMap<String, NBTTag>();

	public NBTCompound()
	{
		super();
	}

	public NBTCompound(String name)
	{
		super(name);
	}

	@Override
	public byte getId()
	{
		return 10;
	}

	@Override
	void writePayload(DataOutput dataoutput) throws IOException
	{
		for (NBTTag tag : this.payload.values())
		{
			tag.write(dataoutput);
		}
		dataoutput.write((byte) 0);
	}

	@Override
	void readPayload(DataInput datainput, int depth) throws IOException
	{
		NBTTag tag;
		while ((tag = NBTTag.read(datainput, depth + 1)).getId() != 0)
		{
			this.payload.put(tag.getName(), tag);
		}
	}

	public void setTag(String name, NBTTag tag)
	{
		tag.setName(name);
		this.payload.put(name, tag);
	}

	public void setTag(NBTTag tag)
	{
		this.payload.put(tag.getName(), tag);
	}

	public NBTTag getTag(String name)
	{
		return this.payload.get(name);
	}

	public void setString(String name, String tag)
	{
		NBTString string = new NBTString(name);
		string.setString(tag);
		this.setTag(string);
	}

	public String getString(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTString)
		{
			return ((NBTString) tag).getString();
		}
		return "";
	}

	public void setByte(String name, byte tag)
	{
		NBTByte b = new NBTByte(name);
		b.setByte(tag);
		this.setTag(b);
	}

	public byte getByte(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTByte)
		{
			return ((NBTByte) tag).getByte();
		}
		return 0;
	}
	
	public void setBool(String name, boolean tag)
	{
		NBTByte b = new NBTByte(name);
		b.setByte((byte) (tag ? 1 : 0));
		this.setTag(b);
	}

	public boolean getBool(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTByte)
		{
			return ((NBTByte) tag).getByte() == 1;
		}
		return false;
	}
	
	public void setShort(String name, short tag)
	{
		NBTShort nbt = new NBTShort(name);
		nbt.setShort(tag);
		this.setTag(nbt);
	}

	public short getShort(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTShort)
		{
			return ((NBTShort) tag).getShort();
		}
		return 0;
	}
	
	public void setInt(String name, int tag)
	{
		NBTInt nbt = new NBTInt(name);
		nbt.setInt(tag);
		this.setTag(nbt);
	}

	public int getInt(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTInt)
		{
			return ((NBTInt) tag).getInt();
		}
		return 0;
	}
	
	public void setLong(String name, long tag)
	{
		NBTLong nbt = new NBTLong(name);
		nbt.setLong(tag);
		this.setTag(nbt);
	}

	public long getLong(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTLong)
		{
			return ((NBTLong) tag).getLong();
		}
		return 0;
	}
	
	public void setFloat(String name, float tag)
	{
		NBTFloat nbt = new NBTFloat(name);
		nbt.setFloat(tag);
		this.setTag(nbt);
	}

	public float getFloat(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTFloat)
		{
			return ((NBTFloat) tag).getFloat();
		}
		return Float.NaN;
	}
	
	public void setDouble(String name, double tag)
	{
		NBTDouble nbt = new NBTDouble(name);
		nbt.setDouble(tag);
		this.setTag(nbt);
	}

	public double getDouble(String name)
	{
		NBTTag tag = this.getTag(name);
		if (tag != null && tag instanceof NBTDouble)
		{
			return ((NBTDouble) tag).getDouble();
		}
		return Double.NaN;
	}
}
