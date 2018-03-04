package nl.oikos.raytracter.texture;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 2-9-2017.
 */
public abstract class Texture
{
	public abstract RGBColor getColor(ShadeRec sr);
}
