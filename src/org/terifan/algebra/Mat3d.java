package org.terifan.algebra;

import java.io.Serializable;


public class Mat3d implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final Vec3d[] m;


	public Mat3d()
	{
		this(new Vec3d(),new Vec3d(),new Vec3d());
	}


	public Mat3d(Vec3d v0, Vec3d v1, Vec3d v2)
	{
		m = new Vec3d[3];
		m[0] = v0;
		m[1] = v1;
		m[2] = v2;
	}


	public Mat3d(Mat3d aOther)
	{
		m = new Vec3d[3];
		m[0] = aOther.m[0].clone();
		m[1] = aOther.m[1].clone();
		m[2] = aOther.m[2].clone();
	}


	public Mat3d(double... aValues)
	{
		m = new Vec3d[]
		{
			new Vec3d(aValues[0],aValues[3],aValues[6]),
			new Vec3d(aValues[1],aValues[4],aValues[7]),
			new Vec3d(aValues[2],aValues[5],aValues[8])
		};
	}


	public Mat3d transposeClone()
	{
		return new Mat3d(
			new Vec3d(m[0].x, m[1].x, m[2].x),
			new Vec3d(m[0].y, m[1].y, m[2].y),
			new Vec3d(m[0].z, m[1].z, m[2].z));
	}


	public Mat3d transpose()
	{
		double t0 = m[0].y;
		double t1 = m[0].z;
		double t2 = m[1].z;

		m[0].y = m[1].x;
		m[0].z = m[2].x;

		m[1].x = t0;
		m[1].z = m[2].y;

		m[2].x = t1;
		m[2].y = t2;

		return this;
	}


	public Vec3d scale(Vec3d q)
	{
		return new Vec3d(m[0].dot(q), m[1].dot(q), m[2].dot(q));
	}


	public static Mat3d identity2D()
	{
		return new Mat3d(
			new Vec3d(1.0, 0.0, 0.0),
			new Vec3d(0.0, 1.0, 0.0),
			new Vec3d(0.0, 0.0, 1.0));
	}


	// Gauss-Jordan elimination with partial pivoting
	public Mat3d inverse()
	{
//		double[][] in =
//		{
//			v[0].toArray(),
//			v[1].toArray(),
//			v[2].toArray()
//		};
//		double[][] out = inverz_matrike(in);
//		v[0] = new Vec3d(out[0]);
//		v[1] = new Vec3d(out[1]);
//		v[2] = new Vec3d(out[2]);
//
//		return this;

		Mat3d a = new Mat3d(this);	    // As a evolves from original mat into identity
		Mat3d b = new Mat3d(identity2D());   // b evolves from identity into inverse(a)

		// Loop over cols of a from left to right, eliminating above and below diag
		for (int j = 0; j < 3; j++)
		{   // Find largest pivot in column j among rows j..2
			int i1 = j;		    // Row with largest pivot candidate
			for (int i = j + 1; i < 3; i++)
			{
				if (Math.abs(a.m[i].getComponent(j)) > Math.abs(a.m[i1].getComponent(j)))
				{
					i1 = i;
				}
			}

			// Swap rows i1 and j in a and b to put pivot on diagonal
			a.m[i1].swap(a.m[j]);
			b.m[i1].swap(b.m[j]);

			// Scale row j to have a unit diagonal
			if (a.m[j].getComponent(j) == 0)
			{
				throw new IllegalArgumentException("mat3::inverse: singular matrix; can't invert: " + a.m[j]);
			}
			b.m[j].divide(a.m[j].getComponent(j));
			a.m[j].divide(a.m[j].getComponent(j));

			// Eliminate off-diagonal elems in col j of a, doing identical ops to b
			for (int i = 0; i < 3; i++)
			{
				if (i != j)
				{
					b.m[i].subtract(b.m[j].clone().scale(a.m[i].getComponent(j)));
					a.m[i].subtract(a.m[j].clone().scale(a.m[i].getComponent(j)));
				}
			}
		}
		return b;
	}

