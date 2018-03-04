package nl.oikos.raytracter.light;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Ray;
import nl.oikos.raytracter.util.ShadeRec;
import nl.oikos.raytracter.util.Vector3D;

/**
 * Created by Adrian on 31-7-2017.
 */
public abstract class Light
{

	protected boolean castShadows;

	public Light()
	{
		this.castShadows = false;
	}

	public Vector3D getDirection(ShadeRec shadeRec)
	{
		return Vector3D.O;
	}

	public RGBColor L(ShadeRec shadeRec)
	{
		return RGBColor.BLACK;
	}

	public boolean inShadow(Ray ray, ShadeRec shadeRec)
	{
		return false;
	}

	public boolean castShadows()
	{
		return castShadows;
	}

	public void setShadows(boolean castShadows)
	{
		this.castShadows = castShadows;
	}
}