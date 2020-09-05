package org.terifan.algebra;


/**
 * Implementation of Dual Quaternion
 *
 * http://wscg.zcu.cz/wscg2012/short/A29-full.pdf
 * http://rodolphe-vaillant.fr/?e=29
 */
public class DualQuaternion implements Cloneable
{
	private QuaternionNew m_real;
	private QuaternionNew m_dual;


	public DualQuaternion()
	{
		this(new QuaternionNew(), new QuaternionNew());
	}


	public DualQuaternion(QuaternionNew aReal, QuaternionNew aDual)
	{
		m_real = aReal;
		m_dual = aDual;
	}


	public DualQuaternion(QuaternionNew aQuaternion, Vec3d aOrigin)
	{
		m_real = aQuaternion;
		m_dual = new QuaternionNew(aOrigin.x, aOrigin.y, aOrigin.z, 0).multiply(0.5).multiply(m_real);
	}


	public DualQuaternion(Mat4d aRotTransMatrix)
	{
		this(QuaternionNew.createFromRotationMatrix(aRotTransMatrix.toMat3d()), aRotTransMatrix.getOrigin());
	}


	/**
	 * Note: this DualQuaternion must be normalized before calling this
	 */
	public Vec3d transform(Vec3d p)
	{
		Vec3d v0 = m_real.getVectorPart();
		Vec3d ve = m_dual.getVectorPart();

		Vec3d trans = ve.clone().multiply(m_real.w).subtract(v0.clone().multiply(m_dual.w)).add(v0.clone().cross(ve)).multiply(2);

		return m_real.rotate(p).add(trans);
	}


	public Vec3d rotate(Vec3d v)
    {
        QuaternionNew tmp = m_real;
        tmp.normalize();
        return tmp.rotate(v);
    }


	public DualQuaternion conjugate()
	{
		m_real.conjugate();
		m_dual.conjugate();
		return this;
	}


	public DualQuaternion multiply(DualQuaternion dq)
	{
		QuaternionNew _q0 = dq.m_real.clone().multiply(m_real);
		QuaternionNew _qe = dq.m_dual.clone().multiply(m_real).add(dq.m_real.clone().multiply(m_dual));

		m_real = _q0;
		m_dual = _qe;

		return this;
	}


	public DualQuaternion multiply(double aScalar)
	{
		m_real.multiply(aScalar);
		m_dual.multiply(aScalar);
		return this;
	}


	public DualQuaternion add(DualQuaternion dq)
	{
		m_real.add(dq.m_real);
		m_dual.add(dq.m_dual);
		return this;
	}


	public DualQuaternion normalize()
	{
		double scale = 1.0 / m_real.dot(m_real);
		m_real.multiply(scale);
		m_dual.multiply(scale);

		return this;
	}


	public DualQuaternion invert()
	{
		double sqr_len_0 = m_real.dot(m_real);
		double sqr_len_e = m_real.dot(m_dual) * 2.0;

		if (sqr_len_0 > 0.0)
		{
			double inv_sqr_len_0 = 1.0 / sqr_len_0;
			double inv_sqr_len_e = -sqr_len_e / (sqr_len_0 * sqr_len_0);

			DualQuaternion conj = clone().conjugate();
			m_real = conj.m_real.clone().multiply(inv_sqr_len_0);
			m_dual = conj.m_dual.multiply(inv_sqr_len_0).add(conj.m_real.multiply(inv_sqr_len_e));
		}
		else
		{
			m_real.multiply(0);
			m_dual.multiply(0);
		}

		return this;
	}


	@Override
	public DualQuaternion clone()
	{
		return new DualQuaternion(m_real.clone(), m_dual.clone());
	}


	private DualQuaternion setFromScrew(double inAngle, double inPitch, Vec3d inDir, Vec3d inMoment)
	{
		double sin_half_angle = Math.sin(inAngle * 0.5);
		double cos_half_angle = Math.cos(inAngle * 0.5);

		m_real.w = cos_half_angle;
		m_real.x = sin_half_angle * inDir.x;
		m_real.y = sin_half_angle * inDir.y;
		m_real.z = sin_half_angle * inDir.z;

		m_dual.w = -inPitch * sin_half_angle * 0.5;
		m_dual.x = sin_half_angle * inMoment.x + 0.5 * inPitch * cos_half_angle * inDir.x;
		m_dual.y = sin_half_angle * inMoment.y + 0.5 * inPitch * cos_half_angle * inDir.y;
		m_dual.z = sin_half_angle * inMoment.z + 0.5 * inPitch * cos_half_angle * inDir.z;

		return this;
	}


	public double dotQuat(QuaternionNew inLHS, QuaternionNew inRHS)
	{
		return inLHS.x * inRHS.x + inLHS.y * inRHS.y + inLHS.z * inRHS.z + inLHS.w * inRHS.w;
	}


	public boolean checkPlucker()
	{
		// Test for Plücker condition. Dot between real and dual part must be 0
		return Math.abs(dotQuat(m_real, m_dual)) < 1e-5;
	}


	public boolean isUnit()
	{
		// Real must be unit and Plücker condition must hold
		return Math.abs(dotQuat(m_real, m_real) - 1.0) < 1e-5 && checkPlucker();
	}


