package org.terifan.algebra;


public class Vec2i implements Cloneable
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


	public Vec2i add(int x, int y)
	{
		this.x += x;
		this.y += y;
		return this;
	}


	public Vec2i add(Vec2i aVec)
	{
		x += aVec.x;
		y += aVec.y;
		return this;
	}


	public Vec2i scale(int x, int y)
	{
		this.x *= x;
		this.y *= y;
		return this;
	}


	public Vec2i scale(Vec2i aVec)
	{
		x *= aVec.x;
		y *= aVec.y;
		return this;
	}


	@Override
	public Vec2i clone()
	{
		return new Vec2i(x, y);
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + '}';
	}


	public Vec2i set(int x, int y)
	{
		this.x = x;
		this.y = y;
		return this;
	}


	public Vec2i scale(int s)
	{
		this.x *= s;
		this.y *= s;
		return this;
	}
}
