package nl.oikos.raytracter.material;

import nl.oikos.raytracter.texture.SingleColor;
import nl.oikos.raytracter.texture.Texture;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 10-3-2018.
 */
public class Emissive extends Material
{

	private double ls;
	private Texture ce;

	public Emissive()
	{
		super();
		this.ls = 1;
		this.ce = new SingleColor(RGBColor.WHITE);
	}

	@Override
	public RGBColor shade(ShadeRec sr)
	{
		if (sr.normal.negate().dot(sr.ray.d) > 0.0)
			return ce.getColor(sr).multiply(ls);
		else
			return RGBColor.BLACK;
	}

	public RGBColor L(ShadeRec sr)
	{
		return ce.getColor(sr).multiply(ls);
	}

	public void scaleRadiance(double ls)
	{
		this.ls = ls;
	}

	public void setColor(Texture ce)
	{
		this.ce = ce;
	}

	public void setColor(RGBColor ce)
	{
		this.ce = new SingleColor(ce);
	}
}