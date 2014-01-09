package zap.turrem.utils.data;

import java.util.HashMap;

public class Histogram<T>
{
	protected HashMap<T, Integer> hist;
	private int total = 0;
	private int max = 0;

	public Histogram()
	{
		this.hist = new HashMap<T, Integer>();
	}

	public void add(T item)
	{
		int n = 0;
		if (this.hist.containsKey(item))
		{
			n = this.hist.get(item);
		}
		n++;
		if (n > this.max)
		{
			this.max = n;
		}
		this.hist.put(item, n);
		this.total++;
	}

	public int get(T item)
	{
		if (this.hist.containsKey(item))
		{
			return this.hist.get(item);
		}
		return 0;
	}

	public int getTotal()
	{
		return this.total;
	}

	public int getMax()
	{
		return this.max;
	}

	public float getf(T item)
	{
		return this.get(item) / (float) this.getMax();
	}
}
