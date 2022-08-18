package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 24-8-2017.
 */
public class Torus extends GeometricObject
{
	protected double a; // radius
	protected double b; // circle radius
	protected BBox bbox;

	public Torus()
	{
		this(2, 0.5);
	}

	public Torus(double a, double b)
	{
		super();

		this.a = a;
		this.b = b;

		this.bbox = new BBox(
				new Point3D(
						-a - b,
						-b,
						-a - b
				),
				new Point3D(
						a + b,
						b,
						a + b
				)
		);
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		if (!bbox.hit(ray))
			return (false);

		double x1 = ray.o.x, y1 = ray.o.y, z1 = ray.o.z;
		double d1 = ray.d.x, d2 = ray.d.y, d3 = ray.d.z;

		double[] coeffs = new double[5]; // coefficient array
		double[] roots = new double[4];    // soluction array

		// define the coefficients

		double sum_d_sqrd = d1 * d1 + d2 * d2 + d3 * d3;
		double e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
		double f = x1 * d1 + y1 * d2 + z1 * d3;
		double four_a_sqrd = 4.0 * a * a;

		coeffs[0] = e * e - four_a_sqrd * (b * b - y1 * y1);    // constant term
		coeffs[1] = 4.0 * f * e + 2.0 * four_a_sqrd * y1 * d2;
		coeffs[2] = 2.0 * sum_d_sqrd * e + 4.0 * f * f + four_a_sqrd * d2 * d2;
		coeffs[3] = 4.0 * sum_d_sqrd * f;
		coeffs[4] = sum_d_sqrd * sum_d_sqrd;                    // coefficient of t^4

		// find the roots

		int num_real_roots = MathUtils.solveQuartic(coeffs, roots);

		boolean intersected = false;
		double t = MathUtils.kHugeValue;

		if (num_real_roots == 0) //ray misses the torus
			return false;

		// find the smallest root greater than kEpsilon, if any

		for (int i = 0; i < num_real_roots; i++)
			if (roots[i] > MathUtils.kEpsilon)
			{
				intersected = true;
				if (roots[i] < t)
					t = roots[i];
			}

		if (!intersected)
			return false;

		tmin.set(t);

		if (sr != null)
		{
			sr.localHitPoint = ray.o.add(ray.d.multiply(t));
			sr.normal = computeNormal(sr.localHitPoint);
		}

		return true;
	}

	private Normal3D computeNormal(Point3D hitPoint)
	{
		double x = hitPoint.x;
		double y = hitPoint.y;
		double z = hitPoint.z;

		double c = x * x + y * y + z * z;
		double d = a * a + b * b;

		double nx = 4 * x * (c - d);
		double ny = 4 * y * (c - d + 2 * a * a);
		double nz = 4 * z * (c - d);

		return new Normal3D(nx, ny, nz);
	}
}
