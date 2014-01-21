package zap.turrem.core.config;

public class ConfigVarInt extends ConfigVar
{
	private int data;
	
	public ConfigVarInt(String key)
	{
		super(key);
	}

	@Override
	public Object getObject()
	{
		return this.data;
	}
	
	public int getInt()
	{
		return this.data;
	}
	
	public void setInt(int i)
	{
		this.data = i;
	}
}
