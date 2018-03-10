package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 24-8-2017.
 */
public class Plane extends GeometricObject
{
	protected Point3D p0;
	protected Normal3D normal;

	public Plane()
	{
		this(Point3D.O, Normal3D.Y);
	}

	public Plane(Point3D center, Normal3D normal)
	{
		super();
		this.p0 = center;
		this.normal = normal;
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t = p0.subtract(ray.o).dot(normal) / ray.d.dot(normal);

		if (t <= MathUtils.kEpsilon)
			return false;

		tmin.set(t);

		if (sr != null)
		{
			sr.normal = normal;
			sr.localHitPoint = ray.o.add(ray.d.multiply(t));
		}

		return true;
	}
}
