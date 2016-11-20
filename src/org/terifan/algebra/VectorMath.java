package org.terifan.algebra;


public class VectorMath 
{
	public static Vec3d getPerpendicularPointAlongAxis(Vec3d aStartPosition, Vec3d aEndPosition, double aAxisAlpha, double aAngleAlpha, double aRadius)
	{
		double angle = Math.PI * 2.0 * aAngleAlpha;

		Vec3d len = aEndPosition.clone().subtract(aStartPosition);
		Vec3d firstPerp = new Vec3d(len.z, len.z, -(len.x + len.y)).normalize();
		Vec3d secondPerp = firstPerp.clone().cross(len).normalize();
		Vec3d direction = firstPerp.scale(Math.cos(angle)).add(secondPerp.scale(Math.sin(angle)));

		return aStartPosition.clone().interpolate(aEndPosition, aAxisAlpha).add(direction.scale(aRadius));
	}
}
