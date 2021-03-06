package nl.oikos.raytracter.brdf;

import nl.oikos.raytracter.texture.SingleColor;
import nl.oikos.raytracter.texture.Texture;
import nl.oikos.raytracter.util.*;

/**
 * Created by Adrian on 2-9-2017.
 */
public class Lambertian extends BRDF
{

	private double kd;
	private Texture cd;

	public Lambertian()
	{
		super();
		this.kd = 0;
		this.cd = new SingleColor(RGBColor.BLACK);
	}

	@Override
	public RGBColor f(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi)
	{
		return cd.getColor(sr).multiply(kd * MathUtils.INV_PI);
	}

	/*@Override
	public RGBColor sampleF(ShadeRec sr, Reference<Vector3D> wo, Reference<Vector3D> wi, Reference<Double> pdf)
	{
		Vector3D w = new Vector3D(sr.normal);
		Vector3D v = new Vector3D(0.0034f, 1, 0.0071f).cross(w);
		v = v.normalize();
		Vector3D u = v.cross(w);


		Point3D sp = sampler.sampleHemisphere(sr);

		Vector3D wiV;
		wiV = u.multiply(sp.x).add(v.multiply(sp.y)).add(w.multiply(sp.z));
		wiV = wiV.normalize();
		wi.set(wiV);

		pdf.set((sr.normal.dot(wi.get()) * MathUtils.INV_PI));

		return cd.getColor(sr).multiply(kd * MathUtils.INV_PI);
	}*/

	@Override
	public RGBColor rho(ShadeRec sr, Reference<Vector3D> wo)
	{
		return cd.getColor(sr).multiply(kd);
	}

	public void setKd(double kd)
	{
		this.kd = kd;
	}

	public void setKa(double ka)
	{
		this.kd = ka;
	}

	public void setCd(Texture cd)
	{
		this.cd = cd;
	}

	public void setCd(RGBColor cd)
	{
		this.cd = new SingleColor(cd);
	}
}