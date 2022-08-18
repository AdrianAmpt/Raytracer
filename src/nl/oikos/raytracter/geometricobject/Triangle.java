package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 10-3-2018.
 */
public class Triangle extends Plane
{

	protected Point3D p1;
	protected Point3D p2;

	public Triangle()
	{
		super(Point3D.O, Normal3D.Y);

		this.p1 = Point3D.Z;
		this.p2 = Point3D.X;
	}

	public Triangle(Point3D p0, Point3D p1, Point3D p2)
	{
		super(p0, p1.subtract(p0).cross(p2.subtract(p0)).normalize());

		this.p1 = p1;
		this.p2 = p2;
	}

	public BBox getBoudingBox()
	{
		double delta = 0.000001;

		return new BBox(
				new Point3D(
						Math.min(Math.min(p0.x, p1.x), p2.x) - delta,
						Math.min(Math.min(p0.y, p1.y), p2.y) - delta,
						Math.min(Math.min(p0.z, p1.z), p2.z) - delta
				),
				new Point3D(
						Math.max(Math.max(p0.x, p1.x), p2.x) + delta,
						Math.max(Math.max(p0.y, p1.y), p2.y) + delta,
						Math.max(Math.max(p0.z, p1.z), p2.z) + delta
				)
		);
	}


	@Override
	public boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr)
	{
		double a = p0.x - p1.x, b = p0.x - p2.x, c = ray.d.x, d = p0.x - ray.o.x;
		double e = p0.y - p1.y, f = p0.y - p2.y, g = ray.d.y, h = p0.y - ray.o.y;
		double i = p0.z - p1.z, j = p0.z - p2.z, k = ray.d.z, l = p0.z - ray.o.z;

		double m = f * k - g * j;
		double n = h * k - g * l;
		double p = f * l - g * j;

		double q = g * i - e * k;
		double s = e * j - f * i;

		double inv_denom = 1.0 / (a * m + b * q + c * s);

		double beta = (d * m - b * n - c * p) * inv_denom;

		if(beta < 0.0)
			return false;

		double r = e * l - h * i;
		double gamma = (a * n + d * q + c * r) * inv_denom;

		if(gamma < 0.0)
			return false;

		if(beta + gamma > 1.0)
			return false;

		double t = (a * p - b * r + d * s) * inv_denom;

		if(t < MathUtils.kEpsilon)
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
