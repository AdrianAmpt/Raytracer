package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 26-8-2017.
 */
public class Sphere extends GeometricObject implements GeometricLightObject
{

	protected Point3D center;
	protected double radius;
	private Sampler sampler;
	private double inverseArea;

	public Sphere()
	{
		this(Point3D.O, 1);
	}

	public Sphere(Point3D center, double radius)
	{
		super();
		this.center = center;
		this.radius = radius;
		this.inverseArea = 1 / (4 * Math.PI * radius * radius);
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t;
		Vector3D temp = ray.o.subtract(center);
		double a = ray.d.dot(ray.d);
		double b = 2 * temp.dot(ray.d);
		double c = temp.dot(temp) - radius * radius;
		double disc = b * b - 4 * a * c;

		if (disc < 0.0)
		{
			return false;
		}
		else
		{
			double e = Math.sqrt(disc);
			double denom = 2 * a;
			t = (-b - e) / denom;

			if (t > MathUtils.kEpsilon)
			{
				tmin.set(t);
				if (sr != null)
				{
					sr.normal = new Normal3D(temp.add(ray.d.multiply(t)).divide(radius));
					sr.localHitPoint = ray.o.add(ray.d.multiply(t));
				}

				return true;
			}

			t = (-b + e) / denom;

			if (t > MathUtils.kEpsilon)
			{
				tmin.set(t);
				if (sr != null)
				{
					sr.normal = new Normal3D(temp.add(ray.d.multiply(t)).divide(radius));
					sr.localHitPoint = ray.o.add(ray.d.multiply(t));
				}
				return true;
			}
		}

		return false;
	}

	public void setCenter(Point3D center)
	{
		this.center = center;
	}

	public void setCenter(double x, double y, double z)
	{
		this.center = new Point3D(x, y, z);
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}

	@Override
	public void setSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.sampler.mapSamplesToSphere();
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		this.sampler.setNumberOfSamples(numberOfSamples);
		this.sampler.initialize();
		this.sampler.mapSamplesToSphere();
	}

	@Override
	public Point3D sample(ShadeRec sr)
	{
		return center.add(sampler.sampleSphere(sr).multiply(radius));
	}

	@Override
	public double pdf(ShadeRec shadeRec)
	{
		return this.inverseArea;
	}

	@Override
	public Normal3D getNormal(Point3D p)
	{
		Vector3D lineSegment =  p.subtract(center);

		return new Normal3D(lineSegment.divide(radius));
	}
}
