package org.terifan.algebra;

import java.io.Serializable;


public class Vec4i implements Serializable
{
	private static final long serialVersionUID = 1L;

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


	public Vec4i add(int x, int y, int z, int w)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}


	public Vec4i add(Vec4i aVec)
	{
		x += aVec.x;
		y += aVec.y;
		z += aVec.z;
		w += aVec.w;
		return this;
	}


	public Vec4i scale(int s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		return this;
	}


	public Vec4i scale(int x, int y, int z, int w)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;
		return this;
	}


	public Vec4i scale(Vec2i aVec)
	{
		x *= aVec.x;
		y *= aVec.y;
		return this;
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
	}
}
