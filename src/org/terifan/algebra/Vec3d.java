package org.terifan.algebra;

import java.io.Serializable;
import java.util.Locale;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;


/**
 * Vector class represents a point in space defined by x, y and z coordinates.
 */
public class Vec3d implements Cloneable, Serializable, Bundlable<Array>
{
	private static final long serialVersionUID = 1L;

	public final static Vec3d ZERO = new Vec3d()
	{
		@Override
		public boolean isZero()
		{
			return true;
		}
	};


	public double x;
	public double y;
	public double z;


	/**
	 * Constructs a new Vector with the coordinate (0,0,0).
	 */
	public Vec3d()
	{
	}


	/**
	 * Constructs a new Vector.
	 */
	public Vec3d(double aValue)
	{
		this(aValue,aValue,aValue);
	}


	/**
	 * Constructs a new Vector.
	 */
	public Vec3d(double x, double y, double z)
	{
		set(x, y, z);
	}


	/**
	 * Constructs a new Vector.
	 */
	public Vec3d(double[] xyz)
	{
		set(xyz[0], xyz[1], xyz[2]);
	}


	/**
	 * Constructs a new Vector.
	 */
	public Vec3d(Vec3d aVector)
	{
		set(aVector);
	}


	/**
	 * Constructs a new Vector.
	 */
	public Vec3d(Vec3f aVector)
	{
		set(aVector);
	}


	/**
	 * Sets the coordinates of this Vector.
	 */
	public Vec3d set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector.
	 */
	public Vec3d set(Vec3d aVector)
	{
		x = aVector.x;
		y = aVector.y;
		z = aVector.z;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector.
	 */
	public Vec3d set(double aValue)
	{
		x = aValue;
		y = aValue;
		z = aValue;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector.
	 */
	public Vec3d set(Vec3f aVector)
	{
		x = aVector.x;
		y = aVector.y;
		z = aVector.z;

		return this;
	}


	/**
	 * Sets each of the x, y, z coordinates to their absolute values.
	 */
	public Vec3d abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);

		return this;
	}


	/**
	 * Returns true if  (x == 0 && y == 0 && z == 0).
	 */
	public boolean isZero()
	{
		return x==0 && y == 0 && z == 0;
	}


	/**
	 * Returns true if  (x == 1 && y == 1 && z == 1).
	 */
	public boolean isOne()
	{
		return x==1 && y == 1 && z == 1;
	}


	public Vec3d add(Vec3d aVector)
	{
		x += aVector.x;
		y += aVector.y;
		z += aVector.z;

		return this;
	}


	public Vec3d add(double x, double y, double z)
	{
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}


	public Vec3d add(double aValue)
	{
		this.x += aValue;
		this.y += aValue;
		this.z += aValue;

		return this;
	}


	public Vec3d subtract(Vec3d aVector)
	{
		x -= aVector.x;
		y -= aVector.y;
		z -= aVector.z;

		return this;
	}


	public Vec3d subtract(double x, double y, double z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}


	public Vec3d subtract(double aValue)
	{
		this.x -= aValue;
		this.y -= aValue;
		this.z -= aValue;

		return this;
	}


	public Vec3d multiply(double aFactor)
	{
		x *= aFactor;
		y *= aFactor;
		z *= aFactor;

		return this;
	}


	public Vec3d multiply(double aFactorX, double aFactorY, double aFactorZ)
	{
		x *= aFactorX;
		y *= aFactorY;
		z *= aFactorZ;

		return this;
	}


	public Vec3d multiply(Vec3d aFactor)
	{
		x *= aFactor.x;
		y *= aFactor.y;
		z *= aFactor.z;

		return this;
	}


	public Vec3d divide(double aFactor)
	{
		double scale = 1 / aFactor;

		x *= scale;
		y *= scale;
		z *= scale;

		return this;
	}


	public Vec3d divide(double x, double y, double z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}


