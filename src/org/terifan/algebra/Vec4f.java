package org.terifan.algebra;

import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableValue;
import org.terifan.bundle.Bundle;


public class Vec4f implements Bundlable, BundlableValue<Array>
{
	public float x;
	public float y;
	public float z;
	public float w;


	public Vec4f()
	{
	}


	public Vec4f(float aX, float aY, float aZ, float aW)
	{
		x = aX;
		y = aY;
		z = aZ;
		w = aW;
	}


	public void limit()
	{
		if (x > 1 || x < -1)
		{
			x -= (long) x;
		}
		if (y > 1 || y < -1)
		{
			y -= (long) y;
		}
		if (z > 1 || z < -1)
		{
			z -= (long) z;
		}
		if (w > 1 || w < -1)
		{
			w -= (long) w;
		}
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
	}


	@Override
	public void readExternal(Bundle aBundle)
	{
		x = aBundle.getFloat("x");
		y = aBundle.getFloat("y");
		z = aBundle.getFloat("z");
		w = aBundle.getFloat("w");
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
		x = aParts.getFloat(0);
		y = aParts.getFloat(1);
		z = aParts.getFloat(2);
		w = aParts.getFloat(3);
	}


	@Override
	public Array writeExternal()
	{
		return new Array(x, y, z, w);
	}
}
