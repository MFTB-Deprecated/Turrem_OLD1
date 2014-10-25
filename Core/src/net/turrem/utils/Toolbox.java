package net.turrem.utils;

import java.util.ArrayList;
import java.util.List;

public class Toolbox
{
	public static String[] mergeStringArrays(String[] ar1, String[] ar2)
	{
		String[] ar = new String[ar1.length + ar2.length];
		int j = 0;
		for (int i = 0; i < ar1.length; i++)
		{
			ar[j] = ar1[i];
			j++;
		}
		for (int i = 0; i < ar2.length; i++)
		{
			ar[j] = ar2[i];
			j++;
		}
		return ar;
	}
	
	public static String[] wrapString(String string, int chars)
	{
		String[] words = string.split(" ");
		List<String> lines = new ArrayList<String>();
		String line = "";
		
		for (int i = 0; i < words.length; i++)
		{
			String word = words[i];
			
			if (line.length() + word.length() + 1 > chars && !line.isEmpty())
			{
				lines.add(line);
				line = "";
			}
			
			if (!line.isEmpty())
			{
				line += " ";
			}
			
			line += word;
		}
		
		lines.add(line);
		
		return lines.toArray(new String[0]);
	}
	
	public static String getFloat(float f, int d)
	{
		String num = "";
		if (f < 0)
		{
			num += "-";
			f *= -1;
		}
		num += (int) f;
		if (d > 0)
		{
			num += ".";
		}
		float frac = f - (int) f;
		float dec = 1.0F;
		for (int i = 0; i < d; i++)
		{
			dec *= 10.0F;
			int deg = ((int) (frac * dec)) % 10;
			num += deg;
		}
		return num;
	}
}
