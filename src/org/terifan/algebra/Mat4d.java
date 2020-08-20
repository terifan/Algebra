package org.terifan.algebra;


public class Mat4d implements Cloneable
{
	public double m00, m01, m02, m03;
	public double m10, m11, m12, m13;
	public double m20, m21, m22, m23;
	public double m30, m31, m32, m33;


	public Mat4d identity()
	{
		m01 = m02 = m03 = 0;
		m10 = m12 = m13 = 0;
		m20 = m21 = m23 = 0;
		m30 = m31 = m32 = 0;
		m00 = m11 = m22 = m33 = 1;
		return this;
	}


	public Mat4d setRow(int r, double x, double y, double z, double w)
	{
		switch (r)
		{
			case 0:
				m00 = x;
				m01 = y;
				m02 = z;
				m03 = w;
				break;
			case 1:
				m10 = x;
				m11 = y;
				m12 = z;
				m13 = w;
				break;
			case 2:
				m20 = x;
				m21 = y;
				m22 = z;
				m23 = w;
				break;
			case 3:
				m30 = x;
				m31 = y;
				m32 = z;
				m33 = w;
				break;
			default:
				throw new IllegalArgumentException();
		}

		return this;
	}


	public Vec3d transformPoint(Vec3d v)
	{
		Vec3d result = new Vec3d();

		result.x = m00 * v.x + m10 * v.y + m20 * v.z + m30;
		result.y = m01 * v.x + m11 * v.y + m21 * v.z + m31;
		result.z = m02 * v.x + m12 * v.y + m22 * v.z + m32;

		return result;
	}


