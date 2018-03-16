package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 11-3-2018.
 */
public class Disk extends Plane implements GeometricLightObject
{
	protected double radius;
	private double rSquared;

	private Sampler sampler;
	private double inverseArea;

	public Disk()
	{
		super();

		this.radius = 1;
		this.rSquared = 1;
		this.inverseArea = MathUtils.INV_PI;
	}

	public Disk(Point3D center, Normal3D normal)
	{
		this(center, normal, 1);
	}

	public Disk(Point3D center, Normal3D normal, double radius)
	{
		super(center, normal);

		this.radius = radius;
		this.rSquared = radius * radius;
		this.inverseArea = 1 / (rSquared * Math.PI);
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t = p0.subtract(ray.o).dot(normal) / ray.d.dot(normal);

		if (t <= MathUtils.kEpsilon)
			return false;

		Point3D p = ray.o.add(ray.d.multiply(t));

		if (p0.distanceSqrd(p) >= rSquared)
			return false;

		tmin.set(t);

		if (sr != null)
		{
			sr.normal = normal;
			sr.localHitPoint = p;
		}

		return true;
	}

	@Override
	public void setSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.sampler.mapSamplesToUnitDisk();
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		this.sampler.setNumberOfSamples(numberOfSamples);
		this.sampler.initialize();
		this.sampler.mapSamplesToUnitDisk();
	}

	@Override
	public Point3D sample(ShadeRec sr)
	{
		Point2D samplePoint = sampler.sampleUnitDisk(sr).multiply(radius);

		double y2 = normal.y * normal.y;
		double z2 = normal.z * normal.z;

		Vector3D u = new Vector3D(0, -normal.z, normal.y);
		Vector3D v = new Vector3D(y2 + z2, -normal.x * normal.y, -normal.x * normal.z);

		return p0.add(u.multiply(samplePoint.x)).add(v.multiply(samplePoint.y));
	}

	@Override
	public double pdf(ShadeRec shadeRec)
	{
		return this.inverseArea;
	}

	@Override
	public Normal3D getNormal(Point3D p)
	{
		return this.normal;
	}
}
