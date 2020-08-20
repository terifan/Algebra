package org.terifan.algebra;


/**
 * Implementation of Dual Quaternion
 *
 * http://wscg.zcu.cz/wscg2012/short/A29-full.pdf
 * http://rodolphe-vaillant.fr/?e=29
 */
public class DualQuaternion implements Cloneable
{
	private QuaternionNew qblend_0;
	private QuaternionNew qblend_e;


	public DualQuaternion()
	{
		this(new QuaternionNew(0.0, 0.0, 0.0, 0.0), new QuaternionNew(0.0, 0.0, 0.0, 0.0));
	}


	public DualQuaternion(QuaternionNew aReal, QuaternionNew aDual)
	{
		qblend_0 = aReal;
		qblend_e = aDual;
	}


	public DualQuaternion(QuaternionNew aQuaternion, Vec3d aTrans)
	{
		qblend_0 = aQuaternion.clone().normalize();
		qblend_e = new QuaternionNew(0, aTrans.x, aTrans.y, aTrans.z).multiply(qblend_0).multiply(0.5);
	}


	/**
	 * Note: this DualQuaternion must be normalized before calling this
	 */
	public Vec3d transform(Vec3d p)
	{
		Vec3d v0 = qblend_0.getVectorPart();
		Vec3d ve = qblend_e.getVectorPart();
		Vec3d trans = ve.clone().scale(qblend_0.w).subtract(v0.clone().scale(qblend_e.w)).add(v0.clone().cross(ve)).scale(2); // (ve*qblend_0.w() - v0*qblend_e.w() + v0.cross(ve)) * 2.f;

		return qblend_0.rotate(p).add(trans);
	}


	public Vec3d rotate(Vec3d v)
    {
        QuaternionNew tmp = qblend_0;
        tmp.normalize();
        return tmp.rotate(v);
    }


	public DualQuaternion conjugate()
	{
		qblend_0.conjugate();
		qblend_e.conjugate();
		return this;
	}


	public DualQuaternion multiply(DualQuaternion dq)
	{
		QuaternionNew _q0 = dq.qblend_0.clone().multiply(qblend_0);
		QuaternionNew _qe = dq.qblend_e.clone().multiply(qblend_0).add(dq.qblend_0.clone().multiply(qblend_e));

		qblend_0 = _q0;
		qblend_e = _qe;

		return this;
	}


	public DualQuaternion multiply(double aScalar)
	{
		qblend_0.multiply(aScalar);
		qblend_e.multiply(aScalar);
		return this;
	}


	public DualQuaternion add(DualQuaternion dq)
	{
		qblend_0.add(dq.qblend_0);
		qblend_e.add(dq.qblend_e);
		return this;
	}


	public DualQuaternion normalize()
	{
		double scale = 1.0 / qblend_0.dot(qblend_0);
		qblend_0.multiply(scale);
		qblend_e.multiply(scale);

		return this;
	}


	public DualQuaternion invert()
	{
		double sqr_len_0 = qblend_0.dot(qblend_0);
		double sqr_len_e = qblend_0.dot(qblend_e) * 2.0;

		if (sqr_len_0 > 0.0)
		{
			double inv_sqr_len_0 = 1.0 / sqr_len_0;
			double inv_sqr_len_e = -sqr_len_e / (sqr_len_0 * sqr_len_0);

			DualQuaternion conj = clone().conjugate();
			qblend_0 = conj.qblend_0.clone().multiply(inv_sqr_len_0);
			qblend_e = conj.qblend_e.multiply(inv_sqr_len_0).add(conj.qblend_0.multiply(inv_sqr_len_e));
		}
		else
		{
			qblend_0.multiply(0);
			qblend_e.multiply(0);
		}

		return this;
	}


	@Override
	public DualQuaternion clone()
	{
		return new DualQuaternion(qblend_0.clone(), qblend_e.clone());
	}


	private DualQuaternion setFromScrew(double inAngle, double inPitch, Vec3d inDir, Vec3d inMoment)
	{
		double sin_half_angle = Math.sin(inAngle * 0.5);
		double cos_half_angle = Math.cos(inAngle * 0.5);

		qblend_0.w = cos_half_angle;
		qblend_0.x = sin_half_angle * inDir.x;
		qblend_0.y = sin_half_angle * inDir.y;
		qblend_0.z = sin_half_angle * inDir.z;

		qblend_e.w = -inPitch * sin_half_angle * 0.5;
		qblend_e.x = sin_half_angle * inMoment.x + 0.5 * inPitch * cos_half_angle * inDir.x;
		qblend_e.y = sin_half_angle * inMoment.y + 0.5 * inPitch * cos_half_angle * inDir.y;
		qblend_e.z = sin_half_angle * inMoment.z + 0.5 * inPitch * cos_half_angle * inDir.z;

		return this;
	}


