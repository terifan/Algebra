package org.terifan.algebra;

import java.io.Serializable;


/**
 * Column-major order matrix, properties are publicly available as: <code>m&lt;row&gt;&lt;column&gt;</code>.
 * <p/>
 * The origin is stored in the matrix properties m30 (x location), m31 (y location) and m32 (z location).
 */
public class Mat4d implements Cloneable, Serializable
{
	private final static long serialVersionUID = 1L;

	private final static double DEGS_TO_RADS = Math.PI / 180.0;
	private final static double DOUBLE_EQUALITY_TOLERANCE = 0.0000001;

	public double m00, m01, m02, m03; // First  row - x-axis
	public double m10, m11, m12, m13; // Second row - y-axis
	public double m20, m21, m22, m23; // Third  row - z-axis
	public double m30, m31, m32, m33; // Fourth row - origin


	public Mat4d()
	{
	}


	public Mat4d(Mat3d rotationMatrix, Vec3d origin)
	{
		m00 = rotationMatrix.m00;
		m01 = rotationMatrix.m01;
		m02 = rotationMatrix.m02;
		m03 = 0.0;

		m10 = rotationMatrix.m10;
		m11 = rotationMatrix.m11;
		m12 = rotationMatrix.m12;
		m13 = 0.0;

		m20 = rotationMatrix.m20;
		m21 = rotationMatrix.m21;
		m22 = rotationMatrix.m22;
		m23 = 0.0;

		m30 = origin.x;
		m31 = origin.y;
		m32 = origin.z;
		m33 = 1.0;
	}


	/**
	 * Sets the provided value diagonally across the matrix. For example, to create an identity matrix with 1.0 across the diagonal then you
	 */
	public Mat4d(double value)
	{
		m00 = m11 = m22 = m33 = value;
	}


	/**
	 * @return this matrix.
	 */
	public Mat4d set(Mat4d aSource)
	{
		m00 = aSource.m00;
		m01 = aSource.m01;
		m02 = aSource.m02;
		m03 = aSource.m03;
		m10 = aSource.m10;
		m11 = aSource.m11;
		m12 = aSource.m12;
		m13 = aSource.m13;
		m20 = aSource.m20;
		m21 = aSource.m21;
		m22 = aSource.m22;
		m23 = aSource.m23;
		m30 = aSource.m30;
		m31 = aSource.m31;
		m32 = aSource.m32;
		m33 = aSource.m33;
		return this;
	}


	/**
	 * @return this matrix.
	 */
	public Mat4d set(double a00, double a01, double a02, double a03, double a10, double a11, double a12, double a13, double a20, double a21, double a22, double a23, double a30, double a31, double a32, double a33)
	{
		m00 = a00;
		m01 = a01;
		m02 = a02;
		m03 = a03;
		m10 = a10;
		m11 = a11;
		m12 = a12;
		m13 = a13;
		m20 = a20;
		m21 = a21;
		m22 = a22;
		m23 = a23;
		m30 = a30;
		m31 = a31;
		m32 = a32;
		m33 = a33;
		return this;
	}


	/**
	 * Zero all properties of a matrix.
	 *
	 * @return this matrix.
	 */
	public Mat4d zero()
	{
		m00 = m01 = m02 = m03 = m10 = m11 = m12 = m13 = m20 = m21 = m22 = m23 = m30 = m31 = m32 = m33 = 0.0;
		return this;
	}


