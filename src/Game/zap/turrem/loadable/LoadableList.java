package zap.turrem.loadable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoadableList<E extends Loadable>
{
	protected List<E> list = new ArrayList<E>();
	protected Map<String, Integer> identifierMap = new HashMap<String, Integer>();

	public int count()
	{
		return list.size();
	}
	
	public Iterator<E> getIterator()
	{
		return this.list.iterator();
	}

	public int add(E item, String name)
	{
		int id = list.size();
		list.add(item);
		identifierMap.put(name, id);
		return id;
	}

	public E get(Class<? extends E> tech, int pass)
	{
		String name = getIdentifier(tech, pass);
		return get(name);
	}

	public String getIdentifier(Class<? extends E> item, int pass)
	{
		String name = JarLoader.getStaticIdentifier(item, pass);
		return name;
	}

	public E get(int index)
	{
		return list.get(index);
	}

	public String getIdentifier(int index)
	{
		E t = get(index);
		return t.getIdentifier();
	}

	public String getIdentifier(Class<? extends E> item)
	{
		return getIdentifier(item, 0);
	}

	public E get(Class<? extends E> loadable)
	{
		return get(loadable, 0);
	}

	public int getIndex(Class<? extends E> loadable, int pass)
	{
		return getIndex(getIdentifier(loadable, pass));
	}

	public int getIndex(Class<? extends E> loadable)
	{
		return getIndex(getIdentifier(loadable));
	}

	public int getIndex(E item)
	{
		return getIndex(item.getIdentifier());
	}

	public int getIndex(E item, int pass)
	{
		return getIndex(item.getIdentifier(pass));
	}

	public int getIndex(String identifier)
	{
		return identifierMap.get(identifier);
	}

	public E get(String identifier)
	{
		return get(getIndex(identifier));
	}

	public void loadClass(Class<? extends E> loadable, JarLoader<E> loader)
	{
		int loadnum = loader.getPassNum(loadable);
		for (int i = 0; i < loadnum; i++)
		{
			E t = loader.newInstance(i, loadable);
			String name = t.getIdentifier(i);
			add(t, name);
		}
	}
}
