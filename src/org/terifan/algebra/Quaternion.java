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


	public static Quaternion fromEuler(double aPitch, double aYaw, double aRoll)
	{
		Quaternion qx = new Quaternion(Math.cos(aPitch / 2), Math.sin(aPitch / 2), 0, 0);
		Quaternion qy = new Quaternion(Math.cos(aYaw / 2), 0, Math.sin(aYaw / 2), 0);
		Quaternion qz = new Quaternion(Math.cos(aRoll / 2), 0, 0, Math.sin(aRoll / 2));
		Quaternion qt = new Quaternion();
		qt.set(qx);
		qt.mul(qy);
		qt.mul(qz);

//		x *= M_DEGTORAD_2;
//		y *= M_DEGTORAD_2;
//		z *= M_DEGTORAD_2;
//		float sinX = sinf(x);
//		float cosX = cosf(x);
//		float sinY = sinf(y);
//		float cosY = cosf(y);
//		float sinZ = sinf(z);
//		float cosZ = cosf(z);
//
//		w_ = cosY * cosX * cosZ + sinY * sinX * sinZ;
//		x_ = cosY * sinX * cosZ + sinY * cosX * sinZ;
//		y_ = sinY * cosX * cosZ - cosY * sinX * sinZ;
//		z_ = cosY * cosX * sinZ - sinY * sinX * cosZ;		
		return qt;
	}


	public Quaternion set(Quaternion aQuaternion)
	{
		mDirection.set(aQuaternion.mDirection);
		mInverted = aQuaternion.mInverted;
		mUpdatedAngle = aQuaternion.mUpdatedAngle;
		w = aQuaternion.w;
		x = aQuaternion.x;
		y = aQuaternion.y;
		z = aQuaternion.z;

		return this;
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
	 * Makes this Quaternion object inverted. An inverted Quaternion computes the rotation in an inverted order (z,y,x instead of x,y,z).
	 *
	 * Inverted rotations is used when a coordinate is rotated from world space to object space.
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
	 * @param aVector the vector to transform
	 * @return the provided vector
	 */
	public Vec3d transform(Vec3d aVector)
	{
		if (mUpdatedAngle)
		{
			init();
		}

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
	 * Note: it's necessary to call the initialize method before a vector can be transformed.
	 *
	 * @param aVector the vector to transform
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


	public Quaternion mul(Quaternion aQuaternion)
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


	public Quaternion mul(double aScalar)
	{
		w *= aScalar;
		x *= aScalar;
		y *= aScalar;
		z *= aScalar;

		return this;
	}


	public Quaternion add(Quaternion q)
	{
		w += q.w;
		x += q.x;
		y += q.y;
		z += q.z;

		return this;
	}


	public Quaternion add(double aScalar)
	{
		w += aScalar;
		x += aScalar;
		y += aScalar;
		z += aScalar;

		return this;
	}


	public Quaternion div(double aScalar)
	{
		w /= aScalar;
		x /= aScalar;
		y /= aScalar;
		z /= aScalar;

		return this;
	}


	public Quaternion normalize()
	{
		double sqrt = Math.sqrt(dot(this));
		if (sqrt != 0)
		{
			div(sqrt);
		}

		return this;
	}


	public double dot(Quaternion q)
	{
		return w * q.w + x * q.x + y * q.y + z * q.z;
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
	public Quaternion lerp(Quaternion q1, Quaternion q2, float maxAngle)
	{
		if (maxAngle < 0.001f)
		{
			// No rotation allowed. Prevent dividing by 0 later.
			return q1;
		}

		double cosTheta = q1.dot(q2);

		// q1 and q2 are already equal.
		// Force q2 just to be sure
		if (cosTheta > 0.9999f)
		{
			return q2;
		}

		// Avoid taking the long path around the sphere
		if (cosTheta < 0)
		{
			q1 = q1.mul(-1);
			cosTheta *= -1.0f;
		}

		double angle = Math.acos(cosTheta);

		// If there is only a 2&deg; difference, and we are allowed 5&deg;,
		// then we arrived.
		if (angle < maxAngle)
		{
			return q2;
		}

		double fT = maxAngle / angle;
		angle = maxAngle;
		Quaternion z1 = q1.mul(Math.sin((1.0f - fT) * angle));
		Quaternion z2 = q2.mul(Math.sin(fT * angle));

		return z1.add(z2).div(Math.sin(angle)).normalize();
	}


	// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-17-quaternions/
	public static Quaternion rotationBetweenVectors(Vec3d aStartDirection, Vec3d aEndDirection)
	{
		Vec3d start = aStartDirection.clone().normalize();
		Vec3d dest = aEndDirection.clone().normalize();

		double cosTheta = start.dot(dest);
		Vec3d rotationAxis;

		if (cosTheta < -1 + 0.001)
		{
			// special case when vectors in opposite directions: there is no "ideal" rotation axis so guess one; any will do as long as it's perpendicular to start
			rotationAxis = new Vec3d(1, 0, 0).cross(start); // RIGHT

			if (rotationAxis.length2() < 0.01) // bad luck, they were parallel, try again!
			{
				rotationAxis = new Vec3d(0, 1, 0).cross(start); // UP
			}

			rotationAxis = rotationAxis.normalize();

			return fromAngleAxis(Math.toRadians(180), rotationAxis); // angleAxis
		}

		rotationAxis = start.cross(dest);

		double s = Math.sqrt((1 + cosTheta) * 2);
		double invs = 1 / s;

		return new Quaternion(s * 0.5, rotationAxis.x * invs, rotationAxis.y * invs, rotationAxis.z * invs);
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public static Quaternion fromAngleAxis(double angle, Vec3d axis)
	{
		Vec3d normAxis = axis.clone().normalize();
		angle = Math.toRadians(angle);
		double sinAngle = Math.sin(angle);
		double cosAngle = Math.cos(angle);

		Quaternion q = new Quaternion();
		q.w = cosAngle;
		q.x = normAxis.x * sinAngle;
		q.y = normAxis.y * sinAngle;
		q.z = normAxis.z * sinAngle;

		return q;
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public static Quaternion fromLookRotation(Vec3d direction, Vec3d upDirection)
	{
		Quaternion ret;
		Vec3d forward = direction.clone().normalize();

		Vec3d v = forward.cross(upDirection);
		// If direction & upDirection are parallel and crossproduct becomes zero, use FromRotationTo() fallback
		if (v.length() >= 1e-10)
		{
			v.normalize();
			Vec3d up = v.cross(forward);
			Vec3d right = up.cross(forward);
			ret = fromAxes(right, up, forward);
		}
		else
		{
			ret = rotationBetweenVectors(new Vec3d(0, 0, 1), forward); // FORWARD
		}
		return ret;
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public static Quaternion fromAxes(Vec3d xAxis, Vec3d yAxis, Vec3d zAxis)
	{
		Mat3d matrix = new Mat3d(
			xAxis.x, yAxis.x, zAxis.x,
			xAxis.y, yAxis.y, zAxis.y,
			xAxis.z, yAxis.z, zAxis.z
		);

		return fromRotationMatrix(matrix);
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public static Quaternion fromRotationMatrix(Mat3d matrix)
	{
		Quaternion q = new Quaternion();

		double t = matrix.m[0].x + matrix.m[1].y + matrix.m[2].z;

		if (t > 0.0f)
		{
			double invS = 0.5f / Math.sqrt(1.0f + t);

			q.x = (matrix.m[2].y - matrix.m[1].z) * invS;
			q.y = (matrix.m[0].z - matrix.m[2].x) * invS;
			q.z = (matrix.m[1].x - matrix.m[0].y) * invS;
			q.w = 0.25f / invS;
		}
		else
		{
			if (matrix.m[0].x > matrix.m[1].y && matrix.m[0].x > matrix.m[2].z)
			{
				double invS = 0.5f / Math.sqrt(1.0f + matrix.m[0].x - matrix.m[1].y - matrix.m[2].z);

				q.x = 0.25f / invS;
				q.y = (matrix.m[0].y + matrix.m[1].x) * invS;
				q.z = (matrix.m[2].x + matrix.m[0].z) * invS;
				q.w = (matrix.m[2].y - matrix.m[1].z) * invS;
			}
			else if (matrix.m[1].y > matrix.m[2].z)
			{
				double invS = 0.5f / Math.sqrt(1.0f + matrix.m[1].y - matrix.m[0].x - matrix.m[2].z);

				q.x = (matrix.m[0].y + matrix.m[1].x) * invS;
				q.y = 0.25f / invS;
				q.z = (matrix.m[1].z + matrix.m[2].y) * invS;
				q.w = (matrix.m[0].z - matrix.m[2].x) * invS;
			}
			else
			{
				double invS = 0.5f / Math.sqrt(1.0f + matrix.m[2].z - matrix.m[0].x - matrix.m[1].y);

				q.x = (matrix.m[0].z + matrix.m[2].x) * invS;
				q.y = (matrix.m[1].z + matrix.m[2].y) * invS;
				q.z = 0.25f / invS;
				q.w = (matrix.m[1].x - matrix.m[0].y) * invS;
			}
		}

		return q;
	}


	// https://github.com/xamarin/Urho3D/blob/master/Source/Urho3D/Math/Quaternion.cpp
	public Vec3d eulerAngles()
	{
		double check = 2.0 * (-y * z + w * x);

		if (check < -0.995)
		{
			return new Vec3d(
				-90.0,
				0.0,
				-Math.toDegrees(Math.atan2(2.0 * (x * z - w * y), 1.0 - 2.0 * (y * y + z * z)))
			);
		}

		if (check > 0.995)
		{
			return new Vec3d(
				90.0,
				0.0,
				Math.toDegrees(Math.atan2(2.0 * (x * z - w * y), 1.0 - 2.0 * (y * y + z * z)))
			);
		}

		return new Vec3d(
			Math.toDegrees(Math.asin(check)),
			Math.toDegrees(Math.atan2(2.0 * (x * z + w * y), 1.0 - 2.0 * (x * x + y * y))),
			Math.toDegrees(Math.atan2(2.0 * (x * y + w * z), 1.0 - 2.0 * (x * x + z * z)))
		);
	}


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
