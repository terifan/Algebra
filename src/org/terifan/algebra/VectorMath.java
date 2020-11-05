package org.terifan.algebra;

import java.util.Random;

public class VectorMath
{
	public static Vec3d getPerpendicularPointAlongAxis(Vec3d aStartPosition, Vec3d aEndPosition, double aAxisAlpha, double aAngleAlpha, double aRadius)
	{
		double angle = Math.PI * 2.0 * aAngleAlpha;

		Vec3d len = aEndPosition.clone().subtract(aStartPosition);
		Vec3d firstPerp = new Vec3d(len.z, len.z, -(len.x + len.y)).normalize();
		Vec3d secondPerp = firstPerp.clone().cross(len).normalize();
		Vec3d direction = firstPerp.multiply(Math.cos(angle)).add(secondPerp.multiply(Math.sin(angle)));

		return aStartPosition.clone().interpolate(aEndPosition, aAxisAlpha).add(direction.multiply(aRadius));
	}


	public static Vec3d importanceSampleUpperHemisphere(Random aRandom, Vec3d aLightNormal, double n)
	{
		return importanceSampleUpperHemisphere(aRandom.nextDouble(), aRandom.nextDouble(), aRandom.nextDouble(), aRandom.nextDouble(), aRandom.nextDouble(), aLightNormal, n);

//		double z = aRandom.nextDouble();
//		double phi = aRandom.nextDouble() * 2 * Math.PI;
//		double theta = (n == 1 ? Math.acos(Math.sqrt(z)) : Math.acos(Math.pow(z, 1 / (n + 1))));
//
//		// Create vector aligned with z=(0,0,1)
//		double sintheta = Math.sin(theta);
//		Vector sample = new Vector(sintheta * Math.cos(phi), sintheta * Math.sin(phi), z);
//
//		// Rotate sample to be aligned with normal
//		Vector t = new Vector(aRandom.nextDouble(), aRandom.nextDouble(), aRandom.nextDouble());
//		Vector u = t.cross(aLightNormal).normalize();
//		Vector v = aLightNormal.clone().cross(u);
//		mat3 rot = new mat3(u, v, aLightNormal.clone());
//
//		return rot.transpose().scale(sample);
	}


	public static Vec3d importanceSampleUpperHemisphere(double x, double y, double z, double gamma, double phi, Vec3d aLightNormal, double n)
	{
		phi *= 2 * Math.PI;
		double theta = (n == 1 ? Math.acos(Math.sqrt(gamma)) : Math.acos(Math.pow(gamma, 1 / (n + 1))));

		// Create vector aligned with z=(0,0,1)
		double sintheta = Math.sin(theta);
		Vec3d sample = new Vec3d(sintheta * Math.cos(phi), sintheta * Math.sin(phi), gamma);

		// Rotate sample to be aligned with normal
		Vec3d t = new Vec3d(x, y, z);
		Vec3d u = t.cross(aLightNormal).normalize();
		Vec3d v = aLightNormal.clone().cross(u);
		Mat3d rot = new Mat3d(u, v, aLightNormal.clone());

		return rot.transpose().multiply(sample);
	}


	public static Vec3d uniformSampleUpperHemisphere(double x, double y, double z, Vec3d aLightNormal)
	{
		Vec3d sample = new Vec3d(
			1 - 2 * x,
			1 - 2 * y,
			1 - 2 * z
		);

		if (sample.dot(aLightNormal) < 0)
		{
			sample.multiply(-1);
		}

		return sample.normalize();
	}


	public static Vec3d hemisphere(double u1, double u2)
	{
		double r = Math.sqrt(1.0 - u1 * u1);
		double phi = 2 * Math.PI * u2;
		return new Vec3d(Math.cos(phi) * r, Math.sin(phi) * r, u1);
	}
}
