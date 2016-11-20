package org.terifan.algebra;


public class Vec2i 
{
	public int x;
	public int y;


	public Vec2i()
	{
	}


	public Vec2i(int aX, int aY)
	{
		x = aX;
		y = aY;
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + '}';
	}
}
