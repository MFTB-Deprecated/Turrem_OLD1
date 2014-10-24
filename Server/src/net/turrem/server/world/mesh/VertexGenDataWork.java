package net.turrem.server.world.mesh;

import java.util.ArrayList;
import java.util.HashSet;

import net.turrem.server.world.morph.IGeomorph;

public final class VertexGenDataWork
{
	private ArrayList<Float> setVals = new ArrayList<Float>();
	private ArrayList<Float> setMults = new ArrayList<Float>();
	private ArrayList<Float> offVals = new ArrayList<Float>();
	private ArrayList<Float> offMults = new ArrayList<Float>();

	HashSet<IGeomorph> set = new HashSet<IGeomorph>();

	public VertexGenDataWork()
	{

	}

	public VertexGenDataWork(float lastHeight)
	{
		this.setVals.add(lastHeight);
		this.setMults.add(1.0F);
	}

	public void addMorph(IGeomorph morph)
	{
		this.set.add(morph);
	}

	public void setHeight(float height, float weight)
	{
		this.setVals.add(height);
		this.setMults.add(weight);
	}

	public void offsetHeight(float offset, float weight)
	{
		this.offVals.add(offset);
		this.offMults.add(weight);
	}

	float getHeight()
	{
		float height = 0.0F;
		float div = 0.0F;
		for (int i = 0; i < this.setVals.size(); i++)
		{
			height += this.setVals.get(i) * this.setMults.get(i);
			div += this.setMults.get(i);
		}
		if (div != 0)
		{
			height /= div;
		}
		div = 0.0F;
		float off = 0.0F;
		for (int i = 0; i < this.offVals.size(); i++)
		{
			off += this.offVals.get(i) * this.offMults.get(i);
			div += this.offMults.get(i);
		}
		if (div != 0)
		{
			height += off / div;
		}
		return height;
	}
}
