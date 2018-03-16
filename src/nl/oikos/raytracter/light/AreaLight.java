package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricLightObject;
import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.material.Emissive;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 10-3-2018.
 */
public class AreaLight extends Light
{

	private GeometricLightObject object;
	private Emissive material;

	public AreaLight()
	{
		super();
	}

	@Override
	public Vector3D getDirection(ShadeRec sr)
	{
		sr.lightSamplePoint.computeIfAbsent(this, l -> object.sample(sr));

		return getWi(sr);
	}

	private Normal3D getLightNormal(ShadeRec sr)
	{
		return object.getNormal(sr.lightSamplePoint.get(this));
	}

	private Vector3D getWi(ShadeRec sr)
	{
		return sr.lightSamplePoint.get(this).subtract(sr.hitPoint).normalize();
	}

	@Override
	public RGBColor L(ShadeRec sr)
	{
		double ndotd = getLightNormal(sr).negate().dot(getWi(sr));

		if (ndotd > 0)
			return material.L(sr);
		else
			return RGBColor.BLACK;
	}

	public double G(ShadeRec sr)
	{
		double ndotd = getLightNormal(sr).negate().dot(getWi(sr));
		double d2 = sr.lightSamplePoint.get(this).distanceSqrd(sr.hitPoint);

		return ndotd / d2;
	}

	public double pdf(ShadeRec sr)
	{
		return object.pdf(sr);
	}


	@Override
	public boolean inShadow(Ray ray, ShadeRec sr)
	{
		Reference<Double> t = new Reference<>(0d);
		double ts = sr.lightSamplePoint.get(this).subtract(ray.o).dot(ray.d);

		for (GeometricObject object : sr.world.objects)
		{
			if (object.shadowHit(ray, t) && t.get() < ts)
				return true;
		}

		return false;
	}

	public void setObject(GeometricLightObject object)
	{
		this.object = object;
	}

	public void setMaterial(Emissive material)
	{
		this.material = material;
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		super.updateNumberOfSamples(numberOfSamples);
		this.object.updateNumberOfSamples(numberOfSamples);
	}
}
