package org.terifan.algebra;


public class Vec2d implements Cloneable
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


	public Vec2d add(double x, double y)
	{
		this.x += x;
		this.y += y;

		return this;
	}


	public Vec2d add(double aValue)
	{
		this.x += aValue;
		this.y += aValue;

		return this;
	}


	public Vec2d scale(double aScale)
	{
		x *= aScale;
		y *= aScale;

		return this;
	}


	public Vec2d scale(double aScaleX, double aScaleY)
	{
		x *= aScaleX;
		y *= aScaleY;

		return this;
	}


	public Vec2d scale(Vec2d aVector)
	{
		x *= aVector.x;
		y *= aVector.y;

		return this;
	}


	@Override
	public String toString()
	{
		return "Vec2f{" + "x=" + x + ", y=" + y + '}';
	}


	@Override
	public Vec2d clone()
	{
		return new Vec2d(x, y);
	}
}
