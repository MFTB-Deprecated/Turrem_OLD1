package zap.turrem.utils.data;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class IntHistogram extends Histogram<Integer>
{
	public float getAverage()
	{
		return this.getSum() / (float) this.getTotal();
	}

	public long getSum()
	{
		long sum = 0;

		Set<Entry<Integer, Integer>> set = this.hist.entrySet();

		Iterator<Entry<Integer, Integer>> it = set.iterator();

		while (it.hasNext())
		{
			Entry<Integer, Integer> ent = it.next();

			sum += ent.getKey() * ent.getValue();
		}

		return sum;
	}

	public int getMinKey()
	{
		Iterator<Integer> keys = this.hist.keySet().iterator();

		int min = 0;
		boolean first = true;

		while (keys.hasNext())
		{
			int k = keys.next();
			if (k < min || first)
			{
				min = k;
			}
			first = false;
		}

		return min;
	}

	public int getMaxKey()
	{
		Iterator<Integer> keys = this.hist.keySet().iterator();

		int max = 0;
		boolean first = true;

		while (keys.hasNext())
		{
			int k = keys.next();
			if (k > max || first)
			{
				max = k;
			}
			first = false;
		}

		return max;
	}

	public float[] getValues(int min, int max)
	{
		float[] vals = new float[max - min + 1];

		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = this.getf(min + i);
		}

		return vals;
	}

	public String[] getGraphicOutput(int h)
	{
		String[] out = new String[h + 1];

		for (int i = 0; i < out.length; i++)
		{
			out[i] = "";
		}
		
		int min = this.getMinKey();
		int max = this.getMaxKey();

		int dig = (int) Math.log10(max) + 1;
		int fdig = dig + 1;

		float[] vals = this.getValues(min, max);

		for (int i = 0; i < vals.length; i++)
		{
			String numb = "" + (i + min);

			while (numb.length() < fdig)
			{
				numb += " ";
			}

			out[h] += numb;

			for (int j = 0; j < fdig; j++)
			{
				int val = (int) (vals[i] * h + 0.2F);
				for (int k = 0; k < h; k++)
				{
					if (j == dig)
					{
						out[k] += " ";
					}
					else
					{
						int s = h - k;
						if (s <= val)
						{
							out[k] += "=";
						}
						else
						{
							out[k] += " ";
						}
					}
				}
			}
		}

		return out;
	}
}
