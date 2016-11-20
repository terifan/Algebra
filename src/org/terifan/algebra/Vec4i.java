package org.terifan.algebra;


public class Vec4i 
{
	public int x;
	public int y;
	public int z;
	public int w;


	public Vec4i()
	{
	}


	public Vec4i(int aX, int aY, int aZ, int aW)
	{
		x = aX;
		y = aY;
		z = aZ;
		w = aW;
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
	}
}
