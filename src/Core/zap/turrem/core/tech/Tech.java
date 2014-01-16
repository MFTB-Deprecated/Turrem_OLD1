package zap.turrem.core.tech;

import zap.turrem.core.tech.item.JavaTechItem;
import zap.turrem.core.tech.item.TechItem;

/**
 * This class defines everything about a particular tech. This class may be constructed for each use, so it must act in a static manner, with no feilds. Can be used to define multiple techs, each with a different pass number,
 */
public abstract class Tech
{
	/**
	 * For how many different (but simmilat) techs should this class be used
	 * @return The pass count
	 */
	public abstract int getPassCount();

	/**
	 * Gets the name of this tech
	 * @param pass The pass number, differentiates between techs that use this class
	 * @return Name
	 */
	public abstract String getName(int pass);

	/**
	 * Gets the techs string identifier
	 * @param pass The pass number, differentiates between techs that use this class
	 * @return String tech identifier
	 */
	public final String getIdentifier(int pass)
	{
		return getStaticIdentifier(this, pass);
	}

	/**
	 * Gets the string identifier of the specified tech
	 * @param tech The tech 
	 * @param pass The pass number, differentiates between techs that use the same class
	 * @return String tech identifier
	 */
	public static final String getStaticIdentifier(Tech tech, int pass)
	{
		return getClassIdentifier(tech.getClass(), pass);
	}

	/**
	 * Gets the string identifier of the specified tech class. Used by all getIdentifier() varients
	 * @param tech The tech class 
	 * @param pass The pass number, differentiates between techs that use the same class
	 * @return String tech identifier
	 */
	public static final String getClassIdentifier(Class<? extends Tech> tech, int pass)
	{
		String name = tech.getName();
		name += "#" + pass;
		return name;
	}

	/**
	 * Creates a TechItem that represents this tech
	 * @param pass The pass number, differentiates between techs that also use this class
	 * @return
	 */
	public TechItem buildItem(int pass)
	{
		return new JavaTechItem(this, pass);
	}
}
