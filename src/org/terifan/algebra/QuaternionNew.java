package org.terifan.algebra;


/**
 * Quaternion implementation used to rotate points in 3D-space.
 */
public class QuaternionNew
{
	double w, x, y, z;


	public QuaternionNew()
	{
	}


	/**
	 * from Euler (roll, pitch, yaw)
	 */
	public QuaternionNew(Vec3d aDirection)
	{
		double tx = Math.PI * aDirection.x;
		double ty = Math.PI * aDirection.y;
		double tz = Math.PI * aDirection.z;

		double cx = Math.cos(tx);
		double sx = Math.sin(tx);
		double cy = Math.cos(ty);
		double sy = Math.sin(ty);
		double cz = Math.cos(tz);
		double sz = Math.sin(tz);

		x = cz * sx * cy - sz * cx * sy;
		y = cz * cx * sy + sz * sx * cy;
		z = sz * cx * cy + cz * sx * sy;
		w = cz * cx * cy - sz * sx * sy;
	}


	public QuaternionNew(double aW, double aX, double aY, double aZ)
	{
		w = aW;
		x = aX;
		y = aY;
		z = aZ;
	}


	/**
	 * Transforms a single Vector.
	 *
	 * @param aVector
	 * the vector to transform
	 * @return
	 * the provided vector
	 */
	public Vec3d transform(Vec3d aVector)
	{
		double cx = 2 * x;
		double cy = 2 * y;
		double cz = 2 * z;

		double ccx = y * aVector.z - z * aVector.y + aVector.x * w;
		double ccy = z * aVector.x - x * aVector.z + aVector.y * w;
		double ccz = x * aVector.y - y * aVector.x + aVector.z * w;

		aVector.x += cy * ccz - cz * ccy;
		aVector.y += cz * ccx - cx * ccz;
		aVector.z += cx * ccy - cy * ccx;

		return aVector;
	}


	public QuaternionNew multiply(QuaternionNew aQuaternion)
	{
		double tw = aQuaternion.w;
		double tx = aQuaternion.x;
		double ty = aQuaternion.y;
		double tz = aQuaternion.z;

		double rw = w * tw - x * tx - y * ty - z * tz;
		double rx = w * tx + x * tw + y * tz - z * ty;
		double ry = w * ty + y * tw + z * tx - x * tz;
		double rz = w * tz + z * tw + x * ty - y * tx;

		w = rw;
		x = rx;
		y = ry;
		z = rz;

		return this;
	}


	public QuaternionNew multiply(double aScalar)
	{
		w *= aScalar;
		x *= aScalar;
		y *= aScalar;
		z *= aScalar;

		return this;
	}


	public QuaternionNew add(QuaternionNew q)
	{
		w += q.w;
		x += q.x;
		y += q.y;
		z += q.z;

		return this;
	}


	public QuaternionNew div(double aScalar)
	{
		w /= aScalar;
		x /= aScalar;
		y /= aScalar;
		z /= aScalar;

		return this;
	}


	public QuaternionNew normalize()
	{
		double sqrt = Math.sqrt(dot(this));
		if (sqrt != 0)
		{
			div(sqrt);
		}

		return this;
	}


	public double dot(QuaternionNew q)
	{
		return w * q.w + x * q.x + y * q.y + z * q.z;
	}


	public Vec3d getVectorPart()
	{
		return new Vec3d(x, y, z);
	}


	/**
	 * Flips the sign of each component of the quaternion.
	 */
	public QuaternionNew negate()
	{
		w = -w;
		x = -x;
		y = -y;
		z = -z;

		return this;
	}


	public QuaternionNew conjugate()
	{
		x = -x;
		y = -y;
		z = -z;

		return this;
	}


	public QuaternionNew identity()
	{
		w = 1;
		x = 0;
		y = 0;
		z = 0;
		return this;
	}


	public static QuaternionNew lookAt(Vec3d aSourcePoint, Vec3d aDestPoint)
	{
		Vec3d forwardVector = aDestPoint.clone().subtract(aSourcePoint).normalize();

		Vec3d forward = new Vec3d(1, 0, 0);
		Vec3d up = new Vec3d(0, 1, 0);

		double dot = forwardVector.dot(forward);

		if (Math.abs(dot - (-1.0)) < 0.000001)
		{
			return new QuaternionNew(up.x, up.y, up.z, Math.PI);
		}
		if (Math.abs(dot - (1.0)) < 0.000001)
		{
			return new QuaternionNew().identity();
		}

		double rotAngle = Math.acos(dot);
		Vec3d rotAxis = forward.cross(forwardVector);
		rotAxis = rotAxis.normalize();

		return createFromAxisAngle(rotAxis, rotAngle);
	}


	public static QuaternionNew createFromAxisAngle(Vec3d aAxis, double aAngle)
	{
		double halfAngle = aAngle * 0.5;
		double s = Math.sin(halfAngle);
		QuaternionNew q = new QuaternionNew();
		q.x = aAxis.x * s;
		q.y = aAxis.y * s;
		q.z = aAxis.z * s;
		q.w = Math.cos(halfAngle);
		return q;
	}


	public Vec3d toEulerAngle()
	{
		Vec3d result = new Vec3d();

		// roll (x-axis rotation)
		double sinr = +2.0 * (w * x + y * z);
		double cosr = +1.0 - 2.0 * (x * x + y * y);
		result.x = Math.atan2(sinr, cosr);

		// pitch (y-axis rotation)
		double sinp = +2.0 * (w * y - z * x);
		if (Math.abs(sinp) >= 1)
		{
			result.y = sinp < 0 ? -Math.PI / 2 : Math.PI / 2; // use 90 degrees if out of range
		}
		else
		{
			result.y = Math.asin(sinp);
		}

		// yaw (z-axis rotation)
		double siny = +2.0 * (w * z + x * y);
		double cosy = +1.0 - 2.0 * (y * y + z * z);
		result.z = Math.atan2(siny, cosy);

		return result;
	}

	
	@Override
	public String toString()
	{
		return "{w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + "}";
	}


	@Override
	public QuaternionNew clone()
	{
		return new QuaternionNew(w, x, y, z);
	}
}
