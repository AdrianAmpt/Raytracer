package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 10-3-2018.
 */
public class Rectangle extends Plane implements GeometricLightObject
{

	protected Vector3D a;
	protected Vector3D b;
	private double aLengthSquared;
	private double bLengthSquared;
	private Sampler sampler;
	private double inverseArea;

	public Rectangle()
	{
		super(new Point3D(-0.5, -0.5, 0), Normal3D.Y);

		this.a = new Vector3D(1, 0, 0);
		this.aLengthSquared = 1;
		this.b = new Vector3D(0, 0, 1);
		this.bLengthSquared = 1;
	}

	public Rectangle(Point3D p0, Vector3D a, Vector3D b)
	{
		super(p0, new Normal3D(a.cross(b).normalize()));

		this.a = a;
		this.aLengthSquared = a.lengthSqrd();
		this.b = b;
		this.bLengthSquared = b.lengthSqrd();
		this.inverseArea = 1 / (a.length() * b.length());
	}

	public Rectangle(Point3D p0, Vector3D a, Vector3D b, Normal3D normal)
	{
		this(p0, a, b);

		if (this.normal.equals(normal) || this.normal.negate().equals(normal))
		{
			this.normal = normal;
		}
		else
		{
			throw new IllegalArgumentException("Normal is not perpendicular to a and b: " + this.normal + " vs " + normal);
		}
	}

	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double t = p0.subtract(ray.o).dot(normal) / ray.d.dot(normal);

		if (t <= MathUtils.kEpsilon)
			return false;

		Point3D p = ray.o.add(ray.d.multiply(t));
		Vector3D d = p.subtract(p0);

		double ddota = d.dot(a);

		if (ddota < 0.0 || ddota > aLengthSquared)
		{
			return false;
		}

		double ddotb = d.dot(b);

		if (ddotb < 0.0 || ddotb > bLengthSquared)
		{
			return false;
		}

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
		this.sampler.initialize();
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		this.sampler.setNumberOfSamples(numberOfSamples);
		this.sampler.initialize();
	}

	@Override
	public Point3D sample(ShadeRec sr)
	{
		Point2D samplePoint = sampler.sampleUnitSquare(sr);
		return p0.add(a.multiply(samplePoint.x)).add(b.multiply(samplePoint.y));
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