//	private double[][] inverz_matrike(double[][]in){
//		int st_vrs=in.length, st_stolp=in[0].length;
//		double[][]out=new double[st_vrs][st_stolp];
//		double[][]old=new double[st_vrs][st_stolp*2];
//		double[][]newX=new double[st_vrs][st_stolp*2];
//
//
//		for (int v=0;v<st_vrs;v++){//ones vector
//			for (int s=0;s<st_stolp*2;s++){
//				if (s-v==st_vrs)
//					old[v][s]=1;
//				if(s<st_stolp)
//					old[v][s]=in[v][s];
//			}
//		}
//		//zeros below the diagonal
//		for (int v=0;v<st_vrs;v++){
//			for (int v1=0;v1<st_vrs;v1++){
//				for (int s=0;s<st_stolp*2;s++){
//					if (v==v1)
//						newX[v][s]=old[v][s]/old[v][v];
//					else
//						newX[v1][s]=old[v1][s];
//				}
//			}
//			old=prepisi(newX);
//			for (int v1=v+1;v1<st_vrs;v1++){
//				for (int s=0;s<st_stolp*2;s++){
//					newX[v1][s]=old[v1][s]-old[v][s]*old[v1][v];
//				}
//			}
//			old=prepisi(newX);
//		}
//		//zeros above the diagonal
//		for (int s=st_stolp-1;s>0;s--){
//			for (int v=s-1;v>=0;v--){
//				for (int s1=0;s1<st_stolp*2;s1++){
//					newX[v][s1]=old[v][s1]-old[s][s1]*old[v][s];
//				}
//			}
//			old=prepisi(newX);
//		}
//		for (int v=0;v<st_vrs;v++){//rigt part of matrix is invers
//			for (int s=st_stolp;s<st_stolp*2;s++){
//				out[v][s-st_stolp]=newX[v][s];
//			}
//		}
//		return out;
//	}
//
//	private double[][] prepisi(double[][]in){
//		double[][]out=new double[in.length][in[0].length];
//		for(int v=0;v<in.length;v++){
//			for (int s=0;s<in[0].length;s++){
//				out[v][s]=in[v][s];
//			}
//		}
//		return out;
//	}

	public Mat3d divide(double d)
	{
		double d_inv = 1.0 / d;
		m[0].scale(d_inv);
		m[1].scale(d_inv);
		m[2].scale(d_inv);
		return this;
	}


	public Mat3d subtract(double d)
	{
		m[0].subtract(d);
		m[1].subtract(d);
		m[2].subtract(d);
		return this;
	}


	public double determinant()
	{
		double scalarA = m[0].x * m[1].y * m[2].z
			           + m[0].y * m[1].z * m[2].x
			           + m[0].z * m[1].x * m[2].y;
		double scalarB = m[2].x * m[1].y * m[0].z
			           + m[2].y * m[1].z * m[0].x
			           + m[2].z * m[1].x * m[0].y;
		return scalarA - scalarB;
	}


//	public double determinantCol()
//	{
//		return v[0].x * (v[1].y * v[2].z - v[2].y * v[1].z)
//			 - v[1].x * (v[0].y * v[2].z - v[2].y * v[0].z)
//			 + v[2].x * (v[0].y * v[1].z - v[0].z * v[1].y);
//	}

	
	public Mat3d setColumn(int aColumn, double... aValues)
	{
		for (int i = 0; i < aValues.length; i++)
		{
			m[aColumn].setComponent(aColumn, aValues[i]);
		}

		return this;
	}
	

	@Override
	public String toString()
	{
		String s = "";
		for (int c = 0; c < 3; c++)
		{
			if (c > 0)
			{
				s += ",";
			}
			s += m[c];
		}
		return "{" + s + "}";
	}
}
