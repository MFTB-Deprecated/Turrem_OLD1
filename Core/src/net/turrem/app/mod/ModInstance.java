package net.turrem.app.mod;

import com.google.common.base.Strings;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class ModInstance
{
	public static enum ModType
	{
		CLIENT_ONLY,
		SERVER_ONLY,
		CLIENT_OPTIONAL_SERVER,
		SERVER_OPTIONAL_CLIENT,
		CLIENT_AND_SERVER,
		NO_MOD;
	}
	
	private static final JdomParser JDOM_PARSER = new JdomParser();
	
	public final String name;
	public final String identifier;
	public final String version;
	public final ModType type;
	
	public final String url;
	public final String srcurl;
	
	public ModInstance(String info, String identifier) throws InvalidSyntaxException
	{
		this.identifier = identifier;
		JsonRootNode json = JDOM_PARSER.parse(info);
		
		this.version = Strings.nullToEmpty(json.getNullableStringValue("version"));
		this.name = Strings.nullToEmpty(json.getNullableStringValue("name"));
		
		this.url = Strings.nullToEmpty(json.getNullableStringValue("url"));
		this.srcurl = Strings.nullToEmpty(json.getNullableStringValue("srcurl"));
		
		ModType t = ModType.CLIENT_AND_SERVER;
		
		try
		{
			t = ModType.valueOf(Strings.nullToEmpty(json.getNullableStringValue("type")));
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			t = ModType.CLIENT_AND_SERVER;
		}
		
		this.type = t;
	}
	
	@Override
	public int hashCode()
	{
		return (this.identifier + this.version).hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ModInstance)
		{
			ModInstance mod = (ModInstance) obj;
			return mod.identifier == this.identifier && mod.version == this.version;
		}
		return false;
	}
}
