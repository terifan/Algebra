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
	 *    the vector to transform
	 * @return
	 *    the provided vector
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
	
	
	// TODO: maybe wrong
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