package nl.oikos.raytracter.brdf;

import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 7-3-2018.
 */
public class GlossySpecular extends BRDF
{

	private double ks;
	private RGBColor cs;
	private double exp;

	public GlossySpecular()
	{
		super();
		this.ks = 0;
		this.cs = RGBColor.WHITE;
		this.exp = 0;
	}

	@Override
	public RGBColor f(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi)
	{
		RGBColor L = RGBColor.BLACK;

		double ndotwi = wo.get().dot(wi.get());
		Vector3D r = wi.get().negate().add(sr.normal.multiply(2 * ndotwi));
		double rdotwo = r.dot(wi.get());

		if (rdotwo > 0)
			L = cs.multiply(ks * Math.pow(rdotwo, exp));

		return L;
	}

	/*@Override
	public RGBColor sampleF(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi, Reference<Double> pdf)
	{

	}*/

	@Override
	public RGBColor rho(ShadeRec sr, Reference<Vector3D> wo)
	{
		return RGBColor.BLACK;
	}

	public void setKs(double ks)
	{
		this.ks = ks;
	}

	public void setCs(RGBColor cs)
	{
		this.cs = cs;
	}

	public void setExp(double exp)
	{
		this.exp = exp;
	}
}
