package org.terifan.algebra;


public class mat3
{
	private Vec3d[] v;


	public mat3(Vec3d v0, Vec3d v1, Vec3d v2)
	{
		v = new Vec3d[3];
		v[0] = v0;
		v[1] = v1;
		v[2] = v2;
	}


	public mat3(mat3 m)
	{
		v = new Vec3d[3];
		v[0] = m.v[0].clone();
		v[1] = m.v[1].clone();
		v[2] = m.v[2].clone();
	}


	public mat3 transposeClone()
	{
		return new mat3(
			new Vec3d(v[0].x, v[1].x, v[2].x),
			new Vec3d(v[0].y, v[1].y, v[2].y),
			new Vec3d(v[0].z, v[1].z, v[2].z));
	}


	public mat3 transpose()
	{
		double t0 = v[0].y;
		double t1 = v[0].z;
		double t2 = v[1].z;

		v[0].y = v[1].x;
		v[0].z = v[2].x;

		v[1].x = t0;
		v[1].z = v[2].y;

		v[2].x = t1;
		v[2].y = t2;

		return this;
	}


	public Vec3d scale(Vec3d q)
	{
		return new Vec3d(v[0].dot(q), v[1].dot(q), v[2].dot(q));
	}


	public static mat3 identity2D()
	{
		return new mat3(
			new Vec3d(1.0, 0.0, 0.0),
			new Vec3d(0.0, 1.0, 0.0),
			new Vec3d(0.0, 0.0, 1.0));
	}


	// Gauss-Jordan elimination with partial pivoting
	public mat3 inverse()
	{
		mat3 a = new mat3(this);	    // As a evolves from original mat into identity
		mat3 b = new mat3(identity2D());   // b evolves from identity into inverse(a)

		// Loop over cols of a from left to right, eliminating above and below diag
		for (int j = 0; j < 3; j++)
		{   // Find largest pivot in column j among rows j..2
			int i1 = j;		    // Row with largest pivot candidate
			for (int i = j + 1; i < 3; i++)
			{
				if (Math.abs(a.v[i].getComponent(j)) > Math.abs(a.v[i1].getComponent(j)))
				{
					i1 = i;
				}
			}

			// Swap rows i1 and j in a and b to put pivot on diagonal
			a.v[i1].swap(a.v[j]);
			b.v[i1].swap(b.v[j]);

			// Scale row j to have a unit diagonal
			if (a.v[j].getComponent(j) == 0)
			{
				throw new IllegalArgumentException("mat3::inverse: singular matrix; can't invert: " + a.v[j]);
			}
			b.v[j].divide(a.v[j].getComponent(j));
			a.v[j].divide(a.v[j].getComponent(j));

			// Eliminate off-diagonal elems in col j of a, doing identical ops to b
			for (int i = 0; i < 3; i++)
			{
				if (i != j)
				{
					b.v[i].subtract(b.v[j].clone().scale(a.v[i].getComponent(j)));
					a.v[i].subtract(a.v[j].clone().scale(a.v[i].getComponent(j)));
				}
			}
		}
		return b;
	}


	public mat3 divide(double d)
	{
		double d_inv = 1.0 / d;
		v[0].scale(d_inv);
		v[1].scale(d_inv);
		v[2].scale(d_inv);
		return this;
	}


	public mat3 subtract(double d)
	{
		v[0].subtract(d);
		v[1].subtract(d);
		v[2].subtract(d);
		return this;
	}


	public double determinant()
	{
		double scalarA = v[0].x * v[1].y * v[2].z
			           + v[0].y * v[1].z * v[2].x
			           + v[0].z * v[1].x * v[2].y;
		double scalarB = v[2].x * v[1].y * v[0].z
			           + v[2].y * v[1].z * v[0].x
			           + v[2].z * v[1].x * v[0].y;
		return scalarA - scalarB;
	}


//	public double determinantCol()
//	{
//		return v[0].x * (v[1].y * v[2].z - v[2].y * v[1].z)
//			 - v[1].x * (v[0].y * v[2].z - v[2].y * v[0].z)
//			 + v[2].x * (v[0].y * v[1].z - v[0].z * v[1].y);
//	}
}
