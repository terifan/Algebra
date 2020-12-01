package org.terifan.algebra;

import java.io.Serializable;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.BundlableInput;
import org.terifan.bundle.BundlableOutput;


/**
 * Column-major order matrix.
 */
public class Mat3d implements Cloneable, Serializable, Bundlable
{
	private final static long serialVersionUID = 1L;

	public double m00, m01, m02; // First  column - typically the direction of the positive X-axis
	public double m10, m11, m12; // Second column - typically the direction of the positive Y-axis
	public double m20, m21, m22; // Third  column - typically the direction of the positive Z-axis


	/**
	 * Default constructor - all matrix elements are set to 0.0.
	 */
	public Mat3d()
	{
	}


	/**
	 * Constructor which sets the given value across the diagonal and zeroes the rest of the matrix.
	 */
	public Mat3d(double aValue)
	{
		m00 = m11 = m22 = aValue;
	}


	/**
	 * Constructor which sets the matrix from the three provided axes.
	 *
	 * @param xAxis	The positive X-axis to set.
	 * @param yAxis	The positive Y-axis to set.
	 * @param zAxis	The positive Z-axis to set.
	 */
	public Mat3d(Vec3d xAxis, Vec3d yAxis, Vec3d zAxis)
	{
		m00 = xAxis.x;
		m01 = xAxis.y;
		m02 = xAxis.z;

		m10 = yAxis.x;
		m11 = yAxis.y;
		m12 = yAxis.z;

		m20 = zAxis.x;
		m21 = zAxis.y;
		m22 = zAxis.z;
	}


	public Mat3d set(Mat3d aSource)
	{
		m00 = aSource.m00;
		m01 = aSource.m01;
		m02 = aSource.m02;
		m10 = aSource.m10;
		m11 = aSource.m11;
		m12 = aSource.m12;
		m20 = aSource.m20;
		m21 = aSource.m21;
		m22 = aSource.m22;
		return this;
	}


	public Mat3d set(double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22)
	{
		m00 = a00;
		m01 = a01;
		m02 = a02;
		m10 = a10;
		m11 = a11;
		m12 = a12;
		m20 = a20;
		m21 = a21;
		m22 = a22;
		return this;
	}


	/**
	 * Zero all elements of this matrix.
	 * @return this matrix.
	 */
	public Mat3d zero()
	{
		m00 = m01 = m02 = m10 = m11 = m12 = m20 = m21 = m22 = 0.0;
		return this;
	}


	/**
	 * Reset this matrix to identity.
	 * @return this matrix.
	 */
	public Mat3d identity()
	{
		m00 = m11 = m22 = 1.0;
		m01 = m02 = m10 = m12 = m20 = m21 = 0.0;
		return this;
	}


	/**
	 * Transposes this matrix.
	 * @return this matrix.
	 */
	public Mat3d transpose()
	{
		set(m00, m10, m20, m01, m11, m21, m02, m12, m22);
		return this;
	}


	/**
	 * Create a rotation matrix from a given direction.
	 * <p>
	 * The reference direction is aligned to the Z-Axis, and the X-Axis is generated via the genPerpendicularVectorQuuck() method. The
	 * Y-Axis is then the cross-product of those two axes.
	 * <p>
	 * This method uses the <a href="https://gist.github.com/roxlu/3082114">Frisvad technique</a> for generating perpendicular axes.
	 *
	 * @param	aReferenceDirection	The vector to use as the Z-Axis
	 * @return	The created rotation matrix.
	 *
	 * @see Vec3d#genPerpendicularVectorQuick(Vec3d)
	 */
	public static Mat3d createRotationMatrix(Vec3d aReferenceDirection)
	{
		Vec3d xAxis;
		Vec3d yAxis;
		Vec3d zAxis = aReferenceDirection.normalize();

		// Handle the singularity (i.e. bone pointing along negative Z-Axis)...
		if (aReferenceDirection.z < -0.9999999)
		{
			xAxis = new Vec3d(1.0, 0.0, 0.0); // ...in which case positive X runs directly to the right...
			yAxis = new Vec3d(0.0, 1.0, 0.0); // ...and positive Y runs directly upwards.
		}
		else
		{
			double a = 1.0 / (1.0 + zAxis.z);
			double b = -zAxis.x * zAxis.y * a;
			xAxis = new Vec3d(1.0 - zAxis.x * zAxis.x * a, b, -zAxis.x).normalize();
			yAxis = new Vec3d(b, 1.0 - zAxis.y * zAxis.y * a, -zAxis.y).normalize();
		}

		return new Mat3d(xAxis, yAxis, zAxis);
	}