	public Vec3d divide(Vec3d aFactor)
	{
		x /= aFactor.x;
		y /= aFactor.y;
		z /= aFactor.z;

		return this;
	}


	public Vec3d normalize()
	{
		double length = Math.sqrt(x * x + y * y + z * z);

		if (length > 0)
		{
			double s = 1.0 / length;

			x *= s;
			y *= s;
			z *= s;

			if (x == -0) x = 0;
			if (y == -0) y = 0;
			if (z == -0) z = 0;
		}

		return this;
	}


	public Vec3d fixIllegalValues()
	{
		if (x == -0 || Double.isNaN(x) || Double.isInfinite(x) || Double.isFinite(x)) x = 0;
		if (y == -0 || Double.isNaN(y) || Double.isInfinite(y) || Double.isFinite(y)) y = 0;
		if (z == -0 || Double.isNaN(z) || Double.isInfinite(z) || Double.isFinite(z)) z = 0;
		return this;
	}


	public double dot(double aValue)
	{
		return x * aValue + y * aValue + z * aValue;
	}


	public double dot(double x, double y, double z)
	{
		return this.x * x + this.y * y + this.z * z;
	}


	public double dot(Vec3d aVector)
	{
		return x * aVector.x + y * aVector.y + z * aVector.z;
	}


	public Vec3d cross(double x, double y, double z)
	{
		double tempX = this.y * z - this.z * y;
		double tempY = this.z * x - this.x * z;
		double tempZ = this.x * y - this.y * x;

		this.x = tempX;
        this.y = tempY;
        this.z = tempZ;

		return this;
	}


	public Vec3d cross(Vec3d aVector)
	{
		double tempX = y * aVector.z - z * aVector.y;
		double tempY = z * aVector.x - x * aVector.z;
		double tempZ = x * aVector.y - y * aVector.x;

		x = tempX;
        y = tempY;
        z = tempZ;

		return this;
	}


	/**
	 * Updates this instance with the interpolated value of the provided vectors.
	 *
	 * @return
	 *   this instance
	 */
	public Vec3d interpolate(Vec3d aVectorFrom, Vec3d aVectorTo, double aAlpha)
	{
		if (aAlpha <= 0)
		{
			x = aVectorFrom.x;
			y = aVectorFrom.y;
			z = aVectorFrom.z;
		}
		else if (aAlpha >= 1)
		{
			x = aVectorTo.x;
			y = aVectorTo.y;
			z = aVectorTo.z;
		}
		else
		{
			x = aVectorFrom.x + aAlpha * (aVectorTo.x - aVectorFrom.x);
			y = aVectorFrom.y + aAlpha * (aVectorTo.y - aVectorFrom.y);
			z = aVectorFrom.z + aAlpha * (aVectorTo.z - aVectorFrom.z);
		}

		return this;
	}


	/**
	 * Updates this instance with the interpolated value of the provided vectors.
	 *
	 * @return
	 *   this instance
	 */
	public Vec3d interpolate(Vec3d aVector, double aAlpha)
	{
		if (aAlpha >= 1)
		{
			x = aVector.x;
			y = aVector.y;
			z = aVector.z;
		}
		else if (aAlpha > 0)
		{
			x += aAlpha * (aVector.x - x);
			y += aAlpha * (aVector.y - y);
			z += aAlpha * (aVector.z - z);
		}

		return this;
	}


	public double distance(Vec3d aVector)
	{
		double dx = aVector.x - x;
		double dy = aVector.y - y;
		double dz = aVector.z - z;

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}


	public double distanceSqr(Vec3d aVector)
	{
		double dx = aVector.x - x;
		double dy = aVector.y - y;
		double dz = aVector.z - z;

		return dx * dx + dy * dy + dz * dz;
	}


	/**
	 * Returns the length of this Vector.<p>
	 *
	 *   return Math.sqrt(x * x + y * y + z * z);
	 */
	public double length()
	{
		return Math.sqrt(x * x + y * y + z * z);
	}


