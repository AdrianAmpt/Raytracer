package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 26-8-2017.
 */
public final class BBox
{
	private final Point3D point0;
	private final Point3D point1;

	public BBox()
	{
		this(Point3D.O, Point3D.I);
	}

	public BBox(Point3D point0, Point3D point1)
	{
		this.point0 = point0;
		this.point1 = point1;
	}

	public boolean hit(Ray ray)
	{
		double ox = ray.o.x; double oy = ray.o.y; double oz = ray.o.z;
		double dx = ray.d.x; double dy = ray.d.y; double dz = ray.d.z;

		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;

		double a = 1.0 / dx;
		if (a >= 0)
		{
			txMin = (point0.x - ox) * a;
			txMax = (point1.x - ox) * a;
		}
		else
		{
			txMin = (point1.x - ox) * a;
			txMax = (point0.x - ox) * a;
		}

		double b = 1.0 / dy;
		if (b >= 0)
		{
			tyMin = (point0.y - oy) * b;
			tyMax = (point1.y - oy) * b;
		}
		else
		{
			tyMin = (point1.y - oy) * b;
			tyMax = (point0.y - oy) * b;
		}

		double c = 1.0 / dz;
		if (c >= 0)
		{
			tzMin = (point0.z - oz) * c;
			tzMax = (point1.z - oz) * c;
		}
		else
		{
			tzMin = (point1.z - oz) * c;
			tzMax = (point0.z - oz) * c;
		}

		double t0, t1;

		// find largest entering t value

		if (txMin > tyMin)
			t0 = txMin;
		else
			t0 = tyMin;

		if (tzMin > t0)
			t0 = tzMin;

		// find smallest exiting t value

		if (txMax < tyMax)
			t1 = txMax;
		else
			t1 = tyMax;

		if (tzMax < t1)
			t1 = tzMax;

		return t0 < t1 && t1 > MathUtils.kEpsilon;
	}
}