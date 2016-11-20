package org.terifan.algebra;


public class Vec2f 
{
	public double x;
	public double y;


	public Vec2f()
	{
	}


	public Vec2f(double aX, double aY)
	{
		x = aX;
		y = aY;
	}


	public Vec2f(double aXY)
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