	/**
	 * Returns the length of this Vector.<p>
	 *
	 *   return x * x + y * y + z * z;
	 */
	public double lenSqr()
	{
		return x * x + y * y + z * z;
	}


	/**
	 * Returns the greatest component of this Vector.
	 */
	public double maxComponent()
	{
		if (x > y && x > z)
		{
			return x;
		}
		else if (y > z)
		{
			return y;
		}
		return z;
	}


	/**
	 * Returns the smallest component of this Vector.
	 */
	public double minComponent()
	{
		if (x < y && x < z)
		{
			return x;
		}
		else if (y < z)
		{
			return y;
		}
		return z;
	}


	public Vec3d minComponentVector()
	{
		double s = minComponent();
		return new Vec3d(s,s,s);
	}


	public Vec3d maxComponentVector()
	{
		double s = maxComponent();
		return new Vec3d(s,s,s);
	}


	/**
	 * Returns x for index 0, y for index 1 and z for index 2.
	 */
	public double getComponent(int aIndex)
	{
		switch (aIndex)
		{
			case 0: return x;
			case 1: return y;
			case 2: return z;
			default: throw new IllegalArgumentException("aIndex out of bounds: " + aIndex);
		}
	}


	/**
	 * Clamps the x, y and z component to a value: 0 >= x/y/z <= 1
	 */
	public Vec3d clamp()
	{
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (z < 0) z = 0;
		if (x > 1) x = 1;
		if (y > 1) y = 1;
		if (z > 1) z = 1;

//		if (x < -1 || x > 1) x -= (long)x;
//		if (y < -1 || y > 1) y -= (long)y;
//		if (z < -1 || z > 1) z -= (long)z;
//		if (x < 0) x += 1;
//		if (y < 0) y += 1;
//		if (z < 0) z += 1;

		return this;
	}


	public Vec3d setLength(double aLength)
	{
		double length = aLength / length();
		x *= length;
		y *= length;
		z *= length;

		return this;
	}


	/**
	 * Returns true if the Vector provided has the exact same coordinate as
	 * this object.
	 */
	@Override
	public boolean equals(Object aVector)
	{
		if (aVector instanceof Vec3d)
		{
			Vec3d v = (Vec3d)aVector;
			return Double.doubleToLongBits(v.x) == Double.doubleToLongBits(x)
				&& Double.doubleToLongBits(v.y) == Double.doubleToLongBits(y)
				&& Double.doubleToLongBits(v.z) == Double.doubleToLongBits(z);
		}
		return false;
	}


	@Override
	public int hashCode()
	{
		long a = Double.doubleToLongBits(x);
		long b = Double.doubleToLongBits(y);
		long c = Double.doubleToLongBits(z);

		int ai = (int)(a ^ (a >>> 32));
		int bi = (int)(b ^ (b >>> 32));
		int ci = (int)(c ^ (c >>> 32));

		return ai ^ Integer.rotateRight(bi, 11) ^ Integer.rotateLeft(ci, 11);
	}


	/**
	 * Constructs a clone of this Vector.
	 */
	@Override
	public Vec3d clone()
	{
		assert !Double.isNaN(x);
		assert !Double.isNaN(y);
		assert !Double.isNaN(z);
		return new Vec3d(x,y,z);
	}


	/**
	 * Returns a description of this object.
	 */
	@Override
	public String toString()
	{
		return String.format("{x=%8.4f, y=%8.4f, z=%8.4f}", x, y, z);
	}


	public String toPrettyString()
	{
		return String.format(new Locale("en", "US"), "{x=%8.3f, y=%8.3f, z=%8.3f}", x, y, z); // english locale to make sure decimal sign is a point and not a comma
	}


	public boolean isValid()
	{
		return !Double.isNaN(x) && !Double.isInfinite(x) && !Double.isNaN(y) && !Double.isInfinite(y) && !Double.isNaN(z) && !Double.isInfinite(z);
	}


