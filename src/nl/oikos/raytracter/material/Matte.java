package nl.oikos.raytracter.material;

import nl.oikos.raytracter.brdf.Lambertian;
import nl.oikos.raytracter.light.Light;
import nl.oikos.raytracter.texture.Texture;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 22-8-2017.
 */
public class Matte extends Material
{

	private Lambertian ambientBRDF;
	private Lambertian diffuseBRDF;

	public Matte()
	{
		super();
		this.ambientBRDF = new Lambertian();
		this.diffuseBRDF = new Lambertian();
	}

	@Override
	public RGBColor shade(ShadeRec sr)
	{
		Reference<Vector3D> wo = new Reference<>(sr.ray.d.negate());
		RGBColor L = ambientBRDF.rho(sr, wo).multiply(sr.world.ambientLight.L(sr));

		for (Light light : sr.world.lights)
		{
			Reference<Vector3D> wi = new Reference<>(new Vector3D(light.getDirection(sr)));
			double ndotwi = sr.normal.dot(wi.get());
			double ndotwo = sr.normal.dot(wo.get());

			if (ndotwi > 0.0 && ndotwo > 0.0)
			{
				boolean inShadow = false;

				if (castShadows && light.castShadows())
				{
					Ray shadowRay = new Ray(sr.hitPoint, wi.get());
					inShadow = light.inShadow(shadowRay, sr);
				}

				if (!inShadow)
				{
					L = L.add(diffuseBRDF.f(sr, wo, wi).multiply(light.L(sr)).multiply(light.G(sr)).multiply(ndotwi / light.pdf(sr)));
					//L = L.add(diffuseBRDF.f(sr, wo, wi).multiply(light.L(sr)).multiply(ndotwi));
				}

			}
		}

		return L;
	}

	public void setKa(double ka)
	{
		this.ambientBRDF.setKa(ka);
	}

	public void setKd(double kd)
	{
		this.diffuseBRDF.setKd(kd);
	}

	public void setCd(Texture cd)
	{
		this.ambientBRDF.setCd(cd);
		this.diffuseBRDF.setCd(cd);
	}

	public void setCd(RGBColor cd)
	{
		this.ambientBRDF.setCd(cd);
		this.diffuseBRDF.setCd(cd);
	}
}