	public boolean hasRotation()
	{
		assert isUnit();
		return Math.abs(m_real.w) < 0.999999;
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
			outDir.x = m_dual.x;
			outDir.y = m_dual.y;
			outDir.z = m_dual.z;

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
			outAngle[0] = 2.0 * Math.acos(m_real.w);

			double s = m_real.x * m_real.x + m_real.y * m_real.y + m_real.z * m_real.z;
			if (s < 1e-6)
			{
				outDir.set(0, 0, 0);
				outPitch[0] = 0.0;
				outMoment.set(0, 0, 0);
			}
			else
			{
				double oos = 1.0 / Math.sqrt(s);
				outDir.x = m_real.x * oos;
				outDir.y = m_real.y * oos;
				outDir.z = m_real.z * oos;

				outPitch[0] = -2.0 * m_dual.w * oos;

				outMoment.x = m_dual.x;
				outMoment.y = m_dual.y;
				outMoment.z = m_dual.z;

				outMoment.subtract(outDir.clone().multiply(outPitch[0] * m_real.w * 0.5)).multiply(oos);
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
		res.m_real.x = direction.x * angle[0] * 0.5;
		res.m_real.y = direction.y * angle[0] * 0.5;
		res.m_real.z = direction.z * angle[0] * 0.5;
		res.m_real.w = 0.0;

		res.m_dual.x = moment.x * angle[0] * 0.5 + direction.x * pitch[0] * 0.5;
		res.m_dual.y = moment.y * angle[0] * 0.5 + direction.y * pitch[0] * 0.5;
		res.m_dual.z = moment.z * angle[0] * 0.5 + direction.z * pitch[0] * 0.5;
		res.m_dual.w = 0.0;

		return res;
	}


	public DualQuaternion exp()
	{
		DualQuaternion res = new DualQuaternion();
		Vec3d n = new Vec3d(m_real.x, m_real.y, m_real.z);

		double half_angle = n.length();

		// Pure translation?
		if (half_angle < 1e-5)
		{
			return new DualQuaternion(new QuaternionNew().identity(), m_dual.clone());
		}

		// Get normalized dir
		Vec3d dir = n.clone().divide(half_angle);

		Vec3d d = new Vec3d(m_dual.x, m_dual.y, m_dual.z);
		double half_pitch = d.dot(dir);
		Vec3d mom = d.subtract(dir.clone().multiply(half_pitch)).divide(half_angle);

		return res.setFromScrew(half_angle * 2.0, half_pitch * 2.0, dir, mom);
	}


	public double dotReal(DualQuaternion inRHS)
	{
		return dotQuat(m_real, inRHS.m_real);
	}


	public double dotDual(DualQuaternion inRHS)
	{
		return dotQuat(m_dual, inRHS.m_dual);
	}


	public double dot(DualQuaternion inRHS)
	{
		return dotReal(inRHS) + dotDual(inRHS);
	}


	public DualQuaternion negateIt()
	{
		m_real.negate();
		m_dual.negate();
		return this;
	}


	public QuaternionNew getReal()
	{
		return m_real;
	}


	public QuaternionNew getDual()
	{
		return m_dual;
	}


	// https://xbdev.net/misc_demos/demos/dual_quaternions_beyond/paper.pdf
	public DualQuaternion lerp(DualQuaternion from, DualQuaternion to, double t)
	{
		// Shortest path
		double dot = from.m_real.dot(to.m_real);
		if (dot < 0)
		{
			to = to.multiply(-1);
		}

		// ScLERP = qa(qa^-1 qb)^t
		DualQuaternion diff = from.conjugate().multiply(to);
		Vec3d vr = diff.m_real.getVectorPart();
		Vec3d vd = diff.m_dual.getVectorPart();
		double invr = 1 / Math.sqrt(vr.dot(vr));

		// Screw parameters
		double angle = 2 * Math.acos(diff.m_real.w);
		double pitch = -2 * diff.m_dual.w * invr;
		Vec3d direction = vr.clone().multiply(invr);
		Vec3d moment = vd.subtract(direction.clone().multiply(pitch * diff.m_real.w * 0.5)).multiply(invr);

		// Exponential power
		angle *= t;
		pitch *= t;

		// Convert back to dual-quaternion
		double sinAngle = Math.sin(0.5 * angle);
		double cosAngle = Math.cos(0.5 * angle);
		Vec3d d = direction.clone().multiply(sinAngle);
		Vec3d e = moment.clone().multiply(sinAngle).add(direction.clone().multiply(pitch * 0.5 * cosAngle));
		QuaternionNew real = new QuaternionNew(d.x, d.y, d.z, cosAngle);
		QuaternionNew dual = new QuaternionNew(e.x, e.y, e.z, -pitch * 0.5 * sinAngle);

		// Complete the multiplication and return the interpolated value
		return from.multiply(new DualQuaternion(real, dual));
	}


	// https://xbdev.net/misc_demos/demos/dual_quaternions_beyond/paper.pdf
	public Mat4d toMat4d()
	{
		DualQuaternion q = clone().normalize();

		Mat4d m = new Mat4d().identity();
		double w = q.m_real.w;
		double x = q.m_real.x;
		double y = q.m_real.y;
		double z = q.m_real.z;

		// Extract rotational information
		m.m00 = w * w + x * x - y * y - z * z;
		m.m01 = 2 * x * y + 2 * w * z;
		m.m02 = 2 * x * z - 2 * w * y;
		m.m10 = 2 * x * y - 2 * w * z;
		m.m11 = w * w + y * y - x * x - z * z;
		m.m12 = 2 * y * z + 2 * w * x;
		m.m20 = 2 * x * z + 2 * w * y;
		m.m21 = 2 * y * z - 2 * w * x;
		m.m22 = w * w + z * z - x * x - y * y;

		// Extract translation information
		QuaternionNew t = q.m_dual.clone().multiply(q.m_real.clone().conjugate().multiply(2.0));
		m.m30 = t.x;
		m.m31 = t.y;
		m.m32 = t.z;

		return m;
	}


	@Override
	public String toString()
	{
		return "{real=" + m_real + ", dual=" + m_dual + "}";
	}
}