	/**
	 * Reset a matrix to the identity matrix.
	 *
	 * @return this matrix.
	 */
	public Mat4d identity()
	{
		return set(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}


	/**
	 * Set the details of this Mat4d from an array of 16 doubles.
	 * <p>
	 * The matrix is set a column at a time, so the first four doubles from the source array are set on m0 to m03, the next four on m10 to
	 * m13 and so on.
	 * <p>
	 *
	 * @return this matrix.
	 */
	public Mat4d setFromArray(double[] source)
	{
		if (source.length != 16)
		{
			throw new IllegalArgumentException("Source array must contain precisely 16 doubles.");
		}

		// First column (x-axis)
		m00 = source[0];
		m01 = source[1];
		m02 = source[2];
		m03 = source[3];

		// Second column (y-axis)
		m10 = source[4];
		m11 = source[5];
		m12 = source[6];
		m13 = source[7];

		// Third column (z-axis)
		m20 = source[8];
		m21 = source[9];
		m22 = source[10];
		m23 = source[11];

		// Fourth column (origin)
		m30 = source[12];
		m31 = source[13];
		m32 = source[14];
		m33 = source[15];

		return this;
	}


	/**
	 * Return the x-basis of this Mat4d as a Vec3d.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02 properties.
	 *
	 * @return	A Vec3d containing the x-basis of this Mat4d.
	 */
	public Vec3d getXBasis()
	{
		return new Vec3d(m00, m01, m02);
	}


	/**
	 * Return the x-basis of this Mat4d as an array of three doubles.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02 properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not want to use the {@link Vec3d} class.
	 *
	 * @return	An array of three doubles containing the x-basis of this Mat4d.
	 */
	public double[] getXBasisArray()
	{
		return new double[]
		{
			m00, m01, m02
		};
	}


	/**
	 * Set the x-basis of this Mat4d from a provided Vec3d.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02 properties.
	 * <p>
	 * To ensure the legality of the x-basis, the provided Vec3d is normalised if required before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a rotation matrix then you should avoid the
	 * setXBasis/setYBasis/setZBasis methods and instead set the matrix properties via other methods which accept a double array and do not
	 * attempt to enforce rotation matrix legality such as {@link #setFromArray(double[])} and {@link #Mat4d(double[])}.
	 *
	 * @param	v	The Vec3d holding the x-basis to set.
	 * @return this matrix.
	 */
	public Mat4d setXBasis(Vec3d v)
	{
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		m00 = v.x;
		m01 = v.y;
		m02 = v.z;
		return this;
	}


	/**
	 * Set the x-basis of this Mat4d from a provided array of three doubles.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02 properties.
	 * <p>
	 * To ensure the legality of the x-basis, the provided array is converted into a Vec3d and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a rotation matrix then you should avoid the
	 * setXBasis/setYBasis/setZBasis methods and instead set the matrix properties via other methods which do not enforce normalisation such
	 * as the {@link #setFromArray(double[])} and {@link #Mat4d(double[])} methods.
	 *
	 * @param	f	The array of three doubles to set as the x-basis of this Mat4d.
	 * @return this matrix.
	 */
	public Mat4d setXBasis(double[] f)
	{
		if (f.length != 3)
		{
			throw new IllegalArgumentException();
		}

		Vec3d v = new Vec3d(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		// Set the x-basis
		m00 = v.x;
		m01 = v.y;
		m02 = v.z;
		return this;
	}


	/**
	 * Return the y-basis of this Mat4d as a Vec3d.
	 * <p>
	 * The y-basis is the orientation of theyx-axis, as held by the m10, m11 and m12 properties.
	 *
	 * @return	A Vec3d containing the y-basis of this Mat4d.
	 */
	public Vec3d getYBasis()
	{
		return new Vec3d(m10, m11, m12);
	}


	/**
	 * Return the y-basis of this Mat4d as an array of three doubles.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12 properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not want to use the {@link Vec3d} class.
	 *
	 * @return	An array of three doubles containing the y-basis of this Mat4d.
	 */
	public double[] getYBasisArray()
	{
		return new double[]
		{
			m10, m11, m12
		};
	}


	/**
	 * Set the y-basis of this Mat4d from a provided Vec3d.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12 properties.
	 * <p>
	 * To ensure the legality of the y-basis, the provided Vec3d is normalised if required before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a rotation matrix then you should avoid the
	 * setXBasis/setYBasis/setZBasis methods and instead set the matrix properties via other methods which do not enforce normalisation such
	 * as the {@link #setFromArray(double[])} and {@link #Mat4d(double[])} methods.
	 *
	 * @param	v	The Vec3d holding the y-basis to set.
	 * @return this matrix.
	 */
	public Mat4d setYBasis(Vec3d v)
	{
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		m10 = v.x;
		m11 = v.y;
		m12 = v.z;
		return this;
	}


	/**
	 * Set the y-basis of this Mat4d from a provided array of three doubles.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12 properties.
	 * <p>
	 * To ensure the legality of the y-basis, the provided array is converted into a Vec3d and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a rotation matrix then you should avoid the
	 * setXBasis/setYBasis/setZBasis methods and instead set the matrix properties via other methods which do not enforce normalisation such
	 * as the {@link #setFromArray(double[])} and {@link #Mat4d(double[])} methods.
	 *
	 * @param	f	The array of three doubles to set as the y-basis of this Mat4d.
	 * @return this matrix.
	 */
	public Mat4d setYBasis(double[] f)
	{
		if (f.length != 3)
		{
			throw new IllegalArgumentException();
		}

		Vec3d v = new Vec3d(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		m10 = v.x;
		m11 = v.y;
		m12 = v.z;
		return this;
	}


	/**
	 * Return the z-basis of this Mat4d as a Vec3d.
	 * <p>
	 * The z-basis is the orientation of the x-axis, as held by the m20, m21 and m22 properties.
	 *
	 * @return	A Vec3d containing the z-basis of this Mat4d.
	 */
	public Vec3d getZBasis()
	{
		return new Vec3d(m20, m21, m22);
	}


	/**
	 * Return the z-basis of this Mat4d as an array of three doubles.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22 properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not want to use the {@link Vec3d} class.
	 *
	 * @return	An array of three doubles containing the y-basis of this Mat4d.
	 */
	public double[] getZBasisArray()
	{
		return new double[]
		{
			m20, m21, m22
		};
	}


	/**
	 * Set the z-basis of this Mat4d from a provided Vec3d.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22 properties.
	 * <p>
	 * To ensure the legality of the z-basis, the provided Vec3d is normalised if required before being set. If you wish to use this class
	 * for storing matrices which do not represent a rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis methods and
	 * instead set the matrix properties via other methods which do not enforce normalisation such as the {@link #setFromArray(double[])}
	 * and {@link #Mat4d(double[])} methods.
	 *
	 * @param	v	The Vec3d holding the z-basis to set.
	 * @return this matrix.
	 */
	public Mat4d setZBasis(Vec3d v)
	{
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		m20 = v.x;
		m21 = v.y;
		m22 = v.z;
		return this;
	}


	/**
	 * Set the z-basis of this Mat4d from a provided array of three doubles.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22 properties.
	 * <p>
	 * To ensure the legality of the z-basis, the provided array is converted into a Vec3d and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a rotation matrix then you should avoid the
	 * setXBasis/setYBasis/setZBasis methods and instead set the matrix properties via other methods which do not enforce normalisation such
	 * as the {@link #setFromArray(double[])} and {@link #Mat4d(double[])} methods.
	 *
	 * @param	f	The array of three doubles to set as the z-basis of this Mat4d.
	 * @return this matrix.
	 */
	public Mat4d setZBasis(double[] f)
	{
		if (f.length != 3)
		{
			throw new IllegalArgumentException();
		}

		Vec3d v = new Vec3d(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0, DOUBLE_EQUALITY_TOLERANCE))
		{
			v.normalize();
		}

		m20 = v.x;
		m21 = v.y;
		m22 = v.z;
		return this;
	}


	/**
	 * Return the origin of this Mat4d.
	 *
	 * @return	A Vec3d of the origin location of this Mat4d, as stored in the m30, m31 and m32 properties.
	 */
	public Vec3d getOrigin()
	{
		return new Vec3d(m30, m31, m32);
	}


	public Vec3d getOrigin(Vec3d aOutput)
	{
		return aOutput.set(m30, m31, m32);
	}


	/**
	 * Set the origin of this Mat4d.
	 *
	 * @return this matrix.
	 */
	public Mat4d setOrigin(Vec3d v)
	{
		m30 = v.x;
		m31 = v.y;
		m32 = v.z;
		return this;
	}


	/**
	 * Set the origin of this Mat4d.
	 *
	 * @return this matrix.
	 */
	public Mat4d setOrigin(double x, double y, double z)
	{
		m30 = x;
		m31 = y;
		m32 = z;
		return this;
	}


	/**
	 * Initializes this matrix with identity and an origin.
	 *
	 * @return this matrix.
	 */
	public Mat4d makeOrigin(double x, double y, double z)
	{
		set(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			x, y, z, 1
		);
		return this;
	}


	/**
	 * Initializes this matrix with identity and an origin.
	 *
	 * @return this matrix.
	 */
	public Mat4d makeOrigin(Vec3d v)
	{
		set(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			v.x, v.y, v.z, 1
		);
		return this;
	}


	/**
	 * @return this matrix.
	 */
	public Mat4d makeScale(double x, double y, double z)
	{
		set(
			x, 0, 0, 0,
			0, y, 0, 0,
			0, 0, z, 0,
			0, 0, 0, 1
		);
		return this;
	}


	/**
	 * Return whether or not all three axes of this Mat4d are orthogonal (i.e. at 90 degrees to each other).
	 * <p>
	 * Any two axes, such as x/y, x/z or y/z will be orthogonal if their dot product is zero. However, to account for doubleing point
	 * precision errors, this method accepts that two axes are orthogonal if their dot product is less than or equal to 0.01f.
	 * <p>
	 * If you want to find out if any two specific axes are orthogonal, then you can use code similar to the following:
	 * <p>
	 * {@code boolean xDotYOrthogonal = Math.abs( xAxis.dot(yAxis) ) <= 0.01f;}
	 *
	 * @return	A boolean indicating whether this Mat4d is orthogonal or not.
	 */
	public boolean isOrthogonal()
	{
		// Get the x, y and z axes of the matrix as Vec3d objects
		Vec3d xAxis = new Vec3d(m00, m01, m02);
		Vec3d yAxis = new Vec3d(m10, m11, m12);
		Vec3d zAxis = new Vec3d(m20, m21, m22);

		// As exact doubleing point comparisons are a bad idea, we'll accept that a double is
		// approximately equal to zero if it is +/- this epsilon.
		double epsilon = 0.01f;

		// Check whether the x/y, x/z and y/z axes are orthogonal.
		// If two axes are orthogonal then their dot product will be zero (or approximately zero in this case).
		// Note: We could have picked y/x, z/x, z/y but it's the same thing - they're either orthogonal or they're not.
		boolean xDotYOrthogonal = Math.abs(xAxis.dot(yAxis)) <= epsilon;
		boolean xDotZOrthogonal = Math.abs(xAxis.dot(zAxis)) <= epsilon;
		boolean yDotZOrthogonal = Math.abs(yAxis.dot(zAxis)) <= epsilon;

		// All three axes are orthogonal? Return true
		return (xDotYOrthogonal && xDotZOrthogonal && yDotZOrthogonal);
	}


	/**
	 * Transpose this Mat4d.
	 *
	 * @return this matrix.
	 */
	public Mat4d transpose()
	{
		return set(
			m00, m10, m20, m30,
			m01, m11, m21, m31,
			m02, m12, m22, m32,
			m03, m13, m23, m33
		);
	}


	public Mat4d transposeYZ()
	{
		return set(
			m02, m01, m00, m03,
			m22, m21, m20, m23,
			m12, m11, m10, m13,
			m30, m31, m32, m33
		);
	}


	/**
	 * Multiplies this Mat4d by another Mat4d.
	 *
	 * @return this matrix.
	 */
	public Mat4d multiply(Mat4d m)
	{
		return set(
			m00 * m.m00 + m10 * m.m01 + m20 * m.m02 + m30 * m.m03,
			m01 * m.m00 + m11 * m.m01 + m21 * m.m02 + m31 * m.m03,
			m02 * m.m00 + m12 * m.m01 + m22 * m.m02 + m32 * m.m03,
			m03 * m.m00 + m13 * m.m01 + m23 * m.m02 + m33 * m.m03,

			m00 * m.m10 + m10 * m.m11 + m20 * m.m12 + m30 * m.m13,
			m01 * m.m10 + m11 * m.m11 + m21 * m.m12 + m31 * m.m13,
			m02 * m.m10 + m12 * m.m11 + m22 * m.m12 + m32 * m.m13,
			m03 * m.m10 + m13 * m.m11 + m23 * m.m12 + m33 * m.m13,

			m00 * m.m20 + m10 * m.m21 + m20 * m.m22 + m30 * m.m23,
			m01 * m.m20 + m11 * m.m21 + m21 * m.m22 + m31 * m.m23,
			m02 * m.m20 + m12 * m.m21 + m22 * m.m22 + m32 * m.m23,
			m03 * m.m20 + m13 * m.m21 + m23 * m.m22 + m33 * m.m23,

			m00 * m.m30 + m10 * m.m31 + m20 * m.m32 + m30 * m.m33,
			m01 * m.m30 + m11 * m.m31 + m21 * m.m32 + m31 * m.m33,
			m02 * m.m30 + m12 * m.m31 + m22 * m.m32 + m32 * m.m33,
			m03 * m.m30 + m13 * m.m31 + m23 * m.m32 + m33 * m.m33
		);
	}


	public Mat4d multiply(Vec3d m)
	{
		return multiply(new Mat4d().makeOrigin(m));
	}


	/**
	 * Transform the provided point in 3D space.
	 *
	 * @return the provided Vec3d.
	 */
	public Vec3d transformPoint(Vec3d v)
	{
		v.x = m00 * v.x + m10 * v.y + m20 * v.z + m30;
		v.y = m01 * v.x + m11 * v.y + m21 * v.z + m31;
		v.z = m02 * v.x + m12 * v.y + m22 * v.z + m32;

		return v;
	}


	/**
	 * Transform the provided point in 3D space.
	 *
	 * @return the provided Vec3d.
	 */
	public Vec4d transformPoint(Vec4d v)
	{
		v.x = m00 * v.x + m10 * v.y + m20 * v.z + m30;
		v.y = m01 * v.x + m11 * v.y + m21 * v.z + m31;
		v.z = m02 * v.x + m12 * v.y + m22 * v.z + m32;
		v.w = m03 * v.x + m13 * v.y + m23 * v.z + m33;

		return v;
	}


	/**
	 * Transform a direction in 3D space taking into account the orientation of this Mat4ds x/y/z axes.
	 *
	 * @return a new Vec3d with the transformed result.
	 */
	public Vec3d transformDirection(Vec3d v)
	{
		Vec3d result = new Vec3d();

		result.x = m00 * v.x + m10 * v.y + m20 * v.z;
		result.y = m01 * v.x + m11 * v.y + m21 * v.z;
		result.z = m02 * v.x + m12 * v.y + m22 * v.z;

		return result;
	}


	public Mat4d scale(double aScale)
	{
		m00 *= aScale;
		m01 *= aScale;
		m02 *= aScale;
		m03 *= aScale;
		m10 *= aScale;
		m11 *= aScale;
		m12 *= aScale;
		m13 *= aScale;
		m20 *= aScale;
		m21 *= aScale;
		m22 *= aScale;
		m23 *= aScale;
		m30 *= aScale;
		m31 *= aScale;
		m32 *= aScale;
		m33 *= aScale;

		return this;
	}


	/**
	 * Invert this Mat4d.
	 * <p>
	 * Only matrices which do not have a {@link #determinant()} of zero can be inverted. If the determinant of the provided matrix is zero
	 * then an IllegalArgumentException is thrown.
	 *
	 * @param	m	The matrix to invert.
	 * @return	The inverted matrix.
	 */
	public Mat4d invert()
	{
		Mat4d m = new Mat4d();

		m.m00 = m12 * m23 * m31 - m13 * m22 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33 + m11 * m22 * m33;
		m.m01 = m03 * m22 * m31 - m02 * m23 * m31 - m03 * m21 * m32 + m01 * m23 * m32 + m02 * m21 * m33 - m01 * m22 * m33;
		m.m02 = m02 * m13 * m31 - m03 * m12 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 + m01 * m12 * m33;
		m.m03 = m03 * m12 * m21 - m02 * m13 * m21 - m03 * m11 * m22 + m01 * m13 * m22 + m02 * m11 * m23 - m01 * m12 * m23;
		m.m10 = m13 * m22 * m30 - m12 * m23 * m30 - m13 * m20 * m32 + m10 * m23 * m32 + m12 * m20 * m33 - m10 * m22 * m33;
		m.m11 = m02 * m23 * m30 - m03 * m22 * m30 + m03 * m20 * m32 - m00 * m23 * m32 - m02 * m20 * m33 + m00 * m22 * m33;
		m.m12 = m03 * m12 * m30 - m02 * m13 * m30 - m03 * m10 * m32 + m00 * m13 * m32 + m02 * m10 * m33 - m00 * m12 * m33;
		m.m13 = m02 * m13 * m20 - m03 * m12 * m20 + m03 * m10 * m22 - m00 * m13 * m22 - m02 * m10 * m23 + m00 * m12 * m23;
		m.m20 = m11 * m23 * m30 - m13 * m21 * m30 + m13 * m20 * m31 - m10 * m23 * m31 - m11 * m20 * m33 + m10 * m21 * m33;
		m.m21 = m03 * m21 * m30 - m01 * m23 * m30 - m03 * m20 * m31 + m00 * m23 * m31 + m01 * m20 * m33 - m00 * m21 * m33;
		m.m22 = m01 * m13 * m30 - m03 * m11 * m30 + m03 * m10 * m31 - m00 * m13 * m31 - m01 * m10 * m33 + m00 * m11 * m33;
		m.m23 = m03 * m11 * m20 - m01 * m13 * m20 - m03 * m10 * m21 + m00 * m13 * m21 + m01 * m10 * m23 - m00 * m11 * m23;
		m.m30 = m12 * m21 * m30 - m11 * m22 * m30 - m12 * m20 * m31 + m10 * m22 * m31 + m11 * m20 * m32 - m10 * m21 * m32;
		m.m31 = m01 * m22 * m30 - m02 * m21 * m30 + m02 * m20 * m31 - m00 * m22 * m31 - m01 * m20 * m32 + m00 * m21 * m32;
		m.m32 = m02 * m11 * m30 - m01 * m12 * m30 - m02 * m10 * m31 + m00 * m12 * m31 + m01 * m10 * m32 - m00 * m11 * m32;
		m.m33 = m01 * m12 * m20 - m02 * m11 * m20 + m02 * m10 * m21 - m00 * m12 * m21 - m01 * m10 * m22 + m00 * m11 * m22;

		// Get the determinant of this matrix
		double determinant = m.determinant();

		// Each property of the inverse matrix is multiplied by 1.0 divided by the determinant.
		// As we cannot divide by zero, we will throw an IllegalArgumentException if the determinant is zero.
		if (determinant == 0)
		{
			throw new IllegalArgumentException("Cannot invert a matrix with a determinant of zero.");
		}

		// Otherwise, calculate the value of one over the determinant and scale the matrix by that value
		m.scale(1.0 / determinant);

		set(m);

		return this;
	}


	/**
	 * Calculate the determinant of this matrix.
	 *
	 * @return	The determinant of this matrix.
	 */
	public double determinant()
	{
		return m03 * m12 * m21 * m30 - m02 * m13 * m21 * m30 - m03 * m11 * m22 * m30 + m01 * m13 * m22 * m30
			+ m02 * m11 * m23 * m30 - m01 * m12 * m23 * m30 - m03 * m12 * m20 * m31 + m02 * m13 * m20 * m31
			+ m03 * m10 * m22 * m31 - m00 * m13 * m22 * m31 - m02 * m10 * m23 * m31 + m00 * m12 * m23 * m31
			+ m03 * m11 * m20 * m32 - m01 * m13 * m20 * m32 - m03 * m10 * m21 * m32 + m00 * m13 * m21 * m32
			+ m01 * m10 * m23 * m32 - m00 * m11 * m23 * m32 - m02 * m11 * m20 * m33 + m01 * m12 * m20 * m33
			+ m02 * m10 * m21 * m33 - m00 * m12 * m21 * m33 - m01 * m10 * m22 * m33 + m00 * m11 * m22 * m33;
	}


	/**
	 * Translate this matrix by a provided Vec3d.
	 * <p>
	 * The changes made are to <strong>this</strong> Mat4d, in the coordinate space of this matrix.
	 *
	 * @param	v	The vector to translate this matrix by.
	 * @return	This Mat4d for chaining.
	 */
	public Mat4d translate(Vec3d v)
	{
		return translate(v.x, v.y, v.z);
	}


	/**
	 * Translate this matrix by separate x, y and z.
	 *
	 * @param	x	The amount to translate on the X-axis.
	 * @param	y	The amount to translate on the Y-axis.
	 * @param	z	The amount to translate on the Z-axis.
	 * @return this matrix.
	 */
	public Mat4d translate(double x, double y, double z)
	{
		m30 += m00 * x + m10 * y + m20 * z;
		m31 += m01 * x + m11 * y + m21 * z;
		m32 += m02 * x + m12 * y + m22 * z;
		m33 += m03 * x + m13 * y + m23 * z;

		return this;
	}


	/**
	 * Rotate this matrix about a local axis by an angle specified in radians.
	 * <p>
	 * By a 'local' axis we mean that for example, if you rotated this matrix about the positive X-axis (1,0,0), then rotation would occur
	 * around the positive X-axis of
	 * <strong>this matrix</strong>, not the <em>global / world-space</em> X-axis.
	 *
	 * @param	angleRads	The angle to rotate the matrix in radians.
	 * @param	localAxis	The local axis around which to rotate the matrix.
	 * @return this matrix.
	 */
	// Method to rotate a matrix around an arbitrary axis
	public Mat4d rotateAboutLocalAxisRads(double angleRads, Vec3d localAxis)
	{
		double cos = Math.cos(angleRads);
		double sin = Math.sin(angleRads);
		double nivCos = 1.0 - cos;

		double xy = localAxis.x * localAxis.y;
		double yz = localAxis.y * localAxis.z;
		double xz = localAxis.x * localAxis.z;
		double xs = localAxis.x * sin;
		double ys = localAxis.y * sin;
		double zs = localAxis.z * sin;

		double f00 = localAxis.x * localAxis.x * nivCos + cos;
		double f01 = xy * nivCos + zs;
		double f02 = xz * nivCos - ys;

		double f10 = xy * nivCos - zs;
		double f11 = localAxis.y * localAxis.y * nivCos + cos;
		double f12 = yz * nivCos + xs;

		double f20 = xz * nivCos + ys;
		double f21 = yz * nivCos - xs;
		double f22 = localAxis.z * localAxis.z * nivCos + cos;

		return set(
			m00 * f00 + m10 * f01 + m20 * f02,
			m01 * f00 + m11 * f01 + m21 * f02,
			m02 * f00 + m12 * f01 + m22 * f02,
			m03 * f00 + m13 * f01 + m23 * f02,
			m00 * f10 + m10 * f11 + m20 * f12,
			m01 * f10 + m11 * f11 + m21 * f12,
			m02 * f10 + m12 * f11 + m22 * f12,
			m03 * f10 + m13 * f11 + m23 * f12,
			m00 * f20 + m10 * f21 + m20 * f22,
			m01 * f20 + m11 * f21 + m21 * f22,
			m02 * f20 + m12 * f21 + m22 * f22,
			m03 * f20 + m13 * f21 + m23 * f22,
			m30,
			m31,
			m32,
			m33
		);
	}


	/**
	 * Rotate this matrix about a local axis by an angle specified in degrees.
	 * <p>
	 * By a 'local' axis we mean that for example, if you rotated this matrix about the positive X-axis (1,0,0), then rotation would occur
	 * around the positive X-axis of
	 * <strong>this matrix</strong>, not the <em>global / world-space</em> X-axis.
	 *
	 * @param	angleDegs	The angle to rotate the matrix in degrees.
	 * @param	localAxis	The local axis around which to rotate the matrix.
	 * @return this matrix.
	 */
	public Mat4d rotateAboutLocalAxisDegs(double angleDegs, Vec3d localAxis)
	{
		return rotateAboutLocalAxisRads(angleDegs * DEGS_TO_RADS, localAxis);
	}


	/**
	 * Rotate this matrix about a world-space axis by an angle specified in radians.
	 * <p>
	 * The cardinal 'world-space' axes are defined so that the +X axis runs to the right, the +Y axis runs upwards, and the +Z axis runs
	 * directly outwards from the screen.
	 *
	 * @param	angleRads	The angle to rotate the matrix in radians.
	 * @param	worldAxis	The world-space axis around which to rotate the matrix.
	 * @return this matrix.
	 */
	public Mat4d rotateAboutWorldAxisRads(double angleRads, Vec3d worldAxis)
	{
		Vec3d xAxis = new Vec3d(m00, m01, m02);
		Vec3d yAxis = new Vec3d(m10, m11, m12);
		Vec3d zAxis = new Vec3d(m20, m21, m22);

		Vec3d rotatedXAxis = Vec3d.rotateAboutAxisRads(xAxis, angleRads, worldAxis);
		Vec3d rotatedYAxis = Vec3d.rotateAboutAxisRads(yAxis, angleRads, worldAxis);
		Vec3d rotatedZAxis = Vec3d.rotateAboutAxisRads(zAxis, angleRads, worldAxis);

		m00 = rotatedXAxis.x;
		m01 = rotatedXAxis.y;
		m02 = rotatedXAxis.z;
		m10 = rotatedYAxis.x;
		m11 = rotatedYAxis.y;
		m12 = rotatedYAxis.z;
		m20 = rotatedZAxis.x;
		m21 = rotatedZAxis.y;
		m22 = rotatedZAxis.z;

		return this;
	}


	/**
	 * Rotate this matrix about a world-space axis by an angle specified in radians.
	 * <p>
	 * The cardinal 'world-space' axes are defined so that the +X axis runs to the right, the +Y axis runs upwards, and the +Z axis runs
	 * directly outwards from the screen.
	 *
	 * @param	angleDegs	The angle to rotate the matrix in degrees.
	 * @param	worldAxis	The world-space axis around which to rotate the matrix.
	 * @return this matrix.
	 */
	public Mat4d rotateAboutWorldAxisDegs(double angleDegs, Vec3d localAxis)
	{
		return rotateAboutWorldAxisRads(angleDegs * DEGS_TO_RADS, localAxis);
	}


	public Mat3d toMat3d()
	{
		Mat3d rotationMatrix = new Mat3d();

		rotationMatrix.m00 = m00;
		rotationMatrix.m01 = m01;
		rotationMatrix.m02 = m02;

		rotationMatrix.m10 = m10;
		rotationMatrix.m11 = m11;
		rotationMatrix.m12 = m12;

		rotationMatrix.m20 = m20;
		rotationMatrix.m21 = m21;
		rotationMatrix.m22 = m22;

		return rotationMatrix;
	}


	/**
	 * Return this Mat4d as an array of 16 doubles.
	 */
	public double[] toArray()
	{
		return new double[]
		{
			m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33
		};
	}


	/**
	 * Note: Displays output in COLUMN-MAJOR format!
	 */
	@Override
	public String toString()
	{
		return String.format("{{%8.4f, %8.4f, %8.4f, %8.4f},{%8.4f, %8.4f, %8.4f, %8.4f},{%8.4f, %8.4f, %8.4f, %8.4f},{%8.4f, %8.4f, %8.4f, %8.4f}}", m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
	}


	/**
	 * Note: Displays output in COLUMN-MAJOR format!
	 */
	public String toString2D()
	{
		return String.format("{%n\t{%8.4f, %8.4f, %8.4f, %8.4f}%n\t{%8.4f, %8.4f, %8.4f, %8.4f}%n\t{%8.4f, %8.4f, %8.4f, %8.4f}%n\t{%8.4f, %8.4f, %8.4f, %8.4f}%n}", m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
	}


	// ---------- Static methods ----------
	/**
	 * Construct an orthographic projection matrix.
	 * <p>
	 * Orthographic projections are commonly used when working in 2D or CAD scenarios. As orthographic projection does not perform
	 * foreshortening on any projected geometry, objects are drawn the same size regardless of their distance from the camera.
	 * <p>
	 * By specifying the bottom clipping plane to be 0.0 and the top clipping plane to be the height of the window, the origin of the
	 * coordinate space is at the bottom-left of the window and the positive y-axis runs upwards. To place the origin at the top left of the
	 * window and have the y-axis run downwards, simply swap the top and bottom values.
	 * <p>
	 * Once you have an orthographic projection matrix, if you are not using any separate Model or View matrices then you may simply use the
	 * orthographic matrix as a ModelViewProjection matrix.
	 * <p>
	 * If values are passed so that (right - left), (top - bottom) or (far - near) are zero then an IllegalArgumentException is thrown.
	 *
	 * @param	left	The left clipping plane, typically 0.0.
	 * @param	right	The right clipping plane, typically the width of the window.
	 * @param	top	The top clipping plane, typically the height of the window.
	 * @param	bottom The bottom clipping plane, typically 0.0.
	 * @param	near	The near clipping plane, typically -1.0.
	 * @param	far The far clipping plane, typically 1.0.
	 * @return	The constructed orthographic matrix
	 * @see
	 * <a href="http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho">http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho</a>
	 */
	public static Mat4d createOrthographicProjectionMatrix(double left, double right, double top, double bottom, double near, double far)
	{
		// Perform sanity checking to avoid divide by zero errors
		if (right == left)
		{
			throw new IllegalArgumentException("(right - left) cannot be zero.");
		}
		if (top == bottom)
		{
			throw new IllegalArgumentException("(top - bottom) cannot be zero.");
		}
		if (far == near)
		{
			throw new IllegalArgumentException("(far - near) cannot be zero.");
		}

		// Got legal arguments? Construct the orthographic matrix
		Mat4d m = new Mat4d();

		m.m00 = 2.0 / (right - left);
		m.m01 = 0.0;
		m.m02 = 0.0;
		m.m03 = 0.0;

		m.m10 = 0.0;
		m.m11 = 2.0 / (top - bottom);
		m.m12 = 0.0;
		m.m13 = 0.0;

		m.m20 = 0.0;
		m.m21 = 0.0;
		m.m22 = -2.0 / (far - near);
		m.m23 = 0.0;

		m.m30 = -(right + left) / (right - left);
		m.m31 = -(top + bottom) / (top - bottom);
		m.m32 = -(far + near) / (far - near);
		m.m33 = 1.0;

		return m;
	}


	/**
	 * Construct a perspective projection matrix.
	 * <p>
	 * The parameters provided are the locations of the left/right/top/bottom/near/far clipping planes.
	 * <p>
	 * There is rarely any need to specify the bounds of a projection matrix in this manner, and you are likely to be better served by using
	 * the {@link #createPerspectiveProjectionMatrix(double, double, double, double)} method instead.
	 * <p>
	 * Once you have a Projection matrix, then it can be combined with a ModelView or separate Model and View matrices in the following
	 * manner (be careful: multiplication order is important) to create a ModelViewProjection matrix:
	 * <p>
	 * {@code Mat4d mvpMatrix = projectionMatrix.times(modelViewMatrix);}
	 * <p>
	 * or
	 * <p>
	 * {@code Mat4d mvpMatrix = projectionMatrix.times(viewMatrix).times(modelMatrix);}
	 *
	 * @param	left	The left clipping plane, typically 0.0.
	 * @param	right	The right clipping plane, typically the width of the window.
	 * @param	top	The top clipping plane, typically the height of the window.
	 * @param	bottom The bottom clipping plane, typically 0.0.
	 * @param	near	The near clipping plane, typically -1.0.
	 * @param	far The far clipping plane, typically 1.0.
	 * @return	The constructed orthographic matrix
	 * @see
	 * <a href="http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho">http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho</a>
	 */
	public static Mat4d createPerspectiveProjectionMatrix(double left, double right, double top, double bottom, double near, double far)
	{
		// Instantiate a new matrix, initialised to identity
		Mat4d p = new Mat4d(1.0);

		// Set matrix values
		p.m00 = (2.0 * near) / (right - left);

		p.m11 = (2.0 * near) / (top - bottom);

		p.m20 = (right + left) / (right - left);
		p.m21 = (top + bottom) / (top - bottom);
		p.m22 = -(far + near) / (far - near);
		p.m23 = -1.0;

		p.m32 = (-2.0 * far * near) / (far - near);
		p.m33 = 0.0;

		return p;
	}


	/**
	 * *
	 * Construct a perspective projection matrix.
	 * <p>
	 * The vertical and horizontal field of view (FoV) values are related in such a way that if you know one then you can calculate the
	 * other. This method takes the vertical FoV and allows the horizontal FoV to adapt to it based on the aspect ratio of the screen in a
	 * technique called 'Hor+' (horizontal plus) scaling.
	 * <p>
	 * If required, the horizontal and vertical FoVs can be calculated via the following process (note: all angles are specified in
	 * <strong>radians</strong>):
	 * <p>
	 * {@code double horizFoVRads = 2.0 * Math.atan( Math.tan(vertFoVRads  / 2.0) * aspectRatio);}
	 * <p>
	 * {@code double vertFoVRads  = 2.0 * Math.atan( Math.tan(horizFoVRads / 2.0) * (1.0 / aspectRatio) ); }
	 * <p>
	 * The aspect ratio can be calculated as: {@code windowWidth / windowHeight} - if the size of the window changes then a new projection
	 * matrix should be created with the new aspect ratio of the window.
	 * <p>
	 * {@code zNear} and {@code zFar} represent the near and far clipping distances outside of which any geometry will be clipped (i.e. not
	 * rendered). An acceptable value for zNear is <strong>1.0</strong> (0.0 should be avoided), however, specifying a zNear value of
	 * <strong>2.0</strong> will essentially double the precision of your depth buffer due to the way in which floating point values
	 * distribute bits between the significand (i.e. the value before the decimal point) and the exponent (the value to raise or lower the
	 * significand to). Choosing good values for your near and far clipping planes can often eliminate any z-fighting in your scenes.
	 * <p>
	 * An {@link IllegalArgumentException} is thrown is any of the parameters are specified with illegal values.
	 *
	 * @param	vertFoVDegs	The vertical Field of View angle - must be a positive value between 1.0 and 179.0. For good choices, see
	 * <a href="http://en.wikipedia.org/wiki/Field_of_view_in_video_games#Choice_of_field_of_view">choice of field of view</a>.
	 * @param	aspectRatio	The aspect ratio of the window in which drawing will occur - must be a positive value.
	 * @param	zNear	The near clipping distance - must be a positive value which is less than zFar.
	 * @param	zFar	The far clipping distance - must be a positive value which is greater than zNear.
	 * @return	A projection matrix as a Mat4d.
	 * @see
	 * <a href="http://en.wikipedia.org/wiki/Field_of_view_in_video_games">http://en.wikipedia.org/wiki/Field_of_view_in_video_games</a>
	 * @see		<a href="http://www.songho.ca/opengl/gl_projectionmatrix.html">http://www.songho.ca/opengl/gl_projectionmatrix.html</a>
	 * @see
	 * <a href="https://www.opengl.org/archives/resources/faq/technical/depthbuffer.htm">https://www.opengl.org/archives/resources/faq/technical/depthbuffer.htm</a>
	 * @see		<a href="http://en.wikipedia.org/wiki/Floating_point">http://en.wikipedia.org/wiki/Floating_point</a>
	 * @see		<a href="http://en.wikipedia.org/wiki/Z-fighting">http://en.wikipedia.org/wiki/Z-fighting</a>
	 */
	public static Mat4d createPerspectiveProjectionMatrix(double vertFoVDegs, double aspectRatio, double zNear, double zFar)
	{
		// Sanity checking
		if (aspectRatio < 0.0)
		{
			throw new IllegalArgumentException("Aspect ratio cannot be negative.");
		}
		if (zNear <= 0.0 || zFar <= 0.0)
		{
			throw new IllegalArgumentException("The values of zNear and zFar must be positive.");
		}
		if (zNear >= zFar)
		{
			throw new IllegalArgumentException("zNear must be less than than zFar.");
		}
		if (vertFoVDegs < 1.0 || vertFoVDegs > 179.0)
		{
			throw new IllegalArgumentException("Vertical FoV must be within 1 and 179 degrees inclusive.");
		}

		double frustumLength = zFar - zNear;

		// Calculate half the vertical field of view in radians
		double halfVertFoVRads = (vertFoVDegs / 2.0) * DEGS_TO_RADS;

		// There is no built in Math.cot() in Java, but co-tangent is simply 1 over tangent
		double cotangent = 1.0 / Math.tan(halfVertFoVRads);

		// Instantiate a new matrix, initialised to identity
		Mat4d p = new Mat4d(1.0);

		// Set matrix values and return the constructed projection matrix
		p.m00 = cotangent / aspectRatio;

		p.m11 = cotangent;

		p.m22 = -(zFar + zNear) / frustumLength;
		p.m23 = -1.0;

		p.m32 = (-2.0 * zNear * zFar) / frustumLength;
		p.m33 = 0.0;

		return p;
	}


	// used by bi_Camera - usefull?
//	public static Mat4d perspective(double aFov, double aNear, double aFar)
//	{
//		// Camera points towards -z.  0 < near < far.
//		// Matrix maps z range [-near, -far] to [-1, 1], after homogeneous division.
//		double f = 1.0 / (Math.tan(aFov * Math.PI / 360.0));
//		double d = 1.0 / (aNear - aFar);
//
//		Mat4d r = new Mat4d();
//		r.m00 = f;
//		r.m01 = 0;
//		r.m02 = 0;
//		r.m03 = 0;
//		r.m10 = 0;
//		r.m11 = -f;
//		r.m12 = 0;
//		r.m13 = 0;
//		r.m20 = 0;
//		r.m21 = 0;
//		r.m22 = (aNear + aFar) * d;
//		r.m23 = 2 * aNear * aFar * d;
//		r.m30 = 0;
//		r.m31 = 0;
//		r.m32 = -1;
//		r.m33 = 0;
//
//		return r;
//	}


	public Mat4d makeRotationX(double angle)
	{
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		return set(
			1, 0, 0, 0,
			0, c, s, 0,
			0, -s, c, 0,
			0, 0, 0, 1
		);
	}


	public Mat4d makeRotationY(double angle)
	{
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		return set(
			c, 0, -s, 0,
			0, 1, 0, 0,
			s, 0, c, 0,
			0, 0, 0, 1
		);
	}


	public Mat4d makeRotationZ(double angle)
	{
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		return set(
			c, s, 0, 0,
			-s, c, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}


	@Override
	public Mat4d clone()
	{
		Mat4d m = new Mat4d();
		m.set(this);
		return m;
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
}
