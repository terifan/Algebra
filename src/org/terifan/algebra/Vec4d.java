package org.terifan.algebra;


public class Vec4d
{
	public double x;
	public double y;
	public double z;
	public double w;


	public Vec4d()
	{
	}


	public Vec4d(double aX, double aY, double aZ, double aW)
	{
		x = aX;
		y = aY;
		z = aZ;
		w = aW;
	}


	public Vec4d set(double aX, double aY, double aZ, double aW)
	{
		x = aX;
		y = aY;
		z = aZ;
		w = aW;
		return this;
	}


	public double dot(double x, double y, double z, double w)
	{
		return this.x * x + this.y * y + this.z * z + this.w * w;
	}


	public double dot(Vec4d aVector)
	{
		return x * aVector.x + y * aVector.y + z * aVector.z + w * aVector.w;
	}


	public Vec4d limit()
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
		return this;
	}


	@Override
	public String toString()
	{
		return "{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
	}


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		x = aBundle.getDouble("x");
//		y = aBundle.getDouble("y");
//		z = aBundle.getDouble("z");
//		w = aBundle.getDouble("w");
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		aBundle.putNumber("x", x);
//		aBundle.putNumber("y", y);
//		aBundle.putNumber("z", z);
//		aBundle.putNumber("w", w);
//	}
//
//
//	@Override
//	public void readExternal(Array aParts)
//	{
//		x = aParts.getDouble(0);
//		y = aParts.getDouble(1);
//		z = aParts.getDouble(2);
//		w = aParts.getDouble(3);
//	}
//
//
//	@Override
//	public Array writeExternal()
//	{
//		return Array.of(x, y, z, w);
//	}
}
