package zap.turrem.core.tech;

import zap.turrem.core.tech.item.JavaTechItem;
import zap.turrem.core.tech.item.TechItem;

public abstract class Tech
{
	public abstract int getPassCount();

	public abstract String getName(int pass);

	public final String getIdentifier(int pass)
	{
		return getStaticIdentifier(this, pass);
	}

	public static final String getStaticIdentifier(Tech tech, int pass)
	{
		return getClassIdentifier(tech.getClass(), pass);
	}

	public static final String getClassIdentifier(Class<? extends Tech> tech, int pass)
	{
		String name = tech.getName();
		name += "#" + pass;
		return name;
	}

	public TechItem buildItem(int pass)
	{
		return new JavaTechItem(this, pass);
	}
}
