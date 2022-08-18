package nl.oikos.raytracter.material;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 1-8-2017.
 */
public abstract class Material
{

	protected boolean castShadows;

	public Material()
	{
		this.castShadows = true;
	}

	public RGBColor shade(ShadeRec shadeRec)
	{
		return RGBColor.BLACK;
	}

	public RGBColor areaLightShade(ShadeRec shadeRec)
	{
		return RGBColor.BLACK;
	}

	public void setShadows(boolean castShadows)
	{
		this.castShadows = castShadows;
	}
}