	/**
	 * Return whether this matrix consists of three orthogonal axes or not to within a cross-product of 0.01.
	 *
	 * @return	Whether or not this matrix is orthogonal.
	 */
	public boolean isOrthogonal()
	{
		return approximatelyEquals(getXBasis().dot(getYBasis()), 0.0, 0.01)
			&& approximatelyEquals(getXBasis().dot(getZBasis()), 0.0, 0.01)
			&& approximatelyEquals(getYBasis().dot(getZBasis()), 0.0, 0.01);
	}


	/**
	 * Return a new matrix with the result of this matrix multiplied by the provided matrix.
	 * @return this matrix.
	 */
	public Mat3d multiply(Mat3d m)
	{
		return set(
			this.m00 * m.m00 + this.m10 * m.m01 + this.m20 * m.m02,
			this.m01 * m.m00 + this.m11 * m.m01 + this.m21 * m.m02,
			this.m02 * m.m00 + this.m12 * m.m01 + this.m22 * m.m02,
			this.m00 * m.m10 + this.m10 * m.m11 + this.m20 * m.m12,
			this.m01 * m.m10 + this.m11 * m.m11 + this.m21 * m.m12,
			this.m02 * m.m10 + this.m12 * m.m11 + this.m22 * m.m12,
			this.m00 * m.m20 + this.m10 * m.m21 + this.m20 * m.m22,
			this.m01 * m.m20 + this.m11 * m.m21 + this.m21 * m.m22,
			this.m02 * m.m20 + this.m12 * m.m21 + this.m22 * m.m22
		);
	}


	/**
	 * Return a new vector with the result of this matrix multiplied with the provided vector.
	 */
	public Vec3d multiply(Vec3d source)
	{
		return new Vec3d(this.m00 * source.x + this.m10 * source.y + this.m20 * source.z,
			this.m01 * source.x + this.m11 * source.y + this.m21 * source.z,
			this.m02 * source.x + this.m12 * source.y + this.m22 * source.z);
	}


	public double determinant()
	{
		return m20 * m01 * m12 - m20 * m02 * m11 - m10 * m01 * m22 + m10 * m02 * m21 + m00 * m11 * m22 - m00 * m12 * m21;
	}


	public Mat3d inverse()
	{
		double d = determinant();

		return set(
			 ( m11 * m22 - m12 * m21) / d,
			-( m01 * m22 - m02 * m21) / d,
			 ( m01 * m12 - m02 * m11) / d,
			-(-m20 * m12 + m10 * m22) / d,
			 (-m20 * m02 + m00 * m22) / d,
			-(-m10 * m02 + m00 * m12) / d,
			 (-m20 * m11 + m10 * m21) / d,
			-(-m20 * m01 + m00 * m21) / d,
			 (-m10 * m02 + m00 * m11) / d
		);
	}


	/**
	 * Rotate this matrix by the provided angle about the specified axis.
	 *
	 * @param	angleRads	The angle to rotate the matrix, specified in radians.
	 * @param	rotationAxis	The axis to rotate this matrix about, relative to the current configuration of this matrix.
	 * @return	this matrix.
	 */
	public Mat3d rotateRads(Vec3d rotationAxis, double angleRads)
	{
		double sin = Math.sin(angleRads);
		double cos = Math.cos(angleRads);
		double oneMinusCos = 1.0 - cos;

		double xy = rotationAxis.x * rotationAxis.y;
		double yz = rotationAxis.y * rotationAxis.z;
		double xz = rotationAxis.x * rotationAxis.z;
		double xs = rotationAxis.x * sin;
		double ys = rotationAxis.y * sin;
		double zs = rotationAxis.z * sin;

		double f00 = rotationAxis.x * rotationAxis.x * oneMinusCos + cos;
		double f01 = xy * oneMinusCos + zs;
		double f02 = xz * oneMinusCos - ys;

		double f10 = xy * oneMinusCos - zs;
		double f11 = rotationAxis.y * rotationAxis.y * oneMinusCos + cos;
		double f12 = yz * oneMinusCos + xs;

		double f20 = xz * oneMinusCos + ys;
		double f21 = yz * oneMinusCos - xs;
		double f22 = rotationAxis.z * rotationAxis.z * oneMinusCos + cos;

		set(m00 * f00 + m10 * f01 + m20 * f02,
			m01 * f00 + m11 * f01 + m21 * f02,
			m02 * f00 + m12 * f01 + m22 * f02,
			m00 * f20 + m10 * f21 + m20 * f22,
			m01 * f20 + m11 * f21 + m21 * f22,
			m02 * f20 + m12 * f21 + m22 * f22,
			m00 * f10 + m10 * f11 + m20 * f12,
			m01 * f10 + m11 * f11 + m21 * f12,
			m02 * f10 + m12 * f11 + m22 * f12
		);

		return this;
	}


