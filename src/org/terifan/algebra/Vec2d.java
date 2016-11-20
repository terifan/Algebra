package org.terifan.algebra;


public class Vec2d 
{
	public double x;
	public double y;


	public Vec2d()
	{
	}


	public Vec2d(double aX, double aY)
	{
		x = aX;
		y = aY;
	}


	public Vec2d(double aXY)
	{
		x = aXY;
		y = aXY;
	}


	public Vec2d add(Vec2d aXY)
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
