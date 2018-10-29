package org.terifan.algebra;

import java.io.Serializable;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableValue;
import org.terifan.bundle.Bundle;
import org.terifan.bundle.Array;


public class Vec4i implements Serializable, Bundlable, BundlableValue<Array>
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


	public Vec4i(int[] aValues)
	{
		x = aValues[0];
		y = aValues[1];
		z = aValues[2];
		w = aValues[3];
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


	@Override
	public void readExternal(Bundle aBundle)
	{
		x = aBundle.getInt("x");
		y = aBundle.getInt("y");
		z = aBundle.getInt("z");
		w = aBundle.getInt("w");
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		aBundle.putNumber("x", x);
		aBundle.putNumber("y", y);
		aBundle.putNumber("z", z);
		aBundle.putNumber("w", w);
	}


	@Override
	public void readExternal(Array aParts)
	{
		x = aParts.getInt(0);
		y = aParts.getInt(1);
		z = aParts.getInt(2);
		w = aParts.getInt(3);
	}


	@Override
	public Array writeExternal()
	{
		return new Array(x, y, z, w);
	}
}
