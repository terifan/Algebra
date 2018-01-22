package org.terifan.algebra;


/**
 * Quaternion implementation used to rotate points in 3D-space.
 */
public final class Quaternion
{
	private final Vec3d mDirection;
	private boolean mInverted;
	private boolean mUpdatedAngle;
	private double w, x, y, z;


	public Quaternion()
	{
		mDirection = new Vec3d();
		mUpdatedAngle = true;
	}


	public Quaternion(double aW, double aX, double aY, double aZ)
	{
		this();

		w = aW;
		x = aX;
		y = aY;
		z = aZ;
		mUpdatedAngle = false;
	}


	private void init()
	{
		double tx = Math.PI * mDirection.x;
		double ty = Math.PI * mDirection.y;
		double tz = Math.PI * mDirection.z;

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

		if (mInverted)
		{
			double scale = 1.0 / (x * x + y * y + z * z + w * w);

			w = w * scale;
			x = -x * scale;
			y = -y * scale;
			z = -z * scale;
		}

		mUpdatedAngle = false;
	}


	/**
	 * Sets the direction of this Quaternion.
	 */
	public Quaternion setDirection(Vec3d aVector)
	{
		mDirection.set(aVector);
		mDirection.wrap();
		mUpdatedAngle = true;

		return this;
	}


	/**
	 * Sets the direction of this Quaternion.
	 */
	public Quaternion setDirection(double x, double y, double z)
	{
		mDirection.set(x, y, z);
		mDirection.wrap();
		mUpdatedAngle = true;

		return this;
	}


	/**
	 * Rotates this Quaternion.
	 */
	public Quaternion rotate(Vec3d aVector)
	{
		mDirection.add(aVector);
		mDirection.wrap();
		mUpdatedAngle = true;

		return this;
	}


	/**
	 * Rotates this Quaternion.
	 */
	public Quaternion rotate(double x, double y, double z)
	{
		mDirection.add(x, y, z);
		mDirection.wrap();
		mUpdatedAngle = true;

		return this;
	}


	/**
	 * Gets the direction.
	 */
	public Vec3d getDirection()
	{
		return mDirection;
	}


	/**
	 * Makes this Quaternion object inverted. An inverted Quaternion computes
	 * the rotation in an inverted order (z,y,x instead of x,y,z).
	 *
	 * Inverted rotations is used when a coordinate is rotated from world
	 * space to object space.
	 */
	public Quaternion setInverted(boolean aState)
	{
		mInverted = aState;
		mUpdatedAngle = true;
		return this;
	}


	/**
	 * Returns true if this Quaternion is inverted.
	 */
	public boolean isInverted()
	{
		return mInverted;
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
		init();

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


	/**
	 * Transforms a single Vector.<p>
	 *
	 * Note: it's necessary to call the initialize method before a vector can
	 * be transformed.
	 *
	 * @param aVector
	 * the vector to transform
	 */
	public Vec3f transform(Vec3f aVector)
	{
		init();

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


	public Quaternion multiply(Quaternion aQuaternion)
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

	
	// http://www.euclideanspace.com/maths/algebra/vectors/lookat/index.htm
//	public Quaternion lookAt(Vec3d target, Vec3d current, Vec3d eye, Vec3d up)
//	{
//		// turn vectors into unit vectors 
//		Vec3d n1 = current.clone().subtract(eye).normalize();
//		Vec3d n2 = target.clone().subtract(eye).normalize();
//		double d = n1.dot(n2);
//
//		// if no noticable rotation is available return zero rotation
//		// this way we avoid Cross product artifacts 
//		if (d > 0.9998)
//		{
//			return new Quaternion(0, 0, 1, 0);
//		}
//		// in this case there are 2 lines on the same axis 
//		if (d < -0.9998)
//		{
//			n1.x += 0.5;
//			// there are an infinite number of normals 
//			// in this case. Anyone of these normals will be 
//			// a valid rotation (180 degrees). so rotate the curr axis by 0.5 radians this way we get one of these normals 
//		}
//		Vec3d axis = n1.clone();
//		axis.cross(n2);
//		Quaternion pointToTarget = new Quaternion(1.0 + d, axis.x, axis.y, axis.z);
//		pointToTarget.norm();
//		// now twist around the target vector, so that the 'up' vector points along the z axis
//		Mat3d projectionMatrix = new Mat3d();
//		double a = pointToTarget.x;
//		double b = pointToTarget.y;
//		double c = pointToTarget.z;
//		projectionMatrix.setColumn(0, b * b + c * c, -a * b, -a * c);
//		projectionMatrix.setColumn(1, -b * a, a * a + c * c, -b * c);
//		projectionMatrix.setColumn(2, -c * a, -c * b, a * a + b * b);
//
//		Vec3d upProjected = projectionMatrix.transform(up);
//		Vec3d yaxisProjected = projectionMatrix.transform(new Vec3d(0, 1, 0));
//		d = upProjected.dot(yaxisProjected);
//		// so the axis of twist is n2 and the angle is arcos(d)
//		//convert this to quat as follows   
//		double s = Math.sqrt(1.0 - d * d);
//		Quaternion twist = new Quaternion(d, n2.x * s, n2.y * s, n2.z * s); // ????????
//		return pointToTarget.multiply(twist);
//	}


	@Override
	public Quaternion clone()
	{
		Quaternion q = new Quaternion(w, x, y, z);
		q.mDirection.set(mDirection);
		q.mInverted = mInverted;
		q.mUpdatedAngle = mUpdatedAngle;
		return q;
	}


	@Override
	public String toString()
	{
		return "{w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + "}";
	}
}
