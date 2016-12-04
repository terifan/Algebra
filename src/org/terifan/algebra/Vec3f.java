package org.terifan.algebra;


/**
 * Vector class represents a point in space defined by x, y and z coordinates.
 */
public class Vec3f
{
	public final static Vec3f ZERO = new Vec3f()
	{
		@Override
		public boolean isZero()
		{
			return true;
		}
	};

	public float x;
	public float y;
	public float z;


	/**
	 * Constructs a new Vector3f with the coordinate (0,0,0).
	 */
	public Vec3f()
	{
	}


	/**
	 * Constructs a new Vector3f.
	 */
	public Vec3f(float o)
	{
		set(o, o, o);
	}


	/**
	 * Constructs a new Vector3f.
	 */
	public Vec3f(float x, float y, float z)
	{
		set(x, y, z);
	}


	/**
	 * Constructs a new Vector3f.
	 */
	public Vec3f(double x, double y, double z)
	{
		set(x, y, z);
	}


	/**
	 * Constructs a new Vector3f.
	 */
	public Vec3f(Vec3d aVector)
	{
		set(aVector);
	}


	/**
	 * Sets the coordinates of this Vector3f to (x,y,z).
	 */
	public Vec3f set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector3f to (x,y,z).
	 */
	public Vec3f set(double x, double y, double z)
	{
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector3f to (aVector.x, aVector.y, aVector.z).
	 */
	public Vec3f set(Vec3d aVector)
	{
		x = (float)aVector.x;
		y = (float)aVector.y;
		z = (float)aVector.z;

		return this;
	}


	/**
	 * Sets the coordinates of this Vector3f to (aVector.x, aVector.y, aVector.z).
	 */
	public Vec3f set(Vec3f aVector)
	{
		x = aVector.x;
		y = aVector.y;
		z = aVector.z;

		return this;
	}


	/**
	 * Sets each of the x, y, z coordinates to their absolute values.
	 */
	public Vec3f abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);

