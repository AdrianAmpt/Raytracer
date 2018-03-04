package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 3-9-2017.
 */
public class PointLight extends Light
{
	private double ls;
	private RGBColor color;
	private Point3D location;

	public PointLight()
	{
		this.ls = 1;
		this.color = RGBColor.WHITE;
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

		double d = location.subtract(ray.o).length();
		for (GeometricObject object : shadeRec.world.objects)
		{
			if (object.shadowHit(ray, t) && t.get() < d)
				return true;
		}

		return false;
	}

	public void scaleRadiance(double ls)
	{
		this.ls = ls;
	}

	public void setColor(RGBColor color)
	{
		this.color = color;
	}

	public void setLocation(Point3D location)
	{
		this.location = location;
	}
}
