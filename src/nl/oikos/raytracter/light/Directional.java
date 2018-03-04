package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 22-8-2017.
 */
public class Directional extends Light
{
	private double ls;
	private RGBColor color;
	private Vector3D direction;

	public Directional()
	{
		this.ls = 1;
		this.color = RGBColor.WHITE;
		this.direction = new Vector3D(0, 1, 0);
	}

	@Override
	public Vector3D getDirection(ShadeRec shadeRec)
	{
		return this.direction;
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

		for (GeometricObject object : shadeRec.world.objects)
		{
			if (object.shadowHit(ray, t))
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

	public void setDirection(Vector3D direction)
	{
		this.direction = direction.normalize();
	}
}
