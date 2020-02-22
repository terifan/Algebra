package org.terifan.algebra;

import java.io.Serializable;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableValue;
import org.terifan.bundle.Bundle;



public class Vec2d implements Cloneable, Serializable, Bundlable, BundlableValue<Array>
{
	private static final long serialVersionUID = 1L;

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


	public Vec2d subtract(Vec2d v)
	{
		return new Vec2d(x - v.x, y - v.y);
	}


	public Vec2d subtract(double x, double y)
	{
		return new Vec2d(x - this.x, y - this.y);
	}


	public Vec2d multiply(Vec2d v)
	{
		return new Vec2d(x * v.x, y * v.y);
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


	public double distanceSqr(Vec2d v)
	{
		return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
	}


	public double distance(Vec2d v)
	{
		return Math.sqrt(distanceSqr(v));
	}


	public double dot(Vec2d v)
	{
		return x * v.x + y * v.y;
	}


	@Override
	public String toString()
	{
		return "Vec2d{" + "x=" + x + ", y=" + y + '}';
	}


	@Override
	public Vec2d clone()
	{
		return new Vec2d(x, y);
	}


	@Override
	public void readExternal(Bundle aBundle)
	{
		x = aBundle.getDouble("x");
		y = aBundle.getDouble("y");
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
		x = aParts.getDouble(0);
		y = aParts.getDouble(1);
	}


	@Override
	public Array writeExternal()
	{
		return Array.of(x, y);
	}


	public double distanceLineSegment(Vec2d v, Vec2d w)
	{
		double l2 = v.distanceSqr(w);

		if (l2 == 0.0)
		{
			return distance(v);
		}

		Vec2d vw = w.subtract(v);

		double t = (dot(vw) - v.dot(vw)) / l2;

		Vec2d z;

		if (t < 0.0)
		{
			z = v;
		}
		else if (t > 1.0)
		{
			z = w;
		}
		else
		{
			z = v.add(vw.scale(t));
		}

		return distance(z);
	}
}
