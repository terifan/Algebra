package org.terifan.algebra;


public class Vec2f 
{
	public float x;
	public float y;


	public Vec2f()
	{
	}


	public Vec2f(float aX, float aY)
	{
		x = aX;
		y = aY;
	}


	public Vec2f(float aXY)
	{
		x = aXY;
		y = aXY;
	}


	public Vec2f add(Vec2f aXY)
	{
		x += aXY.x;
		y += aXY.y;
		return this;
	}


	@Override
	public String toString()
	{
		return "Vec2f{" + "x=" + x + ", y=" + y + '}';
	}
}
