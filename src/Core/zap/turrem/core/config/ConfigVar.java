package zap.turrem.core.config;

public abstract class ConfigVar
{
	public String key;
	
	public ConfigVar(String key)
	{
		this.key = key;
	}
	
	public abstract Object getObject();
}
