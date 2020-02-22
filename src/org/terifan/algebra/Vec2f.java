package org.terifan.algebra;

import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableValue;
import org.terifan.bundle.Bundle;


public class Vec2f implements Bundlable, BundlableValue<Array>
{
	public float x;
	public float y;


	public Vec2f()
	{
	}


	public Vec2f(float aX, float aY)
	{
		x = aX;
		y = aY;
	}


	public Vec2f(float aXY)
	{
		x = aXY;
		y = aXY;
	}


	public Vec2f add(Vec2f aXY)
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


	@Override
	public void readExternal(Bundle aBundle)
	{
		x = aBundle.getFloat("x");
		y = aBundle.getFloat("y");
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		aBundle.putNumber("x", x);
		aBundle.putNumber("y", y);
	}


	@Override
	public void readExternal(Array aParts)
	{
		x = aParts.getFloat(0);
		y = aParts.getFloat(1);
	}


	@Override
	public Array writeExternal()
	{
		return Array.of(x, y);
	}
}
