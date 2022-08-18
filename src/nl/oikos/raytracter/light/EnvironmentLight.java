package nl.oikos.raytracter.light;

import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.material.Emissive;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 11-3-2018.
 */
public class EnvironmentLight extends Light
{

	private Sampler sampler;
	private Emissive material;

	public EnvironmentLight()
	{
		super();
	}

	@Override
	public Vector3D getDirection(ShadeRec sr)
	{
		Vector3D w = sr.normal;
		Vector3D v = new Vector3D(0.0034, 1, 0.0071).cross(w).normalize();
		Vector3D u = v.cross(w);

		Point3D	sp = sampler.sampleHemisphere(sr);
		sr.lightSamplePoint.put(this, sp);

		return u.multiply(sp.x).add(v.multiply(sp.y)).add(w.multiply(sp.z));
	}

	@Override
	public RGBColor L(ShadeRec sr)
	{
		return material.L(sr);
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

	public void setMaterial(Emissive material)
	{
		this.material = material;
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
		this.sampler.mapSamplesToHemisphere(1);
	}
}
