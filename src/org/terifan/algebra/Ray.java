package org.terifan.algebra;


/**
 * The Ray class represents a ray with an origin and direction.
 */
public class Ray implements Cloneable
{
	private Vec3d mOrigin;
	private Vec3d mDirection;
	private double mMin;
	private double mMax;


	public Ray()
	{
		this(new Vec3d(), new Vec3d(), 0, Double.MAX_VALUE);
	}


	public Ray(Vec3d aOrigin, Vec3d aDirection)
	{
		this(aOrigin, aDirection, 0, Double.MAX_VALUE);
	}


	public Ray(Vec3d aOrigin, Vec3d aDirection, double aMin, double aMax)
	{
		mOrigin = aOrigin;
		mDirection = aDirection;
		mMin = aMin;
		mMax = aMax;
	}


	public Vec3d getOrigin()
	{
		return mOrigin;
	}


	public Vec3d getDirection()
	{
		return mDirection;
	}


	@Override
	public String toString()
	{
		return "{origin=" + mOrigin + ", direction=" + mDirection + "}";
	}


	@Override
	public Ray clone()
	{
		return new Ray(mOrigin.clone(), mDirection.clone(), mMin, mMax);
	}


	public double getDistanceToPoint(Vec3d aPoint)
	{
		double tx = aPoint.x - mOrigin.x;
		double ty = aPoint.y - mOrigin.y;
		double tz = aPoint.z - mOrigin.z;

		double x = mDirection.y * tz - mDirection.z * ty;
		double y = mDirection.z * tx - mDirection.x * tz;
		double z = mDirection.x * ty - mDirection.y * tx;

		return Math.sqrt(x * x + y * y + z * z);
	}


	public Vec3d getClosestPointOnLineSegment(Ray aRay, double aLength)
	{
	    Vec3d diff = mOrigin.clone().subtract(aRay.mOrigin);
		double a01 = -mDirection.dot(aRay.mDirection);
		double b0 = diff.dot(mDirection);
		double b1 = -diff.dot(aRay.mDirection);
		double c = diff.length(); //SquaredLength();
		double det = Math.abs(1.0 - a01*a01);
		double s0, s1, sqrDist, extDet;

		if (det >= 0.00001)
		{
			// The ray and segment are not parallel.
			s0 = a01*b1 - b0;
			s1 = a01*b0 - b1;
			extDet = aLength*det;

			if (s0 >= 0)
			{
				if (s1 >= -extDet)
				{
					if (s1 <= extDet)  // region 0
					{
						// Minimum at interior points of ray and segment.
						double invDet = (1)/det;
						s0 *= invDet;
						s1 *= invDet;
						sqrDist = 0;
					}
					else  // region 1
					{
						s1 = aLength;
						s0 = -(a01*s1 + b0);
						if (s0 > 0)
						{
							sqrDist = -s0*s0 + s1*(s1 + (2)*b1) + c;
						}
						else
						{
							sqrDist = s1*(s1 + (2)*b1) + c;
						}
					}
				}
				else  // region 5
				{
					s1 = -aLength;
					s0 = -(a01*s1 + b0);
					if (s0 > 0)
					{
						sqrDist = -s0*s0 + s1*(s1 + (2)*b1) + c;
					}
					else
					{
						sqrDist = s1*(s1 + (2)*b1) + c;
					}
				}
			}
			else
			{
				if (s1 <= -extDet)  // region 4
				{
					s0 = -(-a01*aLength + b0);
					if (s0 > 0)
					{
						s1 = -aLength;
						sqrDist = -s0*s0 + s1*(s1 + (2)*b1) + c;
					}
					else
					{
						s1 = -b1;
						if (s1 < -aLength)
						{
							s1 = -aLength;
						}
						else if (s1 > aLength)
						{
							s1 = aLength;
						}
						sqrDist = s1*(s1 + (2)*b1) + c;
					}
				}
				else if (s1 <= extDet)  // region 3
				{
					s1 = -b1;
					if (s1 < -aLength)
					{
						s1 = -aLength;
					}
					else if (s1 > aLength)
					{
						s1 = aLength;
					}
					sqrDist = s1*(s1 + (2)*b1) + c;
				}
				else  // region 2
				{
					s0 = -(a01*aLength + b0);
					if (s0 > 0)
					{
						s1 = aLength;
						sqrDist = -s0*s0 + s1*(s1 + (2)*b1) + c;
					}
					else
					{
						s1 = -b1;
						if (s1 < -aLength)
						{
							s1 = -aLength;
						}
						else if (s1 > aLength)
						{
							s1 = aLength;
						}
						sqrDist = s1*(s1 + (2)*b1) + c;
					}
				}
			}
		}
		else
		{
			// Ray and segment are parallel.
			if (a01 > 0)
			{
				// Opposite direction vectors.
				s1 = -aLength;
			}
			else
			{
				// Same direction vectors.
				s1 = aLength;
			}

			s0 = -(a01*s1 + b0);
			if (s0 > 0)
			{
				sqrDist = -s0*s0 + s1*(s1 + (2)*b1) + c;
			}
			else
			{
				sqrDist = s1*(s1 + (2)*b1) + c;
			}
		}

		if (s1 < 0)
		{
			s1 = 0;
		}

		return aRay.mDirection.clone().scale(s1).add(aRay.mOrigin);
	}


