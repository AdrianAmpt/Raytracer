package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 3-9-2017.
 */
public class PointLight extends Light
{
	private Point3D location;

	public PointLight()
	{
		this.location = Point3D.Y;
	}

	@Override
	public Vector3D getDirection(ShadeRec shadeRec)
	{
		return location.subtract(shadeRec.hitPoint).normalize();
	}

	@Override
	public RGBColor L(ShadeRec shadeRec)
	{
		return this.color.multiply(this.ls);
	}

	@Override
	public boolean inShadow(Ray ray, ShadeRec shadeRec)
	{
		Reference<Double> t = new Reference<>(0d);

		double d = location.distance(ray.o);
		for (GeometricObject object : shadeRec.world.objects)
		{
			if (object.shadowHit(ray, t) && t.get() < d)
				return true;
		}

		return false;
	}

	public void setLocation(Point3D location)
	{
		this.location = location;
	}
}
