package zap.turrem.core.config;

public class ConfigVarFloat extends ConfigVar
{
	private float data;
	
	public ConfigVarFloat(String key)
	{
		super(key);
	}

	@Override
	public Object getObject()
	{
		return this.data;
	}
	
	public float getFloat()
	{
		return this.data;
	}
	
	public void setFloat(float i)
	{
		this.data = i;
	}
}