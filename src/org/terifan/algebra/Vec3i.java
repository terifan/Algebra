package org.terifan.algebra;


public class Vec3i
{
	public int x;
	public int y;
	public int z;


	public Vec3i()
	{
	}


	public Vec3i(int aX, int aY, int aZ)
	{
		this.x = aX;
		this.y = aY;
		this.z = aZ;
	}


	public Vec3i add(Vec3i aVector)
	{
		x += aVector.x;
		y += aVector.y;
		z += aVector.z;

		return this;
	}


	public Vec3i add(int x, int y, int z)
	{
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}


	public Vec3i add(int aValue)
	{
		this.x += aValue;
		this.y += aValue;
		this.z += aValue;

		return this;
	}


	public Vec3i subtract(Vec3i aVector)
	{
		x -= aVector.x;
		y -= aVector.y;
		z -= aVector.z;

		return this;
	}


	public Vec3i subtract(int x, int y, int z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}


	public Vec3i subtract(int aValue)
	{
		this.x -= aValue;
		this.y -= aValue;
		this.z -= aValue;

		return this;
	}


//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		x = aBundle.getInt("x");
//		y = aBundle.getInt("y");
//		z = aBundle.getInt("z");
//	}
//
//
//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		aBundle.putNumber("x", x);
//		aBundle.putNumber("y", y);
//		aBundle.putNumber("z", z);
//	}
//
//
//	@Override
//	public void readExternal(Array aParts)
//	{
//		x = aParts.getInt(0);
//		y = aParts.getInt(1);
//		z = aParts.getInt(2);
//	}
//
//
//	@Override
//	public Array writeExternal()
//	{
//		return Array.of(x, y, z);
//	}


	@Override
	public String toString()
	{
		return "Vec3i{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}
}