		return this;
	}


	/**
	 * Returns true if (x == 0 && y == 0 && z == 0).
	 */
	public boolean isZero()
	{
		return x == 0 && y == 0 && z == 0;
	}


	/**
	 * Returns true if (x == 1 && y == 1 && z == 1).
	 */
	public boolean isOne()
	{
		return x == 1 && y == 1 && z == 1;
	}


	public Vec3f add(Vec3f aVector)
	{
		x += aVector.x;
		y += aVector.y;
		z += aVector.z;

		return this;
	}


	public Vec3f add(Vec3d aVector)
	{
		x += aVector.x;
		y += aVector.y;
		z += aVector.z;

		return this;
	}


	public Vec3f addClone(Vec3f aVector)
	{
		return new Vec3f(x + aVector.x, y + aVector.y, z + aVector.z);
	}


	public Vec3f add(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}


	public Vec3f addClone(float x, float y, float z)
	{
		return new Vec3f(this.x + x, this.y + y, this.z + z);
	}


	public Vec3f add(float aValue)
	{
		this.x += aValue;
		this.y += aValue;
		this.z += aValue;

		return this;
	}


	public Vec3f addClone(float aValue)
	{
		return new Vec3f(x + aValue, y + aValue, z + aValue);
	}


	public Vec3f subtract(Vec3f aVector)
	{
		x -= aVector.x;
		y -= aVector.y;
		z -= aVector.z;

		return this;
	}


	public Vec3f subtractClone(Vec3f aVector)
	{
		return new Vec3f(x - aVector.x, y - aVector.y, z - aVector.z);
	}


	public Vec3f subtract(float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}


	public Vec3f subtractClone(float x, float y, float z)
	{
		return new Vec3f(this.x - x, this.y - y, this.z - z);
	}


	public Vec3f subtract(float aValue)
	{
		this.x -= aValue;
		this.y -= aValue;
		this.z -= aValue;

		return this;
	}


	public Vec3f subtractClone(float aValue)
	{
		return new Vec3f(x - aValue, y - aValue, z - aValue);
	}


	public Vec3f scale(float aScale)
	{
		if (aScale != 1)
		{
			x *= aScale;
			y *= aScale;
			z *= aScale;
		}

		return this;
	}


	public Vec3f scale(double aScale)
	{
		if (aScale != 1)
		{
			x *= aScale;
			y *= aScale;
			z *= aScale;
		}

		return this;
	}


	public Vec3f scaleClone(float aScale)
	{
		return new Vec3f(x * aScale, y * aScale, z * aScale);
	}


	public Vec3f scale(float aScaleX, float aScaleY, float aScaleZ)
	{
		if (aScaleX != 1)
		{
			x *= aScaleX;
		}
		if (aScaleY != 1)
		{
			y *= aScaleY;
		}
		if (aScaleZ != 1)
		{
			z *= aScaleZ;
		}

		return this;
	}


	public Vec3f scaleClone(float aScaleX, float aScaleY, float aScaleZ)
	{
		return new Vec3f(x * aScaleX, y * aScaleY, z * aScaleZ);
	}


	public Vec3f scale(Vec3f aVector)
	{
		if (aVector.x != 1)
		{
			x *= aVector.x;
		}
		if (aVector.y != 1)
		{
			y *= aVector.y;
		}
		if (aVector.z != 1)
		{
			z *= aVector.z;
		}

		return this;
	}


	public Vec3f scale(Vec3d aVector)
	{
		if (aVector.x != 1)
		{
			x *= aVector.x;
		}
		if (aVector.y != 1)
		{
			y *= aVector.y;
		}
		if (aVector.z != 1)
		{
			z *= aVector.z;
		}

		return this;
	}


	public Vec3f scaleClone(Vec3f aVector)
	{
		return new Vec3f(x * aVector.x, y * aVector.y, z * aVector.z);
	}


	public Vec3f divide(float aFactor)
	{
		float scale = 1 / aFactor;

		x *= scale;
		y *= scale;
		z *= scale;

		return this;
	}


	public Vec3f divideClone(float aFactor)
	{
		float scale = 1f / aFactor;

		return new Vec3f(x * scale, y * scale, z * scale);
	}


	public Vec3f divide(float x, float y, float z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}


	public Vec3f divideClone(float x, float y, float z)
	{
		return new Vec3f(this.x / x, this.y / y, this.z / z);
	}


	public Vec3f divide(Vec3f aFactor)
	{
		x /= aFactor.x;
		y /= aFactor.y;
		z /= aFactor.z;

		return this;
	}


	public Vec3f divideClone(Vec3f aFactor)
	{
		return new Vec3f(x / aFactor.x, y * aFactor.y, z * aFactor.z);
	}


	public Vec3f normalize()
	{
		float length = (float)Math.sqrt(x * x + y * y + z * z);

		if (length == 0)
		{
			length = 1;
		}

		float s = 1 / length;

		x *= s;
		y *= s;
		z *= s;

		if (x == -0)
		{
			x = 0;
		}
		if (y == -0)
		{
			y = 0;
		}
		if (z == -0)
		{
			z = 0;
		}

		return this;
	}


	public Vec3f normalizeClone()
	{
		return cloneVector().normalize();
	}


	public float dot(float aValue)
	{
		return x * aValue + y * aValue + z * aValue;
	}


	public float dot(float x, float y, float z)
	{
		return this.x * x + this.y * y + this.z * z;
	}


	public float dot(Vec3f aVector)
	{
		return x * aVector.x + y * aVector.y + z * aVector.z;
	}


	public Vec3f cross(float x, float y, float z)
	{
		float tempX = this.y * z - this.z * y;
		float tempY = this.z * x - this.x * z;
		float tempZ = this.x * y - this.y * x;

		this.x = tempX;
		this.y = tempY;
		this.z = tempZ;

		return this;
	}


	public Vec3f cross(Vec3f aVector)
	{
		float tempX = y * aVector.z - z * aVector.y;
		float tempY = z * aVector.x - x * aVector.z;
		float tempZ = x * aVector.y - y * aVector.x;

		x = tempX;
		y = tempY;
		z = tempZ;

		return this;
	}


	public Vec3f crossClone(Vec3f aVector)
	{
		return cloneVector().cross(aVector);
	}


	public Vec3f interpolate(Vec3f aVectorA, Vec3f aVectorB, float aAlpha)
	{
		if (aAlpha == 0)
		{
			x = aVectorA.x;
			y = aVectorA.y;
			z = aVectorA.z;
		}
		else if (aAlpha == 1)
		{
			x = aVectorB.x;
			y = aVectorB.y;
			z = aVectorB.z;
		}
		else
		{
			x = aVectorA.x + aAlpha * (aVectorB.x - aVectorA.x);
			y = aVectorA.y + aAlpha * (aVectorB.y - aVectorA.y);
			z = aVectorA.z + aAlpha * (aVectorB.z - aVectorA.z);
		}

		return this;
	}


	public Vec3f interpolateClone(Vec3f aVectorA, Vec3f aVectorB, float aAlpha)
	{
		return cloneVector().interpolateClone(aVectorA, aVectorB, aAlpha);
	}


	public float distance(Vec3f aVector)
	{
		float dx = aVector.x - x;
		float dy = aVector.y - y;
		float dz = aVector.z - z;

		return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
	}


	/**
	 * Returns the length of this Vector3f.<p>
	 *
	 * return Math.sqrt(x * x + y * y + z * z);
	 */
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}


	/**
	 * Returns the length of this Vector3f.<p>
	 *
	 * return x * x + y * y + z * z;
	 */
	public float lengthSqr()
	{
		return x * x + y * y + z * z;
	}


	/**
	 * Returns the greatest component of this Vector3f.
	 */
	public float maxComponent()
	{
		if (x > y && x > z)
		{
			return x;
		}
		else if (y > z)
		{
			return y;
		}
		else
		{
			return z;
		}
	}


	/**
	 * Returns the smallest component of this Vector3f.
	 */
	public float minComponent()
	{
		if (x < y && x < z)
		{
			return x;
		}
		else if (y < z)
		{
			return y;
		}
		else
		{
			return z;
		}
	}


	/**
	 * Returns x for index 0, y for index 1 and z for index 2.
	 */
	public float getComponent(int aIndex)
	{
		switch (aIndex)
		{
			case 0:
				return x;
			case 1:
				return y;
			case 2:
				return z;
			default:
				throw new IllegalArgumentException("aIndex out of bounds: " + aIndex);
		}
	}


	/**
	 * Clamps the x, y and z component to a value: 0 >= x/y/z < 1
	 */
	public Vec3f clamp()
	{
		if (x <= -1 || x >= 1)
		{
			x -= (long)x;
		}
		if (y <= -1 || y >= 1)
		{
			y -= (long)y;
		}
		if (z <= -1 || z >= 1)
		{
			z -= (long)z;
		}
		if (x < 0)
		{
			x += 1;
		}
		if (y < 0)
		{
			y += 1;
		}
		if (z < 0)
		{
			z += 1;
		}

		return this;
	}


	public Vec3f clampClone()
	{
		return cloneVector().clamp();
	}


	public Vec3f setLength(float aLength)
	{
		float length = aLength / length();
		x *= length;
		y *= length;
		z *= length;

		return this;
	}


	/**
	 * Returns true if the Vector3f provided has the exact same coordinate as
	 * this object.
	 */
	@Override
	public boolean equals(Object aVector)
	{
		if (aVector instanceof Vec3f)
		{
			Vec3f v = (Vec3f)aVector;
			return v.x == x && v.y == y && v.z == z;
		}
		return false;
	}


	@Override
	public int hashCode()
	{
		int a = Float.floatToIntBits(x);
		int b = Float.floatToIntBits(y);
		int c = Float.floatToIntBits(z);

		return a ^ b ^ c;
	}


	/**
	 * Constructs a clone of this Vector3f.
	 */
	public Vec3f cloneVector()
	{
		return new Vec3f(x, y, z);
	}


	float get(int i)
	{
		switch (i)
		{
			case 0: return x;
			case 1: return y;
			case 2: return z;
		}
		throw new IllegalArgumentException(""+i);
	}


	void set(int i, float o)
	{
		switch (i)
		{
			case 0: x = o; return;
			case 1: y = o; return;
			case 2: z = o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	void scale(int i, float o)
	{
		switch (i)
		{
			case 0: x *= o; return;
			case 1: y *= o; return;
			case 2: z *= o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	public Vec2f getXY()
	{
		return new Vec2f(x, y);
	}


	public float max()
	{
		if (x > y && x > z)
		{
			return x;
		}
		if (y > z)
		{
			return y;
		}
		return z;
	}


	public void add(int i, float o)
	{
		switch (i)
		{
			case 0: x += o; return;
			case 1: y += o; return;
			case 2: z += o; return;
		}
		throw new IllegalArgumentException(""+i);
	}


	public float lenSqr()
	{
		return dot(this);
	}


	/**
	 * Constructs a clone of this Vector3f.
	 */
	@Override
	public Vec3f clone()
	{
		return new Vec3f(x, y, z);
	}


	/**
	 * Returns a description of this object.
	 */
	@Override
	public String toString()
	{
		return ("[x=" + x + ", y=" + y + ", z=" + z + "]").replace(".0,", ",").replace(".0]", "]");
	}


	/**
	 * Returns the length of this Vector.<p>
	 *
	 *   return x * x + y * y + z * z;
	 */
	public double length2()
	{
		return x * x + y * y + z * z;
	}
}