	public Vec3d getClosestPoint(Ray aRay)
	{
		Vec3d diff = mOrigin.clone().subtract(aRay.mOrigin);
		double a01 = -mDirection.dot(aRay.mDirection);
		double b0 = diff.dot(mDirection);
		double c = diff.length();
		double det = Math.abs(1 - a01*a01);
		double b1, s0, s1, sqrDist;

		if (det >= 0.000001)
		{
			// Rays are not parallel.
			b1 = -diff.dot(aRay.mDirection);
			s0 = a01*b1 - b0;
			s1 = a01*b0 - b1;

			if (s0 >= 0)
			{
				if (s1 >= 0)  // region 0 (interior)
				{
					// Minimum at two interior points of rays.
					double invDet = (1)/det;
					s0 *= invDet;
					s1 *= invDet;
					sqrDist = 0;
				}
				else  // region 3 (side)
				{
					if (b0 >= 0)
					{
						s0 = 0;
						sqrDist = c;
					}
					else
					{
						s0 = -b0;
						sqrDist = b0*s0 + c;
					}
				}
			}
			else
			{
				if (s1 >= 0)  // region 1 (side)
				{
					s0 = 0;
					if (b1 >= 0)
					{
						sqrDist = c;
					}
					else
					{
						s1 = -b1;
						sqrDist = b1*s1 + c;
					}
				}
				else  // region 2 (corner)
				{
					if (b0 < 0)
					{
						s0 = -b0;
						sqrDist = b0*s0 + c;
					}
					else
					{
						s0 = 0;
						if (b1 >= 0)
						{
							sqrDist = c;
						}
						else
						{
							s1 = -b1;
							sqrDist = b1*s1 + c;
						}
					}
				}
			}
		}
		else
		{
			// Rays are parallel.
			if (a01 > 0.0)
			{
				// Opposite direction vectors.
				if (b0 >= 0)
				{
					s0 = 0;
					sqrDist = c;
				}
				else
				{
					s0 = -b0;
					sqrDist = b0*s0 + c;
				}
			}
			else
			{
				// Same direction vectors.
				if (b0 >= 0)
				{
					b1 = -diff.dot(aRay.mDirection);
					s0 = 0;
					s1 = -b1;
					sqrDist = b1*s1 + c;
				}
				else
				{
					s0 = -b0;
					sqrDist = b0*s0 + c;
				}
			}
		}

		return mOrigin.clone().add(mDirection.clone().scale(s0));
	}


//	// http://answers.unity3d.com/questions/192261/finding-shortest-line-segment-between-two-rays-clo.html
//	public static double closestTimeOfApproach(Vec3d pos1, Vec3d vel1, Vec3d pos2, Vec3d vel2)
//	{
//		Vec3d dv = vel1.clone().subtract(vel2);
//		double dv2 = dv.dot(dv);
//		if (dv2 < 0.0000001)      // the tracks are almost parallel
//		{
//			return 0.0; // any time is ok.  Use time 0.
//		}
//
//		Vec3d w0 = pos1.clone().subtract(pos2);
//
//		return -w0.dot(dv) / dv2;
//	}
//
//
//	public static double closestDistOfApproach(Vec3d pos1, Vec3d vel1, Vec3d pos2, Vec3d vel2, Vec3d p1, Vec3d p2)
//	{
//		double t = closestTimeOfApproach(pos1, vel1, pos2, vel2);
//
//		p1.set(pos1).add(vel1.clone().scale(t));
//		p2.set(pos2).add(vel2.clone().scale(t));
//
//		return p1.distance(p2); // distance at CPA
//	}
//
//
//	public static Vec3d closestPointOfApproach(Vec3d pos1, Vec3d vel1, Vec3d pos2, Vec3d vel2)
//	{
//		double t = closestTimeOfApproach(pos1, vel1, pos2, vel2);
//
//		return pos1.clone().add(vel1.clone().scale(t));
//	}


