package org.terifan.algebra;


/**
 * A class representing a plane.
 */
public class Plane implements Cloneable
{
	public final static int PROJECTION_PLANE_X = 1;
	public final static int PROJECTION_PLANE_Y = 2;
	public final static int PROJECTION_PLANE_Z = 4;

	public final static int FRONT_OF_PLANE = 1;
	public final static int BACK_OF_PLANE = 2;
	public final static int INTERSECT_PLANE = 4;

	private Vec3d mNormal;
	private double mDistance;
	private Vec3d mOrigin;
	private Vec3d mDirection;


	/**
	 * Construct a new plane.
	 */
	public Plane()
	{
		mNormal = new Vec3d();
		mDistance = 0;
	}


	/**
	 * Construct a new plane.
	 */
	public Plane(Vec3d aNormal, double aDistance)
	{
		mNormal = aNormal.clone();
		mDistance = aDistance;
	}


	/**
	 * Construct a new plane from three vertices.
	 */
	public Plane(Vec3d a, Vec3d b, Vec3d c)
	{
		mNormal = computeNormal(a, b, c, new Vec3d());
		mDistance = a.dot(mNormal);
		mOrigin = new Vec3d(a.x, a.y, a.z);
	}


	/**
	 * Construct a new plane from three vertices.
	 */
	public Plane(Vec3d aOrigin, Vec3d aDirection)
	{
		mOrigin = aOrigin.clone();
		mNormal = aDirection.clone().normalize();
		mDistance = mOrigin.dot(mNormal);
	}


	/**
	 * Construct a new plane from three vertices.
	 */
	public Plane(double ax, double ay, double az, double bx, double by, double bz, double cx, double cy, double cz)
	{
		mNormal = new Vec3d();
		mNormal.x = (cy - by) * (az - bz) - (cz - bz) * (ay - by);
		mNormal.y = (cz - bz) * (ax - bx) - (cx - bx) * (az - bz);
		mNormal.z = (cx - bx) * (ay - by) - (cy - by) * (ax - bx);
		mNormal.normalize();
		mDistance = mNormal.dot(ax, ay, az);
		mOrigin = new Vec3d(ax, ay, az);
	}


	public Vec3d getOrigin()
	{
		return mOrigin;
	}
	
	
	public void setOrigin(Vec3d aOrigin)
	{
		mOrigin = aOrigin;
	}


	/**
	 *
	 */
	public int getProjectionPlane()
	{
		Vec3d normal = getNormal();
	
		if (Math.abs(normal.x) > Math.abs(normal.y) && Math.abs(normal.x) > Math.abs(normal.z))
		{
			return PROJECTION_PLANE_X;
		}
		else if (Math.abs(normal.y) > Math.abs(normal.x) && Math.abs(normal.y) > Math.abs(normal.z))
		{
			return PROJECTION_PLANE_Y; 
		}
		else
		{
			return PROJECTION_PLANE_Z;
		}
	}


	/**
	 * Sets the normal of this plane.
	 */
	public void setNormal(Vec3d aVector)
	{
		mNormal.set(aVector);
	}


	/**
	 * Gets the normal of this plane.
	 */
	public Vec3d getNormal()
	{
		return mNormal;
	}


	/**
	 * Sets the distance of this plane.
	 */
	public void setDistance(double aDistance)
	{
		mDistance = aDistance;
	}


	/**
	 * Gets the distance of this plane.
	 */
	public double getDistance()
	{
		return mDistance;
	}


	/**
	 * Find the distance between a ray and a plane.
	 *
	 * @param aRayOrigin
	 *    A position in world space.
	 * @param aRayVector
	 *    A normalized direction vector.
	 * @return
	 *    The distance or Double.NaN if the ray and plane are parallel.
	 */
	public double distanceVectorPlane2(Vec3d aOrigin)
	{
		double denom = mNormal.dot(mNormal.clone().scale(-1)); // ?????????? ger alltid -1 plus/minus 0.000001

		if (denom == 0)
		{
			return Double.NaN;
		}

		return -((mNormal.dot(aOrigin) - mDistance) / denom);	 // "-" var "+" förrut
	}


	/**
	 * Returns the distance from a coordinate to a plane.
	 */
	public double distanceVectorPlane(Vec3d aVector)
	{
		return mNormal.dot(aVector) - mDistance;
	}	


	/**
	 * Returns the distance from a coordinate to a plane.
	 */
	public double distanceVectorPlane(double aCoordinateX, double aCoordinateY, double aCoordinateZ)
	{
		return mNormal.dot(aCoordinateX, aCoordinateY, aCoordinateZ) - mDistance;
	}	


