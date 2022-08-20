package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 26-8-2017.
 */
public class OpenCylinder extends GeometricObject
{

	protected double y0;
	protected double y1;
	protected double radius;
	private double inverseRadius;

	public OpenCylinder()
	{
		this(-1, 1, 1);
	}

	public OpenCylinder(double y0, double y1, double radius)
	{
		super();
		this.y0 = y0;
		this.y1 = y1;
		this.radius = radius;
		this.inverseRadius = 1 / radius;
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t;
		double ox = ray.o.x;
		double oy = ray.o.y;
		double oz = ray.o.z;
		double dx = ray.d.x;
		double dy = ray.d.y;
		double dz = ray.d.z;

		double a = dx * dx + dz * dz;
		double b = 2.0 * (ox * dx + oz * dz);
		double c = ox * ox + oz * oz - radius * radius;
		double disc = b * b - 4.0 * a * c ;


		if (disc < 0.0)
			return false;
		else
		{
			double e = Math.sqrt(disc);
			double denom = 2.0 * a;
			t = (-b - e) / denom;    // smaller root

			if (t > MathUtils.kEpsilon)
			{
				double yhit = oy + t * dy;

				if (yhit > y0 && yhit < y1)
				{
					tmin.set(t);

					if (sr != null)
					{
						sr.normal = new Normal3D((ox + t * dx) * inverseRadius, 0.0, (oz + t * dz) * inverseRadius);
						sr.localHitPoint = ray.o.add(ray.d.multiply(t));

						if (ray.d.negate().dot(sr.normal) < 0.0)
							sr.normal = sr.normal.negate();
					}

					return true;
				}
			}

			t = (-b + e) / denom;    // larger root

			if (t > MathUtils.kEpsilon)
			{
				double yhit = oy + t * dy;

				if (yhit > y0 && yhit < y1)
				{
					tmin.set(t);

					if (sr != null)
					{
						sr.normal = new Normal3D((ox + t * dx) * inverseRadius, 0.0, (oz + t * dz) * inverseRadius);
						sr.localHitPoint = ray.o.add(ray.d.multiply(t));

						if (ray.d.negate().dot(sr.normal) < 0.0)
							sr.normal = sr.normal.negate();

					}

					return true;
				}
			}
		}

		return false;
	}

	public void setY0(double y0)
	{
		this.y0 = y0;
	}

	public void setY1(double y1)
	{
		this.y1 = y1;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
		this.inverseRadius = 1 / radius;
	}
}