	public void reflect(Vec3d normal)
	{
		double n = 2 * mDirection.dot(normal);

		mDirection.x -= n * normal.x;
		mDirection.y -= n * normal.y;
		mDirection.z -= n * normal.z;
	}


	public Vec3d intersectionPoint(double t)
	{
		return mDirection.clone().scale(t).add(mOrigin);
	}


	public double getMin()
	{
		return mMin;
	}


	public double getMax()
	{
		return mMax;
	}

	
	public boolean isWithinBounds(double tVal)
	{
		return tVal <= mMax && tVal >= mMin;
	}


	public double intersectTriangle(Vec3d a, Vec3d b, Vec3d c)
	{
		// find vectors for two edges sharing vert0
		Vec3d edge1 = b.clone().subtract(a);
		Vec3d edge2 = c.clone().subtract(a);

		// begin calculating determinant - also used to calculate U parameter
		Vec3d pvec = mDirection.clone().cross(edge2);

		// if determinant is near zero, ray lies in plane of triangle
		double det = edge1.dot(pvec);

		if (det < 0.000001)
		{
			return -1;
		}

		// calculate distance from vert0 to ray origin
		Vec3d tvec = mOrigin.clone().subtract(a);

		// calculate U parameter and test bounds
		double u = tvec.dot(pvec);
		if (u < 0.0 || u > det)
		{
			return -1;
		}

		// prepare to test V parameter
		Vec3d qvec = tvec.clone().cross(edge1);

		// calculate V parameter and test bounds
		double v = mDirection.dot(qvec);
		if (v < 0.0 || u + v > det)
		{
			return -1;
		}

		// calculate t, scale parameters, ray intersects triangle
		double inv_det = 1.0 / det;
		double t = edge2.dot(qvec);

		t *= inv_det;

		return t;
	}


	public double intersectSphere(Vec3d aCenter, Vec3d aRadius)
	{
		Vec3d center = aCenter.clone();
		Vec3d radius = aRadius.clone();

		double discriminant = getDiscriminant(center, radius, mOrigin, mDirection);

		if (discriminant < 0)
		{
			return -1;
		}

		// Quadratic formula!
		double leftTerm = -mDirection.dot(mOrigin.clone().subtract(center));
		double rightTerm = Math.sqrt(discriminant);
		double denominator = mDirection.dot(mDirection);
		double tNeg = (leftTerm - rightTerm) / denominator;
		double tPos = (leftTerm + rightTerm) / denominator;

		// If either of the roots is within the ray's intersection bounds, use smaller one. Otherwise, the ray didn't hit.
		if (isWithinBounds(tNeg))
		{
			return tNeg;
		}
		else if (isWithinBounds(tPos))
		{
			return tPos;
		}

		return -1;
	}


	// Gets the discriminat givin the origin and direction of the ray
	private static double getDiscriminant(Vec3d aCenter, Vec3d aRadius, Vec3d aOrigin, Vec3d aDirection)
	{
		assert aRadius.x == aRadius.y && aRadius.x == aRadius.z;

		Vec3d cent = aOrigin.clone().subtract(aCenter);
		double scalarA = aDirection.dot(cent) * aDirection.dot(cent);
		double scalarB = cent.dot(cent) - aRadius.x * aRadius.x;
		return scalarA - aDirection.dot(aDirection) * scalarB;
	}


	public Vec3d getSphereNormal(Vec3d aCenter, Vec3d aRadius, Vec3d aPoint)
	{
		assert aRadius.x == aRadius.y && aRadius.x == aRadius.z;

		// Find Normal
		Vec3d surfaceNormal = aPoint.clone().subtract(aCenter);
		surfaceNormal.normalize();

		//Get the norm of the vector between the ray and the center of sphere.
		Vec3d originToSphere = mOrigin.clone().subtract(aCenter);

		// If the length of the vector is less than the radius, ray originated
		// from inside the sphere. Thus we need to negate the normal.
		if (originToSphere.length() < aRadius.x)
		{
			surfaceNormal.scale(-1);
		}

		return surfaceNormal;
	}
}