	/**
	 * Returns the intersection distance of a ray and a plane.
	 *
	 * @param aOrigin
	 *    Origin of the ray.
	 * @param aVector
	 *    Direction of the ray. This values must be normalized.
	 * @return
	 *    A value ranging from 0 to 1 of the ray intersects the plane and 
	 *    Double.NaN if the ray never intersects the plane.
	 */
	public double intersectRayPlane(Vec3d aOrigin, Vec3d aVector) 
	{
		double denom = mNormal.dot(aVector);

		if (denom == 0)
		{
			return Double.NaN;
		}

		return -((mNormal.dot(aOrigin) - mDistance) / denom);	 // "-" var "+" förrut
	}


	/**
	 * Returns the intersection distance of a ray and a plane.
	 *
	 * @param aRay
	 *    the Ray.
	 * @return
	 *    A value ranging from 0 to 1 of the ray intersects the plane and 
	 *    Double.NaN if the ray never intersects the plane.
	 */
	public double intersectRayPlane(Ray aRay)
	{
		double denom = mNormal.dot(aRay.getDirection());

		if (denom == 0)
		{
			return Double.NaN;
		}

		return -((mNormal.dot(aRay.getOrigin()) - mDistance) / denom);	 // "-" var "+" förrut
	}


	/**
	 * This function checks if point is in front of, in back of, or on the plane.
	 * 
	 * @return
	 *    PLANE_FRONT; if the point is in the front of the plane.
	 *    PLANE_BACKSIDE; if the point is in the back of the plane.
	 *    ON_PLANE; if the point is on the plane.
	 */
	public int classifyPoint(Vec3d aPoint)
	{
		double d = mNormal.dot(mOrigin.x - aPoint.x, mOrigin.y - aPoint.y, mOrigin.z - aPoint.z);

		if (d < -0.001)
		{
			return FRONT_OF_PLANE; // front of plane
		}
		else if (d > 0.001)
		{
			return BACK_OF_PLANE; // backside of plane
		}
		else
		{
			return INTERSECT_PLANE; // on plane
		}
	}


	/**
	 * Computes a normal given three vertices in a triangle.
	 */
	public static Vec3d computeNormal(Vec3d a, Vec3d b, Vec3d c, Vec3d aNormal)
	{
		aNormal.x = (c.y - b.y) * (a.z - b.z) - (c.z - b.z) * (a.y - b.y);
		aNormal.y = (c.z - b.z) * (a.x - b.x) - (c.x - b.x) * (a.z - b.z);
		aNormal.z = (c.x - b.x) * (a.y - b.y) - (c.y - b.y) * (a.x - b.x);
		aNormal.normalize();
		return aNormal;
	}


	/**
	 * Computes a normal given three vertices in a triangle.
	 */
	public static Vec3f computeNormal(Vec3f a, Vec3f b, Vec3f c, Vec3f aNormal)
	{
		aNormal.x = (c.y - b.y) * (a.z - b.z) - (c.z - b.z) * (a.y - b.y);
		aNormal.y = (c.z - b.z) * (a.x - b.x) - (c.x - b.x) * (a.z - b.z);
		aNormal.z = (c.x - b.x) * (a.y - b.y) - (c.y - b.y) * (a.x - b.x);
		aNormal.normalize();
		return aNormal;
	}


	/**
	 * Gets a description of this object.
	 */
	@Override
	public String toString()
	{
		return "Plane[normal=[" + mNormal + "] distance=" + mDistance + "]";
	}


	/**
	 * Returns true if the object provided is an instance of Plane and the 
	 * normal and distance values match.
	 *
	 * Two normal values match if the difference between the values is smaller 
	 * than the threshold specified.
	 */
	public boolean compare(Object aObject, double aThresholdPlane, double aThresholdDistance)
	{
		if (!(aObject instanceof Plane))
		{
			return false;
		}

		Plane plane = (Plane)aObject;

		if (Math.abs(plane.mDistance - mDistance) > aThresholdDistance) return false;
		if (Math.abs(plane.mNormal.x - mNormal.x) > aThresholdPlane) return false;
		if (Math.abs(plane.mNormal.y - mNormal.y) > aThresholdPlane) return false;
		if (Math.abs(plane.mNormal.z - mNormal.z) > aThresholdPlane) return false;

		return true;
	}


	public Vec3d closestPointOnPlane(Vec3d aVector)
	{
		return aVector.clone().subtract(getNormal().scale(intersectRayPlane(aVector, getNormal().clone().scale(-1))));
	}


	@Override
	public Plane clone()
	{
		try
		{
			Plane p = (Plane)super.clone();
			p.mDistance = mDistance;
			if (mNormal != null)
			{
				p.mNormal = mNormal.clone();
			}
			if (mOrigin != null)
			{
				p.mOrigin = mOrigin.clone();
			}
			if (mDirection != null)
			{
				p.mDirection = mDirection.clone();
			}
			return p;
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}