	public static Mat4d inverse(Mat4d m)
	{
		Mat4d temp = new Mat4d();

		temp.m00 = m.m12 * m.m23 * m.m31 - m.m13 * m.m22 * m.m31 + m.m13 * m.m21 * m.m32 - m.m11 * m.m23 * m.m32 - m.m12 * m.m21 * m.m33 + m.m11 * m.m22 * m.m33;
		temp.m01 = m.m03 * m.m22 * m.m31 - m.m02 * m.m23 * m.m31 - m.m03 * m.m21 * m.m32 + m.m01 * m.m23 * m.m32 + m.m02 * m.m21 * m.m33 - m.m01 * m.m22 * m.m33;
		temp.m02 = m.m02 * m.m13 * m.m31 - m.m03 * m.m12 * m.m31 + m.m03 * m.m11 * m.m32 - m.m01 * m.m13 * m.m32 - m.m02 * m.m11 * m.m33 + m.m01 * m.m12 * m.m33;
		temp.m03 = m.m03 * m.m12 * m.m21 - m.m02 * m.m13 * m.m21 - m.m03 * m.m11 * m.m22 + m.m01 * m.m13 * m.m22 + m.m02 * m.m11 * m.m23 - m.m01 * m.m12 * m.m23;
		temp.m10 = m.m13 * m.m22 * m.m30 - m.m12 * m.m23 * m.m30 - m.m13 * m.m20 * m.m32 + m.m10 * m.m23 * m.m32 + m.m12 * m.m20 * m.m33 - m.m10 * m.m22 * m.m33;
		temp.m11 = m.m02 * m.m23 * m.m30 - m.m03 * m.m22 * m.m30 + m.m03 * m.m20 * m.m32 - m.m00 * m.m23 * m.m32 - m.m02 * m.m20 * m.m33 + m.m00 * m.m22 * m.m33;
		temp.m12 = m.m03 * m.m12 * m.m30 - m.m02 * m.m13 * m.m30 - m.m03 * m.m10 * m.m32 + m.m00 * m.m13 * m.m32 + m.m02 * m.m10 * m.m33 - m.m00 * m.m12 * m.m33;
		temp.m13 = m.m02 * m.m13 * m.m20 - m.m03 * m.m12 * m.m20 + m.m03 * m.m10 * m.m22 - m.m00 * m.m13 * m.m22 - m.m02 * m.m10 * m.m23 + m.m00 * m.m12 * m.m23;
		temp.m20 = m.m11 * m.m23 * m.m30 - m.m13 * m.m21 * m.m30 + m.m13 * m.m20 * m.m31 - m.m10 * m.m23 * m.m31 - m.m11 * m.m20 * m.m33 + m.m10 * m.m21 * m.m33;
		temp.m21 = m.m03 * m.m21 * m.m30 - m.m01 * m.m23 * m.m30 - m.m03 * m.m20 * m.m31 + m.m00 * m.m23 * m.m31 + m.m01 * m.m20 * m.m33 - m.m00 * m.m21 * m.m33;
		temp.m22 = m.m01 * m.m13 * m.m30 - m.m03 * m.m11 * m.m30 + m.m03 * m.m10 * m.m31 - m.m00 * m.m13 * m.m31 - m.m01 * m.m10 * m.m33 + m.m00 * m.m11 * m.m33;
		temp.m23 = m.m03 * m.m11 * m.m20 - m.m01 * m.m13 * m.m20 - m.m03 * m.m10 * m.m21 + m.m00 * m.m13 * m.m21 + m.m01 * m.m10 * m.m23 - m.m00 * m.m11 * m.m23;
		temp.m30 = m.m12 * m.m21 * m.m30 - m.m11 * m.m22 * m.m30 - m.m12 * m.m20 * m.m31 + m.m10 * m.m22 * m.m31 + m.m11 * m.m20 * m.m32 - m.m10 * m.m21 * m.m32;
		temp.m31 = m.m01 * m.m22 * m.m30 - m.m02 * m.m21 * m.m30 + m.m02 * m.m20 * m.m31 - m.m00 * m.m22 * m.m31 - m.m01 * m.m20 * m.m32 + m.m00 * m.m21 * m.m32;
		temp.m32 = m.m02 * m.m11 * m.m30 - m.m01 * m.m12 * m.m30 - m.m02 * m.m10 * m.m31 + m.m00 * m.m12 * m.m31 + m.m01 * m.m10 * m.m32 - m.m00 * m.m11 * m.m32;
		temp.m33 = m.m01 * m.m12 * m.m20 - m.m02 * m.m11 * m.m20 + m.m02 * m.m10 * m.m21 - m.m00 * m.m12 * m.m21 - m.m01 * m.m10 * m.m22 + m.m00 * m.m11 * m.m22;

		// Get the determinant of this matrix
		double determinant = temp.determinant();

		// Each property of the inverse matrix is multiplied by 1.0 divided by the determinant.
		// As we cannot divide by zero, we will throw an IllegalArgumentException if the determinant is zero.
		if (Double.compare(determinant, 0.0) == 0)
		{
			throw new IllegalArgumentException("Cannot invert a matrix with a determinant of zero.");
		}

		// Otherwise, calculate the value of one over the determinant and scale the matrix by that value
		double oneOverDeterminant = 1.0 / temp.determinant();

		temp.m00 *= oneOverDeterminant;
		temp.m01 *= oneOverDeterminant;
		temp.m02 *= oneOverDeterminant;
		temp.m03 *= oneOverDeterminant;
		temp.m10 *= oneOverDeterminant;
		temp.m11 *= oneOverDeterminant;
		temp.m12 *= oneOverDeterminant;
		temp.m13 *= oneOverDeterminant;
		temp.m20 *= oneOverDeterminant;
		temp.m21 *= oneOverDeterminant;
		temp.m22 *= oneOverDeterminant;
		temp.m23 *= oneOverDeterminant;
		temp.m30 *= oneOverDeterminant;
		temp.m31 *= oneOverDeterminant;
		temp.m32 *= oneOverDeterminant;
		temp.m33 *= oneOverDeterminant;

		// Finally, return the inverted matrix
		return temp;
	}


	public double determinant()
	{
		return m03 * m12 * m21 * m30 - m02 * m13 * m21 * m30 - m03 * m11 * m22 * m30 + m01 * m13 * m22 * m30
			+ m02 * m11 * m23 * m30 - m01 * m12 * m23 * m30 - m03 * m12 * m20 * m31 + m02 * m13 * m20 * m31
			+ m03 * m10 * m22 * m31 - m00 * m13 * m22 * m31 - m02 * m10 * m23 * m31 + m00 * m12 * m23 * m31
			+ m03 * m11 * m20 * m32 - m01 * m13 * m20 * m32 - m03 * m10 * m21 * m32 + m00 * m13 * m21 * m32
			+ m01 * m10 * m23 * m32 - m00 * m11 * m23 * m32 - m02 * m11 * m20 * m33 + m01 * m12 * m20 * m33
			+ m02 * m10 * m21 * m33 - m00 * m12 * m21 * m33 - m01 * m10 * m22 * m33 + m00 * m11 * m22 * m33;
	}


