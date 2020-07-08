package org.terifan.algebra;


public class Vec3i
{
	public int x;
	public int y;
	public int z;


	public Vec3i(int aX, int aY, int aZ)
	{
		this.x = aX;
		this.y = aY;
		this.z = aZ;
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
}
