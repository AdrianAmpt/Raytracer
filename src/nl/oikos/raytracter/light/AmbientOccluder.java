package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 7-3-2018.
 */
public class AmbientOccluder extends Light
{

	private Sampler sampler;
	private RGBColor minAmount;

	public AmbientOccluder()
	{
		minAmount = new RGBColor(0.5);
	}

	@Override
	public Vector3D getDirection(ShadeRec shadeRec)
	{
		Vector3D w = shadeRec.normal;
		Vector3D v = w.cross(new Vector3D(0.0072, 1.0, 0.0034)).normalize();
		Vector3D u = v.cross(w);

		Point3D sp = shadeRec.lightSamplePoint.computeIfAbsent(this, k -> sampler.sampleHemisphere(shadeRec));
		shadeRec.lightSamplePoint.put(this, sp);

		return u.multiply(sp.x).add(v.multiply(sp.y)).add(w.multiply(sp.z));
	}

	@Override
	public RGBColor L(ShadeRec shadeRec)
	{
		Ray shadowRay = new Ray(shadeRec.hitPoint, getDirection(shadeRec));

		if (inShadow(shadowRay, shadeRec))
			return minAmount.multiply(ls).multiply(color);
		else
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

	public void setSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.sampler.mapSamplesToHemisphere(1);
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		super.updateNumberOfSamples(numberOfSamples);

		this.sampler.setNumberOfSamples(numberOfSamples);
		this.sampler.initialize();
		this.sampler.mapSamplesToHemisphere(1);
	}

	public void setMinAmount(RGBColor minAmount)
	{
		this.minAmount = minAmount;
	}
}
