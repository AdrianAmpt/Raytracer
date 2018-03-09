package nl.oikos.raytracter.brdf;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Reference;
import nl.oikos.raytracter.util.ShadeRec;
import nl.oikos.raytracter.util.Vector3D;

/**
 * Created by Adrian on 23-8-2017.
 */
public abstract class BRDF
{

	protected Sampler sampler;

	public BRDF() {}

	public RGBColor f(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi)
	{
		return RGBColor.BLACK;
	}

	/*public RGBColor sampleF(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi)
	{
		return RGBColor.BLACK;
	}

	public RGBColor	sampleF(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi, Reference<Double> pdf)
	{
		return RGBColor.BLACK;
	}*/

	public RGBColor rho(ShadeRec sr, Reference<Vector3D> wo)
	{
		return RGBColor.BLACK;
	}

	public void setSampler(Sampler sampler)
	{
		this.sampler = sampler;
		this.sampler.mapSamplesToHemisphere(1);
	}
}
