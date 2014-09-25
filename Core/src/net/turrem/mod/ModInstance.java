package net.turrem.mod;

import java.util.List;

import com.google.common.base.Strings;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
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
		CLIENT_AND_SERVER;
	}

	private static final JdomParser JDOM_PARSER = new JdomParser();

	public final String name;
	public final String identifier;
	public final String version;
	public final String[] dependencies;
	public final ModType type;

	public ModInstance(String info, String identifier) throws InvalidSyntaxException
	{
		this.identifier = identifier;
		JsonRootNode json = JDOM_PARSER.parse(info);
		List<JsonNode> deps = json.getNullableArrayNode("dependencies");
		if (deps == null)
		{
			this.dependencies = new String[0];
		}
		else
		{
			this.dependencies = new String[deps.size()];
			for (int i = 0; i < this.dependencies.length; i++)
			{
				this.dependencies[i] = Strings.nullToEmpty(deps.get(i).getNullableStringValue());
			}
		}
		this.version = Strings.nullToEmpty(json.getNullableStringValue("version"));
		this.name = Strings.nullToEmpty(json.getNullableStringValue("name"));
		ModType t = ModType.CLIENT_AND_SERVER;
		
		try
		{
			t = ModType.valueOf(Strings.nullToEmpty(json.getNullableStringValue("identifier")));
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
