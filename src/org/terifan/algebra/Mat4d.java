package org.terifan.algebra;


public class Mat4d
{
	private final static int m00 = 0 + 4 * 0;
	private final static int m01 = 0 + 4 * 1;
	private final static int m02 = 0 + 4 * 2;
	private final static int m03 = 0 + 4 * 3;
	private final static int m10 = 1 + 4 * 0;
	private final static int m11 = 1 + 4 * 1;
	private final static int m12 = 1 + 4 * 2;
	private final static int m13 = 1 + 4 * 3;
	private final static int m20 = 2 + 4 * 0;
	private final static int m21 = 2 + 4 * 1;
	private final static int m22 = 2 + 4 * 2;
	private final static int m23 = 2 + 4 * 3;
	private final static int m30 = 3 + 4 * 0;
	private final static int m31 = 3 + 4 * 1;
	private final static int m32 = 3 + 4 * 2;
	private final static int m33 = 3 + 4 * 3;

	private double[] m = new double[4 * 4];


	public double get(int r, int c)
	{
		return m[r + c * 4];
	}


	public void set(int r, int c, double v)
	{
		m[r + 4 * c] = v;
	}


	public Mat4d identity()
	{
		for (int i = 0; i < 4; i++)
		{
			set(i, i, 1);
		}
		return this;
	}


	public void setRow(int r, double a, double b, double c, double d)
	{
		set(r, 0, a);
		set(r, 1, b);
		set(r, 2, c);
		set(r, 3, d);
	}


	public void setRow(int r, Vec3d a, double b)
	{
		for (int i = 0; i < 3; i++)
		{
			set(r, i, a.get(i));
		}

		set(r, 3, b);
	}


	public Vec3d transformPoint(Vec3d aVec)
	{
		double w = get(3, 3);

		for (int c = 0; c < 3; c++)
		{
			w += get(3, c) * aVec.get(c);
		}

		if (w == 0)
		{
			throw new Error("div 0");
		}

		double invW = 1.0 / w;

		Vec3d res = new Vec3d(0);

		for (int r = 0; r < 3; r++)
		{
			res.set(r, get(r, 3));

			for (int c = 0; c < 3; c++)
			{
				res.add(r, aVec.get(c) * get(r, c));
			}

			res.scale(r, invW);
		}

		return res;
	}


	public Vec3d transformPoint(double x, double y, double z)
	{
		double w = get(3, 3);

		w += get(3, 0) * x;
		w += get(3, 1) * y;
		w += get(3, 2) * z;

		if (w == 0)
		{
			throw new Error("div 0");
		}

		double invW = 1.0 / w;

		Vec3d res = new Vec3d(0);

		for (int r = 0; r < 3; r++)
		{
			res.set(r, get(r, 3));

			res.add(r, x * get(r, 0));
			res.add(r, y * get(r, 1));
			res.add(r, z * get(r, 2));

			res.scale(r, invW);
		}

		return res;
	}


	public static Mat4d scale(Mat4d left, Mat4d right)
	{
		Mat4d res = new Mat4d();
		for (int row = 0; row < 4; row++)
		{
			for (int col = 0; col < 4; col++)
			{
				for (int i = 0; i < 4; i++)
				{
					res.set(row, col, res.get(row, col) + left.get(row, i) * right.get(i, col));
				}
			}
		}

		return res;
	}