	public double dotQuat(QuaternionNew inLHS, QuaternionNew inRHS)
	{
		return inLHS.x * inRHS.x + inLHS.y * inRHS.y + inLHS.z * inRHS.z + inLHS.w * inRHS.w;
	}


	public boolean checkPlucker()
	{
		// Test for Plücker condition. Dot between real and dual part must be 0
		return Math.abs(dotQuat(qblend_0, qblend_e)) < 1e-5;
	}


	public boolean isUnit()
	{
		// Real must be unit and plucker condition must hold
		return Math.abs(dotQuat(qblend_0, qblend_0) - 1.0) < 1e-5 && checkPlucker();
	}


	public boolean hasRotation()
	{
		assert isUnit();
		return Math.abs(qblend_0.w) < 0.999999;
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
			outDir.x = qblend_e.x;
			outDir.y = qblend_e.y;
			outDir.z = qblend_e.z;

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
			outAngle[0] = 2.0 * Math.acos(qblend_0.w);

			double s = qblend_0.x * qblend_0.x + qblend_0.y * qblend_0.y + qblend_0.z * qblend_0.z;
			if (s < 1e-6)
			{
				outDir.set(0, 0, 0);
				outPitch[0] = 0.0;
				outMoment.set(0, 0, 0);
			}
			else
			{
				double oos = 1.0 / Math.sqrt(s);
				outDir.x = qblend_0.x * oos;
				outDir.y = qblend_0.y * oos;
				outDir.z = qblend_0.z * oos;

				outPitch[0] = -2.0 * qblend_e.w * oos;

				outMoment.x = qblend_e.x;
				outMoment.y = qblend_e.y;
				outMoment.z = qblend_e.z;

				outMoment.subtract(outDir.clone().scale(outPitch[0] * qblend_0.w * 0.5)).scale(oos);
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
		res.qblend_0.x = direction.x * angle[0] * 0.5;
		res.qblend_0.y = direction.y * angle[0] * 0.5;
		res.qblend_0.z = direction.z * angle[0] * 0.5;
		res.qblend_0.w = 0.0;

		res.qblend_e.x = moment.x * angle[0] * 0.5 + direction.x * pitch[0] * 0.5;
		res.qblend_e.y = moment.y * angle[0] * 0.5 + direction.y * pitch[0] * 0.5;
		res.qblend_e.z = moment.z * angle[0] * 0.5 + direction.z * pitch[0] * 0.5;
		res.qblend_e.w = 0.0;

		return res;
	}


	public DualQuaternion exp()
	{
		DualQuaternion res = new DualQuaternion();
		Vec3d n = new Vec3d(qblend_0.x, qblend_0.y, qblend_0.z);

		double half_angle = n.length();

		// Pure translation?
		if (half_angle < 1e-5)
		{
			return new DualQuaternion(new QuaternionNew().identity(), qblend_e.clone());
		}

		// Get normalized dir
		Vec3d dir = n.clone().divide(half_angle);

		Vec3d d = new Vec3d(qblend_e.x, qblend_e.y, qblend_e.z);
		double half_pitch = d.dot(dir);
		Vec3d mom = d.subtract(dir.clone().scale(half_pitch)).divide(half_angle);

		return res.setFromScrew(half_angle * 2.0, half_pitch * 2.0, dir, mom);
	}


	public double dotReal(DualQuaternion inRHS)
	{
		return dotQuat(qblend_0, inRHS.qblend_0);
	}


	public double dotDual(DualQuaternion inRHS)
	{
		return dotQuat(qblend_e, inRHS.qblend_e);
	}


	public double dot(DualQuaternion inRHS)
	{
		return dotReal(inRHS) + dotDual(inRHS);
	}


	public DualQuaternion negateIt()
	{
		qblend_0.negate();
		qblend_e.negate();
		return this;
	}


	public QuaternionNew getReal()
	{
		return qblend_0;
	}


	public QuaternionNew getDual()
	{
		return qblend_e;
	}


	@Override
	public String toString()
	{
		return "{real=" + qblend_0 + ", dual=" + qblend_e + "}";
	}
}
