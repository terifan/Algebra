package org.terifan.algebra;


public class Halton
{
	private double value, inv_base;


	public Halton number(int i, int base)
	{
		double f = inv_base = 1.0 / base;
		value = 0.0;
		while (i > 0)
		{
			value += f * (double)(i % base);
			i /= base;
			f *= inv_base;
		}
		return this;
	}


	public void next()
	{
		double r = 1.0 - value - 0.0000001;
		if (inv_base < r)
		{
			value += inv_base;
		}
		else
		{
			double h = inv_base, hh;
			do
			{
				hh = h;
				h *= inv_base;
			}
			while (h >= r);
			value += hh + h - 1.0;
		}
	}


	public double get()
	{
		System.out.println(value);
		return value;
	}
}