	public Mat4d invert()
	{
		double[] inv = new double[4 * 4];
		double det;
		int i;

		inv[0] = m[5] * m[10] * m[15]
			- m[5] * m[11] * m[14]
			- m[9] * m[6] * m[15]
			+ m[9] * m[7] * m[14]
			+ m[13] * m[6] * m[11]
			- m[13] * m[7] * m[10];

		inv[4] = -m[4] * m[10] * m[15]
			+ m[4] * m[11] * m[14]
			+ m[8] * m[6] * m[15]
			- m[8] * m[7] * m[14]
			- m[12] * m[6] * m[11]
			+ m[12] * m[7] * m[10];

		inv[8] = m[4] * m[9] * m[15]
			- m[4] * m[11] * m[13]
			- m[8] * m[5] * m[15]
			+ m[8] * m[7] * m[13]
			+ m[12] * m[5] * m[11]
			- m[12] * m[7] * m[9];

		inv[12] = -m[4] * m[9] * m[14]
			+ m[4] * m[10] * m[13]
			+ m[8] * m[5] * m[14]
			- m[8] * m[6] * m[13]
			- m[12] * m[5] * m[10]
			+ m[12] * m[6] * m[9];

		inv[1] = -m[1] * m[10] * m[15]
			+ m[1] * m[11] * m[14]
			+ m[9] * m[2] * m[15]
			- m[9] * m[3] * m[14]
			- m[13] * m[2] * m[11]
			+ m[13] * m[3] * m[10];

		inv[5] = m[0] * m[10] * m[15]
			- m[0] * m[11] * m[14]
			- m[8] * m[2] * m[15]
			+ m[8] * m[3] * m[14]
			+ m[12] * m[2] * m[11]
			- m[12] * m[3] * m[10];

		inv[9] = -m[0] * m[9] * m[15]
			+ m[0] * m[11] * m[13]
			+ m[8] * m[1] * m[15]
			- m[8] * m[3] * m[13]
			- m[12] * m[1] * m[11]
			+ m[12] * m[3] * m[9];

		inv[13] = m[0] * m[9] * m[14]
			- m[0] * m[10] * m[13]
			- m[8] * m[1] * m[14]
			+ m[8] * m[2] * m[13]
			+ m[12] * m[1] * m[10]
			- m[12] * m[2] * m[9];

		inv[2] = m[1] * m[6] * m[15]
			- m[1] * m[7] * m[14]
			- m[5] * m[2] * m[15]
			+ m[5] * m[3] * m[14]
			+ m[13] * m[2] * m[7]
			- m[13] * m[3] * m[6];

		inv[6] = -m[0] * m[6] * m[15]
			+ m[0] * m[7] * m[14]
			+ m[4] * m[2] * m[15]
			- m[4] * m[3] * m[14]
			- m[12] * m[2] * m[7]
			+ m[12] * m[3] * m[6];

		inv[10] = m[0] * m[5] * m[15]
			- m[0] * m[7] * m[13]
			- m[4] * m[1] * m[15]
			+ m[4] * m[3] * m[13]
			+ m[12] * m[1] * m[7]
			- m[12] * m[3] * m[5];

		inv[14] = -m[0] * m[5] * m[14]
			+ m[0] * m[6] * m[13]
			+ m[4] * m[1] * m[14]
			- m[4] * m[2] * m[13]
			- m[12] * m[1] * m[6]
			+ m[12] * m[2] * m[5];

		inv[3] = -m[1] * m[6] * m[11]
			+ m[1] * m[7] * m[10]
			+ m[5] * m[2] * m[11]
			- m[5] * m[3] * m[10]
			- m[9] * m[2] * m[7]
			+ m[9] * m[3] * m[6];

		inv[7] = m[0] * m[6] * m[11]
			- m[0] * m[7] * m[10]
			- m[4] * m[2] * m[11]
			+ m[4] * m[3] * m[10]
			+ m[8] * m[2] * m[7]
			- m[8] * m[3] * m[6];

		inv[11] = -m[0] * m[5] * m[11]
			+ m[0] * m[7] * m[9]
			+ m[4] * m[1] * m[11]
			- m[4] * m[3] * m[9]
			- m[8] * m[1] * m[7]
			+ m[8] * m[3] * m[5];

		inv[15] = m[0] * m[5] * m[10]
			- m[0] * m[6] * m[9]
			- m[4] * m[1] * m[10]
			+ m[4] * m[2] * m[9]
			+ m[8] * m[1] * m[6]
			- m[8] * m[2] * m[5];

		det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

		if (det == 0)
		{
			return new Mat4d().identity();
		}

		det = 1.f / det;

		Mat4d res = new Mat4d();
		for (i = 0; i < 16; i++)
		{
			res.m[i] = inv[i] * det;
		}

		return res;
	}


	public static Mat4d scale(Vec3d aScale)
	{
		Mat4d res = new Mat4d().identity();
		for (int i = 0; i < 3; i++)
		{
			res.set(i, i, aScale.get(i));
		}
		res.set(3, 3, 1);
		return res;
	}


	public static Mat4d translate(Vec3d aScale)
	{
		Mat4d res = new Mat4d().identity();
		for (int i = 0; i < 3; i++)
		{
			res.set(i, 3, aScale.get(i));
		}
		res.set(3, 3, 1);
		return res;
	}


	public static Mat4d perspective(double aFov, double aNear, double aFar)
	{
		// Camera points towards -z.  0 < near < far.
		// Matrix maps z range [-near, -far] to [-1, 1], after homogeneous division.
		double f = 1.0 / (Math.tan(aFov * Math.PI / 360.0));
		double d = 1.0 / (aNear - aFar);

		Mat4d r = new Mat4d();
		r.m[m00] = f;
		r.m[m01] = 0.0f;
		r.m[m02] = 0.0f;
		r.m[m03] = 0.0f;
		r.m[m10] = 0.0f;
		r.m[m11] = -f;
		r.m[m12] = 0.0f;
		r.m[m13] = 0.0f;
		r.m[m20] = 0.0f;
		r.m[m21] = 0.0f;
		r.m[m22] = (aNear + aFar) * d;
		r.m[m23] = 2.0f * aNear * aFar * d;
		r.m[m30] = 0.0f;
		r.m[m31] = 0.0f;
		r.m[m32] = -1.0f;
		r.m[m33] = 0.0f;

		return r;
	}


	@Override
	public String toString()
	{
		String s = "";
		for (int c = 0; c < 4; c++)
		{
			if (c > 0)
			{
				s += ",";
			}
			s += "{";
			for (int r = 0; r < 4; r++)
			{
				if (r > 0)
				{
					s += ",";
				}
				s += get(r, c);
			}
			s += "}";
		}
		return "{" + s + "}";
	}
}
