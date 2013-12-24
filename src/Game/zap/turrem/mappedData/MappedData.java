package zap.turrem.mappedData;

import java.util.HashMap;

public class MappedData
{
	protected HashMap<String, Object> data = new HashMap<String, Object>();

	public MappedData()
	{

	}

	public void setByte(String key, byte dat)
	{
		this.data.put(key, dat);
	}

	public byte getByte(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof Byte)
			{
				return (Byte) dat;
			}
		}
		return -1;
	}

	public void setShort(String key, short dat)
	{
		this.data.put(key, dat);
	}

	public short getShort(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof Short)
			{
				return (Short) dat;
			}
		}
		return -1;
	}

	public void setInt(String key, int dat)
	{
		this.data.put(key, dat);
	}

	public int getInt(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof Integer)
			{
				return (Integer) dat;
			}
		}
		return -1;
	}

	public void setLong(String key, long dat)
	{
		this.data.put(key, dat);
	}

	public long getLong(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof Long)
			{
				return (Long) dat;
			}
		}
		return -1;
	}

	public void setString(String key, String dat)
	{
		this.data.put(key, dat);
	}

	public String getString(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof String)
			{
				return (String) dat;
			}
		}
		return null;
	}

	public void setByteArray(String key, byte[] dat)
	{
		this.data.put(key, dat);
	}

	public byte[] getByteArray(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof byte[])
			{
				return (byte[]) dat;
			}
		}
		return null;
	}

	public void setIntArray(String key, int[] dat)
	{
		this.data.put(key, dat);
	}

	public int[] getIntArray(String key)
	{
		if (this.data.containsKey(key))
		{
			Object dat = this.data.get(key);
			if (dat instanceof int[])
			{
				return (int[]) dat;
			}
		}
		return null;
	}

	public void setObject(String key, Object dat)
	{
		this.data.put(key, dat);
	}

	public Object getObject(String key)
	{
		if (this.data.containsKey(key))
		{
			return this.data.get(key);
		}
		return null;
	}

	public void clear()
	{
		this.data.clear();
	}

	public HashMap<String, Object> getMap()
	{
		return this.data;
	}
}
