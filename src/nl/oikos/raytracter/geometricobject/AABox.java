package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 26-8-2017.
 */
public class AABox extends GeometricObject
{

	protected Point3D point0;
	protected Point3D point1;

	protected static final double kEpsilon = 0.001;

	public AABox()
	{
		this(Point3D.O, Point3D.I);
	}

	public AABox(Point3D point0, Point3D point1)
	{
		super();
		this.point0 = point0;
		this.point1 = point1;
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
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
		int faceIn, faceOut;

		// find largest entering t value

		if (txMin > tyMin)
		{
			t0 = txMin;
			faceIn = (a >= 0.0) ? 0 : 3;
		}
		else
		{
			t0 = tyMin;
			faceIn = (b >= 0.0) ? 1 : 4;
		}
		if (tzMin > t0)
		{
			t0 = tzMin;
			faceIn = (c >= 0.0) ? 2 : 5;
		}

		// find smallest exiting t value

		if (txMax < tyMax)
		{
			t1 = txMax;
			faceOut = (a >= 0.0) ? 3 : 0;
		}
		else
		{
			t1 = tyMax;
			faceOut = (b >= 0.0) ? 4 : 1;
		}
		if (tzMax < t1)
		{
			t1 = tzMax;
			faceOut = (c >= 0.0) ? 5 : 2;
		}


		if (t0 < t1 && t1 > kEpsilon)
		{        // condition for a hit
			if (t0 > kEpsilon)
			{
				tmin.set(t0);
				sr.normal = getNormal(faceIn);
			}
			else
			{
				tmin.set(t1);
				sr.normal = getNormal(faceOut);
			}

			sr.localHitPoint = ray.o.add(ray.d.multiply(tmin.get()));
			return true;
		}

		return false;
	}

	private Normal3D getNormal(int faceHit)
	{
		switch (faceHit)
		{
			case 0:
				return new Normal3D(-1, 0, 0); // -x face
			case 1:
				return new Normal3D(0, -1, 0); // -y face
			case 2:
				return new Normal3D(0, 0, -1); // -z face
			case 3:
				return new Normal3D(1, 0, 0);  // +x face
			case 4:
				return new Normal3D(0, 1, 0);  // +y face
			case 5:
				return new Normal3D(0, 0, 1);  // +z face
		}
		return null;
	}

	public void setPoint0(Point3D point0)
	{
		this.point0 = point0;
	}

	public void setPoint0(double x, double y, double z)
	{
		this.point0 = new Point3D(x, y, z);
	}

	public void setPoint1(Point3D point1)
	{
		this.point1 = point1;
	}

	public void setPoint1(double x, double y, double z)
	{
		this.point1 = new Point3D(x, y, z);
	}
}