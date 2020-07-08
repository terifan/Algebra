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


	public Vec2i add(int v)
	{
		this.x += v;
		this.y += v;
		return this;
	}


	public Vec2i add(Vec2i aVec)
	{
		x += aVec.x;
		y += aVec.y;
		return this;
	}


	public Vec2i subtract(int x, int y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}


	public Vec2i subtract(int v)
	{
		this.x -= v;
		this.y -= v;
		return this;
	}


	public Vec2i subtract(Vec2i v)
	{
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}


	public Vec2i scale(int x, int y)
	{
		this.x *= x;
		this.y *= y;
		return this;
	}


	public Vec2i scale(int v)
	{
		this.x *= v;
		this.y *= v;
		return this;
	}


	public Vec2i scale(Vec2i aVec)
	{
		x *= aVec.x;
		y *= aVec.y;
		return this;
	}


	public Vec2i divide(int v)
	{
		this.x /= v;
		this.y /= v;
		return this;
	}


	public Vec2i divide(int x, int y)
	{
		this.x /= x;
		this.y /= y;
		return this;
	}


	public Vec2i divide(Vec2i v)
	{
		this.x /= v.x;
		this.y /= v.y;
		return this;
	}


	public Vec2i set(int x, int y)
	{
		this.x = x;
		this.y = y;
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


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		x = aBundle.getInt("x");
//		y = aBundle.getInt("y");
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		aBundle.putNumber("x", x);
//		aBundle.putNumber("y", y);
//	}
//
//
//	@Override
//	public void readExternal(Array aParts)
//	{
//		x = aParts.getInt(0);
//		y = aParts.getInt(1);
//	}
//
//
//	@Override
//	public Array writeExternal()
//	{
//		return Array.of(x, y);
//	}
}
