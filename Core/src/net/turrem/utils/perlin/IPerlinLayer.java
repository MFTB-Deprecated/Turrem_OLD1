package net.turrem.utils.perlin;

interface IPerlinLayer extends IPerlinGroup
{
	public float[] getChunk(float u, float v);
}