	/**
	 * Rotate this matrix by the provided angle about the specified axis.
	 *
	 * @param	angleDegs	The angle to rotate the matrix, specified in degrees.
	 * @param	localAxis	The axis to rotate this matrix about, relative to the current configuration of this matrix.
	 * @return	this matrix.
	 */
	public Mat3d rotateDegs(double angleDegs, Vec3d localAxis)
	{
		return rotateRads(localAxis, Math.toRadians(angleDegs));
	}


	/**
	 * Set the X basis of this matrix.
	 *
	 * @param	v	The vector to use as the X-basis of this matrix.
	 * @return	this matrix.
	 */
	public Mat3d setXBasis(Vec3d v)
	{
		m00 = v.x;
		m01 = v.y;
		m02 = v.z;
		return this;
	}


	/**
	 * Get the X basis of this matrix.
	 *
	 * @return The X basis of this matrix as a Vec3d
	 *
	 */
	public Vec3d getXBasis()
	{
		return new Vec3d(m00, m01, m02);
	}


	/**
	 * Set the Y basis of this matrix.
	 *
	 * @param	v	The vector to use as the Y-basis of this matrix.
	 * @return	this matrix.
	 */
	public Mat3d setYBasis(Vec3d v)
	{
		m10 = v.x;
		m11 = v.y;
		m12 = v.z;
		return this;
	}


	/**
	 * Get the Y basis of this matrix.
	 *
	 * @return The Y basis of this matrix as a Vec3d
	 *
	 */
	public Vec3d getYBasis()
	{
		return new Vec3d(m10, m11, m12);
	}


	/**
	 * Set the Z basis of this matrix.
	 *
	 * @param	v	The vector to use as the Z-basis of this matrix.
	 * @return	this matrix.
	 */
	public Mat3d setZBasis(Vec3d v)
	{
		m20 = v.x;
		m21 = v.y;
		m22 = v.z;
		return this;
	}


	/**
	 * Get the Z basis of this matrix.
	 *
	 * @return The Z basis of this matrix as a Vec3d
	 *
	 */
	public Vec3d getZBasis()
	{
		return new Vec3d(m20, m21, m22);
	}


	/**
	 * Return this Mat3d as an array of 9 doubles.
	 *
	 * @return	This Mat3d as an array of 9 doubles.
	 */
	public double[] toArray()
	{
		return new double[]
		{
			m00, m01, m02, m10, m11, m12, m20, m21, m22
		};
	}


