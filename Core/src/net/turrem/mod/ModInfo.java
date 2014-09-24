package net.turrem.mod;

import java.util.List;

import com.google.common.base.Strings;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class ModInfo
{
	private static final JdomParser JDOM_PARSER = new JdomParser();
	
	public final String name;
	public final String identifier;
	public final String version;
	public final String[] dependencies;
	
	public ModInfo(String info) throws InvalidSyntaxException
	{
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
		this.identifier = Strings.nullToEmpty(json.getNullableStringValue("identifier"));
	}
}
