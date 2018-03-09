package nl.oikos.raytracter.light;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;
import nl.oikos.raytracter.util.Vector3D;

/**
 * Created by Adrian on 31-7-2017.
 */
public class Ambient extends Light
{

	public Ambient() {}

	@Override
	public Vector3D getDirection(ShadeRec shadeRec)
	{
		return Vector3D.O;
	}

	@Override
	public RGBColor L(ShadeRec shadeRec)
	{
		return this.color.multiply(this.ls);
	}
}
