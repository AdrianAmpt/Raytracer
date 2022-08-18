package nl.oikos.raytracter.brdf;

import nl.oikos.raytracter.texture.SingleColor;
import nl.oikos.raytracter.texture.Texture;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 7-3-2018.
 */
public class PerfectSpecular extends BRDF
{

	private double kr;
	private Texture cr;

	public PerfectSpecular()
	{
		super();
		this.kr = 0;
		this.cr = new SingleColor(RGBColor.WHITE);
	}

//	@Override
//	public RGBColor sampleF(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi, Reference<Double> pdf)
//	{
//		double ndotwo = sr.normal.dot(wo.get());
//		wi.set(wo.get().negate().add(sr.normal.multiply(2).multiply(ndotwo)));
//
//		return (cr.getColor(sr).multiply(kr).divide((sr.normal.dot(wi.get()))));
//	}

	public void setKr(double kr)
	{
		this.kr = kr;
	}

	public void setCr(Texture cr)
	{
		this.cr = cr;
	}
}
