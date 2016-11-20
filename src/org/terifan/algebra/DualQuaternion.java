package org.terifan.algebra;


/**
 * Implementation of Dual Quaternion
 *
 * http://wscg.zcu.cz/wscg2012/short/A29-full.pdf
 * http://rodolphe-vaillant.fr/?e=29
 */
public class DualQuaternion implements Cloneable
{
	private QuaternionNew mReal;
	private QuaternionNew mDual;


	public DualQuaternion()
	{
		this(new QuaternionNew(0.0, 0.0, 0.0, 0.0), new QuaternionNew(0.0, 0.0, 0.0, 0.0));
	}


	public DualQuaternion(QuaternionNew aReal, QuaternionNew aDual)
	{
		mReal = aReal;
		mDual = aDual;
	}


	public DualQuaternion(QuaternionNew aQuaternion, Vec3d aTrans)
	{
		mReal = aQuaternion.clone().normalize();
		mDual = new QuaternionNew(0, aTrans.x, aTrans.y, aTrans.z).multiply(mReal).multiply(0.5);
	}


	public Vec3d transform(Vec3d p)
	{
//		double scale = Math.sqrt(real.dot(real));
//		Quaternion _real = real.clone().div(scale);
//		Quaternion _dual = dual.clone().div(scale);

		// Translation from the normalized dual quaternion equals :
		// 2.0 * dual * conjugate(real)
		Vec3d _real = mReal.getVectorPart();
		Vec3d _dual = mDual.getVectorPart();
		Vec3d trans = _real.clone().scale(mDual.w).subtract(_dual.clone().scale(mReal.w)).subtract(_real.clone().cross(_dual)).scale(-2);

//		Vector trans = dual.multiply(2).multiply(real.clone().conjugate()).getVectorPart();
		mReal.transform(p);
		p.add(trans);

		return p;
	}


	public DualQuaternion conjugate()
	{
		mReal.conjugate();
		mDual.conjugate();
		return this;
	}


	public DualQuaternion multiply(DualQuaternion dq)
	{
		QuaternionNew _q0 = dq.mReal.clone().multiply(mReal);
		QuaternionNew _qe = dq.mDual.clone().multiply(mReal).add(dq.mReal.clone().multiply(mDual));

		mReal = _q0;
		mDual = _qe;

		return this;
	}


	public DualQuaternion multiply(double aScalar)
	{
		mReal.multiply(aScalar);
		mDual.multiply(aScalar);
		return this;
	}


	public DualQuaternion add(DualQuaternion dq)
	{
		mReal.add(dq.mReal);
		mDual.add(dq.mDual);
		return this;
	}


	public DualQuaternion normalize()
	{
		double scale = 1.0 / mReal.dot(mReal);
		mReal.multiply(scale);
		mDual.multiply(scale);

		return this;
	}


	public DualQuaternion invert()
	{
		double sqr_len_0 = mReal.dot(mReal);
		double sqr_len_e = mReal.dot(mDual) * 2.0;

		if (sqr_len_0 > 0.0)
		{
			double inv_sqr_len_0 = 1.0 / sqr_len_0;
			double inv_sqr_len_e = -sqr_len_e / (sqr_len_0 * sqr_len_0);

			DualQuaternion conj = clone().conjugate();
			mReal = conj.mReal.clone().multiply(inv_sqr_len_0);
			mDual = conj.mDual.multiply(inv_sqr_len_0).add(conj.mReal.multiply(inv_sqr_len_e));
		}
		else
		{
			mReal.multiply(0);
			mDual.multiply(0);
		}

		return this;
	}


	@Override
	public DualQuaternion clone()
	{
		return new DualQuaternion(mReal.clone(), mDual.clone());
	}


	private DualQuaternion setFromScrew(double inAngle, double inPitch, Vec3d inDir, Vec3d inMoment)
	{
		double sin_half_angle = Math.sin(inAngle * 0.5);
		double cos_half_angle = Math.cos(inAngle * 0.5);

		mReal.w = cos_half_angle;
		mReal.x = sin_half_angle * inDir.x;
		mReal.y = sin_half_angle * inDir.y;
		mReal.z = sin_half_angle * inDir.z;

		mDual.w = -inPitch * sin_half_angle * 0.5;
		mDual.x = sin_half_angle * inMoment.x + 0.5 * inPitch * cos_half_angle * inDir.x;
		mDual.y = sin_half_angle * inMoment.y + 0.5 * inPitch * cos_half_angle * inDir.y;
		mDual.z = sin_half_angle * inMoment.z + 0.5 * inPitch * cos_half_angle * inDir.z;

		return this;
	}


	public double dotQuat(QuaternionNew inLHS, QuaternionNew inRHS)
	{
		return inLHS.x * inRHS.x + inLHS.y * inRHS.y + inLHS.z * inRHS.z + inLHS.w * inRHS.w;
	}


