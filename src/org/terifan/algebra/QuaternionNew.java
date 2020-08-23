package org.terifan.algebra;


/**
 * Quaternion implementation used to rotate points in 3D-space.
 */
public class QuaternionNew
{
	double x, y, z, w;


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


	public QuaternionNew(double aX, double aY, double aZ, double aW)
	{
		x = aX;
		y = aY;
		z = aZ;
		w = aW;
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public static QuaternionNew createFromRotationMatrix(Mat3d matrix)
	{
		QuaternionNew q = new QuaternionNew();

		double t = matrix.m00 + matrix.m11 + matrix.m22;

		if (t > 0.0)
		{
			double invS = 0.5 / Math.sqrt(1.0 + t);

			q.x = (matrix.m21 - matrix.m12) * invS;
			q.y = (matrix.m02 - matrix.m20) * invS;
			q.z = (matrix.m10 - matrix.m01) * invS;
			q.w = 0.25 / invS;
		}
		else
		{
			if (matrix.m00 > matrix.m11 && matrix.m00 > matrix.m22)
			{
				double invS = 0.5 / Math.sqrt(1.0 + matrix.m00 - matrix.m11 - matrix.m22);

				q.x = 0.25 / invS;
				q.y = (matrix.m01 + matrix.m10) * invS;
				q.z = (matrix.m20 + matrix.m02) * invS;
				q.w = (matrix.m21 - matrix.m12) * invS;
			}
			else if (matrix.m11 > matrix.m22)
			{
				double invS = 0.5f / Math.sqrt(1.0f + matrix.m11 - matrix.m00 - matrix.m22);

				q.x = (matrix.m01 + matrix.m10) * invS;
				q.y = 0.25 / invS;
				q.z = (matrix.m12 + matrix.m21) * invS;
				q.w = (matrix.m02 - matrix.m20) * invS;
			}
			else
			{
				double invS = 0.5f / Math.sqrt(1.0f + matrix.m22 - matrix.m00 - matrix.m11);

				q.x = (matrix.m02 + matrix.m20) * invS;
				q.y = (matrix.m12 + matrix.m21) * invS;
				q.z = 0.25 / invS;
				q.w = (matrix.m10 - matrix.m01) * invS;
			}
		}

		return q;
	}


	/**
	 * Transforms a single Vector.
	 *
	 * @param aVector the vector to transform
	 * @return the provided vector
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
		return x * q.x + y * q.y + z * q.z + w * q.w;
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
		x = -x;
		y = -y;
		z = -z;
		w = -w;

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
		x = 0;
		y = 0;
		z = 0;
		w = 1;
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
			return new QuaternionNew(up.y, up.z, Math.PI, up.x);
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


	public Vec3d rotate(Vec3d v)
	{
		return v.add(new Vec3d(x, y, z).scale(2).cross(new Vec3d(x, y, z).cross(v).add(v.clone().scale(w))));
	}


	@Override
	public String toString()
	{
		return "{x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "}";
	}


	@Override
	public QuaternionNew clone()
	{
		return new QuaternionNew(x, y, z, w);
	}
}