	/**
	 * Note: Displays output in COLUMN-MAJOR format!
	 */
	@Override
	public String toString()
	{
		return String.format("{{%8.4f, %8.4f, %8.4f},{%8.4f, %8.4f, %8.4f},{%8.4f, %8.4f, %8.4f}}", m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}


	/**
	 * Note: Displays output in COLUMN-MAJOR format!
	 */
	public String toString2D()
	{
		return String.format("{%n\t{%8.4f, %8.4f, %8.4f}%n\t{%8.4f, %8.4f, %8.4f}%n\t{%8.4f, %8.4f, %8.4f}%n}", m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}


	public static boolean approximatelyEquals(double a, double b, double tolerance)
	{
		return Math.abs(a - b) <= tolerance;
	}


	@Override
	public Mat3d clone()
	{
		Mat3d m = new Mat3d();
		m.set(this);
		return m;
	}


	@Override
	public void readExternal(BundlableInput aInput)
	{
		Array in = aInput.array();
		m00 = in.getDouble(0);
		m01 = in.getDouble(1);
		m02 = in.getDouble(2);
		m10 = in.getDouble(3);
		m11 = in.getDouble(4);
		m12 = in.getDouble(5);
		m20 = in.getDouble(6);
		m21 = in.getDouble(7);
		m22 = in.getDouble(8);
	}


	@Override
	public void writeExternal(BundlableOutput aOutput)
	{
		aOutput.array(m00,m01,m02,m10,m11,m12,m20,m21,m22);
	}


//	private static final long serialVersionUID = 1L;
//
//	public final Vec3d[] m;
//
//
//	public Mat3d()
//	{
//		this(new Vec3d(),new Vec3d(),new Vec3d());
//	}
//
//
//	public Mat3d(Vec3d v0, Vec3d v1, Vec3d v2)
//	{
//		m = new Vec3d[3];
//		m[0] = v0;
//		m[1] = v1;
//		m[2] = v2;
//	}
//
//
//	public Mat3d(Mat3d aOther)
//	{
//		m = new Vec3d[3];
//		m[0] = aOther.m[0].clone();
//		m[1] = aOther.m[1].clone();
//		m[2] = aOther.m[2].clone();
//	}
//
//
//	public Mat3d(double... aValues)
//	{
//		m = new Vec3d[]
//		{
//			new Vec3d(aValues[0],aValues[3],aValues[6]),
//			new Vec3d(aValues[1],aValues[4],aValues[7]),
//			new Vec3d(aValues[2],aValues[5],aValues[8])
//		};
//	}
//
//
//	public Mat3d identity()
//	{
//		m[0].set(1,0,0);
//		m[1].set(0,1,0);
//		m[2].set(0,0,1);
//		return this;
//	}
//
//
//	public Mat3d transposeClone()
//	{
//		return new Mat3d(
//			new Vec3d(m[0].x, m[1].x, m[2].x),
//			new Vec3d(m[0].y, m[1].y, m[2].y),
//			new Vec3d(m[0].z, m[1].z, m[2].z));
//	}
//
//
//	public Mat3d transpose()
//	{
//		double t0 = m[0].y;
//		double t1 = m[0].z;
//		double t2 = m[1].z;
//
//		m[0].y = m[1].x;
//		m[0].z = m[2].x;
//
//		m[1].x = t0;
//		m[1].z = m[2].y;
//
//		m[2].x = t1;
//		m[2].y = t2;
//
//		return this;
//	}
//
//
//	public Vec3d scale(Vec3d q)
//	{
//		return new Vec3d(m[0].dot(q), m[1].dot(q), m[2].dot(q));
//	}
//
//
//	public static Mat3d identity2D()
//	{
//		return new Mat3d(
//			new Vec3d(1.0, 0.0, 0.0),
//			new Vec3d(0.0, 1.0, 0.0),
//			new Vec3d(0.0, 0.0, 1.0));
//	}
//
//
//	// Gauss-Jordan elimination with partial pivoting
//	public Mat3d inverse()
//	{
////		double[][] in =
////		{
////			v[0].toArray(),
////			v[1].toArray(),
////			v[2].toArray()
////		};
////		double[][] out = inverz_matrike(in);
////		v[0] = new Vec3d(out[0]);
////		v[1] = new Vec3d(out[1]);
////		v[2] = new Vec3d(out[2]);
////
////		return this;
//
//		Mat3d a = new Mat3d(this);	    // As a evolves from original mat into identity
//		Mat3d b = new Mat3d(identity2D());   // b evolves from identity into inverse(a)
//
//		// Loop over cols of a from left to right, eliminating above and below diag
//		for (int j = 0; j < 3; j++)
//		{   // Find largest pivot in column j among rows j..2
//			int i1 = j;		    // Row with largest pivot candidate
//			for (int i = j + 1; i < 3; i++)
//			{
//				if (Math.abs(a.m[i].getComponent(j)) > Math.abs(a.m[i1].getComponent(j)))
//				{
//					i1 = i;
//				}
//			}
//
//			// Swap rows i1 and j in a and b to put pivot on diagonal
//			a.m[i1].swap(a.m[j]);
//			b.m[i1].swap(b.m[j]);
//
//			// Scale row j to have a unit diagonal
//			if (a.m[j].getComponent(j) == 0)
//			{
//				throw new IllegalArgumentException("mat3::inverse: singular matrix; can't invert: " + a.m[j]);
//			}
//			b.m[j].divide(a.m[j].getComponent(j));
//			a.m[j].divide(a.m[j].getComponent(j));
//
//			// Eliminate off-diagonal elems in col j of a, doing identical ops to b
//			for (int i = 0; i < 3; i++)
//			{
//				if (i != j)
//				{
//					b.m[i].subtract(b.m[j].clone().scale(a.m[i].getComponent(j)));
//					a.m[i].subtract(a.m[j].clone().scale(a.m[i].getComponent(j)));
//				}
//			}
//		}
//		return b;
//	}
//
////	private double[][] inverz_matrike(double[][]in){
////		int st_vrs=in.length, st_stolp=in[0].length;
////		double[][]out=new double[st_vrs][st_stolp];
////		double[][]old=new double[st_vrs][st_stolp*2];
////		double[][]newX=new double[st_vrs][st_stolp*2];
////
////
////		for (int v=0;v<st_vrs;v++){//ones vector
////			for (int s=0;s<st_stolp*2;s++){
////				if (s-v==st_vrs)
////					old[v][s]=1;
////				if(s<st_stolp)
////					old[v][s]=in[v][s];
////			}
////		}
////		//zeros below the diagonal
////		for (int v=0;v<st_vrs;v++){
////			for (int v1=0;v1<st_vrs;v1++){
////				for (int s=0;s<st_stolp*2;s++){
////					if (v==v1)
////						newX[v][s]=old[v][s]/old[v][v];
////					else
////						newX[v1][s]=old[v1][s];
////				}
////			}
////			old=prepisi(newX);
////			for (int v1=v+1;v1<st_vrs;v1++){
////				for (int s=0;s<st_stolp*2;s++){
////					newX[v1][s]=old[v1][s]-old[v][s]*old[v1][v];
////				}
////			}
////			old=prepisi(newX);
////		}
////		//zeros above the diagonal
////		for (int s=st_stolp-1;s>0;s--){
////			for (int v=s-1;v>=0;v--){
////				for (int s1=0;s1<st_stolp*2;s1++){
////					newX[v][s1]=old[v][s1]-old[s][s1]*old[v][s];
////				}
////			}
////			old=prepisi(newX);
////		}
////		for (int v=0;v<st_vrs;v++){//rigt part of matrix is invers
////			for (int s=st_stolp;s<st_stolp*2;s++){
////				out[v][s-st_stolp]=newX[v][s];
////			}
////		}
////		return out;
////	}
////
////	private double[][] prepisi(double[][]in){
////		double[][]out=new double[in.length][in[0].length];
////		for(int v=0;v<in.length;v++){
////			for (int s=0;s<in[0].length;s++){
////				out[v][s]=in[v][s];
////			}
////		}
////		return out;
////	}
//
//	public Mat3d divide(double d)
//	{
//		double d_inv = 1.0 / d;
//		m[0].scale(d_inv);
//		m[1].scale(d_inv);
//		m[2].scale(d_inv);
//		return this;
//	}
//
//
//	public Mat3d subtract(double d)
//	{
//		m[0].subtract(d);
//		m[1].subtract(d);
//		m[2].subtract(d);
//		return this;
//	}
//
//
//	public double determinant()
//	{
//		double scalarA = m[0].x * m[1].y * m[2].z
//			           + m[0].y * m[1].z * m[2].x
//			           + m[0].z * m[1].x * m[2].y;
//		double scalarB = m[2].x * m[1].y * m[0].z
//			           + m[2].y * m[1].z * m[0].x
//			           + m[2].z * m[1].x * m[0].y;
//		return scalarA - scalarB;
//	}
//
//
////	public double determinantCol()
////	{
////		return v[0].x * (v[1].y * v[2].z - v[2].y * v[1].z)
////			 - v[1].x * (v[0].y * v[2].z - v[2].y * v[0].z)
////			 + v[2].x * (v[0].y * v[1].z - v[0].z * v[1].y);
////	}
//
//
//	public Mat3d setColumn(int aColumn, double... aValues)
//	{
//		for (int i = 0; i < aValues.length; i++)
//		{
//			m[aColumn].setComponent(aColumn, aValues[i]);
//		}
//
//		return this;
//	}
//
//
//	@Override
//	public String toString()
//	{
//		String s = "";
//		for (int c = 0; c < 3; c++)
//		{
//			if (c > 0)
//			{
//				s += ",";
//			}
//			s += m[c];
//		}
//		return "{" + s + "}";
//	}
}