	public boolean checkPlucker()
	{
		// Test for Plücker condition. Dot between real and dual part must be 0
		return Math.abs(dotQuat(mReal, mDual)) < 1e-5;
	}


	public boolean isUnit()
	{
		// Real must be unit and plucker condition must hold
		return Math.abs(dotQuat(mReal, mReal) - 1.0) < 1e-5 && checkPlucker();
	}


	public boolean hasRotation()
	{
		assert isUnit();
		return Math.abs(mReal.w) < 0.999999;
	}


	public boolean isPureTranslation()
	{
		return !hasRotation();
	}


	private void toScrew(double[] outAngle, double[] outPitch, Vec3d outDir, Vec3d outMoment)
	{
		// See if it's a pure translation:
		if (isPureTranslation())
		{
			outAngle[0] = 0.0;
			outDir.x = mDual.x;
			outDir.y = mDual.y;
			outDir.z = mDual.z;

			double dir_sq_len = outDir.x * outDir.x + outDir.y * outDir.y + outDir.z * outDir.z;

			// If a translation is nonzero, normalize is
			// else leave <outDir> zero vector (no motion at all)
			if (dir_sq_len > 1e-6)
			{
				double dir_len = Math.sqrt(dir_sq_len);
				outPitch[0] = 2.0 * dir_len;
				outDir.divide(dir_len);
			}
			else
			{
				outPitch[0] = 0.0;
			}

			// Moment can be arbitrary
			outMoment.set(0, 0, 0);
		}
		else
		{
			// Rigid transformation with a nonzero rotation
			outAngle[0] = 2.0 * Math.acos(mReal.w);

			double s = mReal.x * mReal.x + mReal.y * mReal.y + mReal.z * mReal.z;
			if (s < 1e-6)
			{
				outDir.set(0, 0, 0);
				outPitch[0] = 0.0;
				outMoment.set(0, 0, 0);
			}
			else
			{
				double oos = 1.0 / Math.sqrt(s);
				outDir.x = mReal.x * oos;
				outDir.y = mReal.y * oos;
				outDir.z = mReal.z * oos;

				outPitch[0] = -2.0 * mDual.w * oos;

				outMoment.x = mDual.x;
				outMoment.y = mDual.y;
				outMoment.z = mDual.z;

				outMoment.subtract(outDir.clone().scale(outPitch[0] * mReal.w * 0.5)).scale(oos);
			}
		}
	}


	public DualQuaternion log()
	{
		double[] angle = new double[1];
		double[] pitch = new double[1];
		Vec3d direction = new Vec3d();
		Vec3d moment = new Vec3d();

		toScrew(angle, pitch, direction, moment);

		DualQuaternion res = new DualQuaternion();
		res.mReal.x = direction.x * angle[0] * 0.5;
		res.mReal.y = direction.y * angle[0] * 0.5;
		res.mReal.z = direction.z * angle[0] * 0.5;
		res.mReal.w = 0.0;

		res.mDual.x = moment.x * angle[0] * 0.5 + direction.x * pitch[0] * 0.5;
		res.mDual.y = moment.y * angle[0] * 0.5 + direction.y * pitch[0] * 0.5;
		res.mDual.z = moment.z * angle[0] * 0.5 + direction.z * pitch[0] * 0.5;
		res.mDual.w = 0.0;

		return res;
	}


	public DualQuaternion exp()
	{
		DualQuaternion res = new DualQuaternion();
		Vec3d n = new Vec3d(mReal.x, mReal.y, mReal.z);

		double half_angle = n.length();

		// Pure translation?
		if (half_angle < 1e-5)
		{
			return new DualQuaternion(new QuaternionNew().identity(), mDual.clone());
		}

		// Get normalized dir
		Vec3d dir = n.clone().divide(half_angle);

		Vec3d d = new Vec3d(mDual.x, mDual.y, mDual.z);
		double half_pitch = d.dot(dir);
		Vec3d mom = d.subtract(dir.clone().scale(half_pitch)).divide(half_angle);

		return res.setFromScrew(half_angle * 2.0, half_pitch * 2.0, dir, mom);
	}


	public double dotReal(DualQuaternion inRHS)
	{
		return dotQuat(mReal, inRHS.mReal);
	}


	public double dotDual(DualQuaternion inRHS)
	{
		return dotQuat(mDual, inRHS.mDual);
	}


	public double dot(DualQuaternion inRHS)
	{
		return dotReal(inRHS) + dotDual(inRHS);
	}


	public DualQuaternion negateIt()
	{
		mReal.negate();
		mDual.negate();
		return this;
	}


	public QuaternionNew getReal()
	{
		return mReal;
	}


	public QuaternionNew getDual()
	{
		return mDual;
	}


	@Override
	public String toString()
	{
		return "{real=" + mReal + ", dual=" + mDual + "}";
	}
}