	/**
	 * Swap components depending on type parameter: 0 do nothing, 1 swap X/Y, 2 swap X/Z, 3 swap Y/Z.
	 */
	public Vec3d swapComponents(int aType)
	{
		switch (aType)
		{
			case 0:
				return this;
			case 1:
				return set(y, x, z);
			case 2:
				return set(z, y, x);
			case 3:
				return set(x, z, y);
		}

		throw new IllegalArgumentException();
	}


	/**
	 * Removes the integer part of each component and increments with one if
	 * the fractional part is negative ensuring that the each component will
	 * be in range 0 to 1 inclusive.
	 */
	public Vec3d wrap()
	{
		if (x < 0)
		{
			x -= (int)x - 1;
		}
		else if (x >= 1)
		{
			x -= (int)x;
		}

		if (y < 0)
		{
			y -= (int)y - 1;
		}
		else if (y >= 1)
		{
			y -= (int)y;
		}

		if (z < 0)
		{
			z -= (int)z - 1;
		}
		else if (z >= 1)
		{
			z -= (int)z;
		}

		return this;
	}


	/**
	 * Return XYZ as 8 bit RGB values packed into an int. Components are clamped into range 0-255 with X component shifted 16 bits, Y component shifted 8 bits.
	 */
	public int toRGB(double aScale)
	{
		int r = Math.max(Math.min((int)(x * aScale + 0.5), 255), 0) << 16;
		int g = Math.max(Math.min((int)(y * aScale + 0.5), 255), 0) << 8;
		int b = Math.max(Math.min((int)(z * aScale + 0.5), 255), 0);

		return r + g + b;
	}


	public Vec3d swap(Vec3d aVector)
	{
		double t;
		t = this.x; this.x = aVector.x; aVector.x = t;
		t = this.y; this.y = aVector.y; aVector.y = t;
		t = this.z; this.z = aVector.z; aVector.z = t;
		return this;
	}


	public Vec3d setComponent(int aComponent, double aValue)
	{
		switch (aComponent)
		{
			case 0: x = aValue; break;
			case 1: y = aValue; break;
			case 2: z = aValue; break;
			default: throw new IllegalArgumentException();
		}
		return this;
	}


	public Vec3d min(Vec3d aVector)
	{
		if (aVector.x < x) x = aVector.x;
		if (aVector.y < y) y = aVector.y;
		if (aVector.z < z) z = aVector.z;
		return this;
	}


	public Vec3d max(Vec3d aVector)
	{
		if (aVector.x > x) x = aVector.x;
		if (aVector.y > y) y = aVector.y;
		if (aVector.z > z) z = aVector.z;
		return this;
	}


