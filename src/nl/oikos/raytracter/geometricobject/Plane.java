package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 24-8-2017.
 */
public class Plane extends GeometricObject
{
	protected Point3D center;
	protected Normal3D normal;

	protected static final double kEpsilon = 0.001;

	public Plane()
	{
		this(Point3D.O, Normal3D.Y);
	}

	public Plane(Point3D center, Normal3D normal)
	{
		super();
		this.center = center;
		this.normal = normal;
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t = center.subtract(ray.o).dot(normal) / ray.d.dot(normal);

		if (t > kEpsilon)
		{
			tmin.set(t);
			if (sr != null)
			{
				sr.normal = normal;
				sr.localHitPoint = ray.o.add(ray.d.multiply(t));
			}
			return true;
		}

		return false;
	}
}