	public static Mat4d perspective(double aFov, double aNear, double aFar)
	{
		// Camera points towards -z.  0 < near < far.
		// Matrix maps z range [-near, -far] to [-1, 1], after homogeneous division.
		double f = 1.0 / (Math.tan(aFov * Math.PI / 360.0));
		double d = 1.0 / (aNear - aFar);

		Mat4d r = new Mat4d();
		r.m00 = f;
		r.m01 = 0;
		r.m02 = 0;
		r.m03 = 0;
		r.m10 = 0;
		r.m11 = -f;
		r.m12 = 0;
		r.m13 = 0;
		r.m20 = 0;
		r.m21 = 0;
		r.m22 = (aNear + aFar) * d;
		r.m23 = 2 * aNear * aFar * d;
		r.m30 = 0;
		r.m31 = 0;
		r.m32 = -1;
		r.m33 = 0;

		return r;
	}


	public Mat4d set(Mat4d aOther)
	{
		m00 = aOther.m00;
		m01 = aOther.m01;
		m02 = aOther.m02;
		m03 = aOther.m03;
		m10 = aOther.m10;
		m11 = aOther.m11;
		m12 = aOther.m12;
		m13 = aOther.m13;
		m20 = aOther.m20;
		m21 = aOther.m21;
		m22 = aOther.m22;
		m23 = aOther.m23;
		m30 = aOther.m30;
		m31 = aOther.m31;
		m32 = aOther.m32;
		m33 = aOther.m33;
		return this;
	}


	public Mat4d setScale(double sx, double sy, double sz)
	{
		identity();
		m00 = sx;
		m11 = sy;
		m22 = sz;
		m33 = 1;
		return this;
	}


	public Mat4d setTranslation(double x, double y, double z)
	{
		identity();
		m03 = x;
		m13 = y;
		m23 = z;
		return this;
	}


	public Mat4d setTranslation(Vec4d v)
	{
		identity();
		m03 = v.x;
		m13 = v.y;
		m23 = v.z;
		return this;
	}


	public Mat4d setTranslation(Vec3d v)
	{
		identity();
		m03 = v.x;
		m13 = v.y;
		m23 = v.z;
		return this;
	}


	public Mat4d setRotationX(double angle)
	{
		identity();
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		m11 = c;
		m12 = -s;
		m21 = s;
		m22 = c;
		return this;
	}


	public Mat4d setRotationY(double angle)
	{
		identity();
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		m00 = c;
		m02 = s;
		m20 = -s;
		m22 = c;
		return this;
	}


	public Mat4d setRotationZ(double angle)
	{
		identity();
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		m00 = c;
		m01 = -s;
		m10 = s;
		m11 = c;
		return this;
	}


	public Mat4d multiply(Mat4d m)
	{
		double nm00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30;
		double nm01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31;
		double nm02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32;
		double nm03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33;
		double nm10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30;
		double nm11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31;
		double nm12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32;
		double nm13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33;
		double nm20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30;
		double nm21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31;
		double nm22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32;
		double nm23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33;
		double nm30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30;
		double nm31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31;
		double nm32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32;
		double nm33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33;
		m00 = nm00;
		m01 = nm01;
		m02 = nm02;
		m03 = nm03;
		m10 = nm10;
		m11 = nm11;
		m12 = nm12;
		m13 = nm13;
		m20 = nm20;
		m21 = nm21;
		m22 = nm22;
		m23 = nm23;
		m30 = nm30;
		m31 = nm31;
		m32 = nm32;
		m33 = nm33;
		return this;
	}


	public Mat4d multiply(Vec4d v)
	{
		double nx = m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w;
		double ny = m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w;
		double nz = m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w;
		double nw = m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w;
		v.x = nx;
		v.y = ny;
		v.z = nz;
		v.w = nw;
		return this;
	}


	public Mat4d translate(Vec3d v)
	{
		this.m30 += this.m00 * v.x + this.m10 * v.y + this.m20 * v.z;
		this.m31 += this.m01 * v.x + this.m11 * v.y + this.m21 * v.z;
		this.m32 += this.m02 * v.x + this.m12 * v.y + this.m22 * v.z;
		this.m33 += this.m03 * v.x + this.m13 * v.y + this.m23 * v.z;

		return this;
	}


	@Override
	public Mat4d clone()
	{
		Mat4d m = new Mat4d();
		m.set(this);
		return m;
	}


	@Override
	public String toString()
	{
		String s = "";
		s += "{" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + "}";
		s += "{" + m10 + ", " + m11 + ", " + m12 + ", " + m13 + "}";
		s += "{" + m20 + ", " + m21 + ", " + m22 + ", " + m23 + "}";
		s += "{" + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "}";
		return "{" + s + "}";
	}
}