	public Vec3d limit()
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
		return this;
	}


	public double get(int i)
	{
		switch (i)
		{
			case 0: return x;
			case 1: return y;
			case 2: return z;
		}
		throw new IllegalArgumentException(""+i);
	}


	public void set(int i, double o)
	{
		switch (i)
		{
			case 0: x = o; return;
			case 1: y = o; return;
			case 2: z = o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	public void scale(int i, double o)
	{
		switch (i)
		{
			case 0: x *= o; return;
			case 1: y *= o; return;
			case 2: z *= o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	public void add(int i, double o)
	{
		switch (i)
		{
			case 0: x += o; return;
			case 1: y += o; return;
			case 2: z += o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	public double luminance()
	{
		return 0.212671 * x + 0.715160 * y + 0.072169 * z;
	}


	public double[] toArray()
	{
		return new double[]{x,y,z};
	}


	/**
	 * Remove all but five decimals.
	 */
	public Vec3d truncate()
	{
		double s = 10000;

		x = (int)(x * s) / s;
		y = (int)(y * s) / s;
		z = (int)(z * s) / s;

		return this;
	}


	public static Vec3d fromRGB(int aRGB)
	{
		return new Vec3d((0xFF & (aRGB >> 16)) / 255.0, (0xFF & (aRGB >> 8)) / 255.0, (0xFF & aRGB) / 255.0);
	}


	public int toRGB()
	{
		return (Math.max(0, Math.min(255, (int)(255 * x))) << 16) + (Math.max(0, Math.min(255, (int)(255 * y))) << 8) + Math.max(0, Math.min(255, (int)(255 * z)));
	}


	public Vec2d toVec2d()
	{
		return new Vec2d(x, y);
	}


	public Vec2f toVec2f()
	{
		return new Vec2f((float)x, (float)y);
	}


	public Vec3f toVec3f()
	{
		return new Vec3f((float)x, (float)y, (float)z);
	}


	public Vec4d toVec4d()
	{
		return new Vec4d(x, y, z, 0);
	}


	@Override
	public void readExternal(Array aArray)
	{
		x = aArray.getDouble(0);
		y = aArray.getDouble(1);
		z = aArray.getDouble(2);
	}


	@Override
	public void writeExternal(Array aArray)
	{
		aArray.put(0, x);
		aArray.put(1, y);
		aArray.put(2, z);
	}


	/**
	 * Multiplies aVector with aFactor and adds result to this instance.
	 *
	 * @param aVector
	 * @param aFactor
	 * @return
	 */
	public Vec3d addmod(Vec3d aVector, double aFactor)
	{
		x += aVector.x * aFactor;
		y += aVector.y * aFactor;
		z += aVector.z * aFactor;

		return this;
	}


	public static Vec3d rotateAboutAxisRads(Vec3d source, double angleRads, Vec3d rotationAxis)
	{
		Mat3d rotationMatrix = new Mat3d();

		double sinTheta         = Math.sin(angleRads);
		double cosTheta         = Math.cos(angleRads);
		double oneMinusCosTheta = 1.0 - cosTheta;

		// It's quicker to pre-calc these and reuse than calculate x * y, then y * x later (same thing).
		double xyOne = rotationAxis.x * rotationAxis.y * oneMinusCosTheta;
		double xzOne = rotationAxis.x * rotationAxis.z * oneMinusCosTheta;
		double yzOne = rotationAxis.y * rotationAxis.z * oneMinusCosTheta;

		// Calculate rotated x-axis
		rotationMatrix.m00 = rotationAxis.x * rotationAxis.x * oneMinusCosTheta + cosTheta;
		rotationMatrix.m01 = xyOne + rotationAxis.z * sinTheta;
		rotationMatrix.m02 = xzOne - rotationAxis.y * sinTheta;

		// Calculate rotated y-axis
		rotationMatrix.m10 = xyOne - rotationAxis.z * sinTheta;
		rotationMatrix.m11 = rotationAxis.y * rotationAxis.y * oneMinusCosTheta + cosTheta;
		rotationMatrix.m12 = yzOne + rotationAxis.x * sinTheta;

		// Calculate rotated z-axis
		rotationMatrix.m20 = xzOne + rotationAxis.y * sinTheta;
		rotationMatrix.m21 = yzOne - rotationAxis.x * sinTheta;
		rotationMatrix.m22 = rotationAxis.z * rotationAxis.z * oneMinusCosTheta + cosTheta;

		// Multiply the source by the rotation matrix we just created to perform the rotation
		return rotationMatrix.multiply(source);
	}


	boolean lengthIsApproximately(double value, double tolerance)
	{
		return Math.abs(this.length() - value) < tolerance;
	}


	public Vec3d multiply(Mat3d m)
	{
		return set(
			m.m00 * x + m.m10 * y + m.m20 * z,
			m.m01 * x + m.m11 * y + m.m21 * z,
			m.m02 * x + m.m12 * y + m.m22 * z
		);
	}


	public Vec3d multiply(Mat4d m)
	{
		return set(
			m.m00 * x + m.m10 * y + m.m20 * z,
			m.m01 * x + m.m11 * y + m.m21 * z,
			m.m02 * x + m.m12 * y + m.m22 * z
		);
	}
}
