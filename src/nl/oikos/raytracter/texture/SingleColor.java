package nl.oikos.raytracter.texture;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 10-3-2018.
 */
public class SingleColor extends Texture
{

	/***********************************
	 **          Properties           **
	 ***********************************/

	/** Color. */
	private RGBColor color;

	/***********************************
	 **          Constructors         **
	 ***********************************/

	public SingleColor(RGBColor color)
	{
		super();

		this.color = color;
	}

	public SingleColor(double r, double g, double b)
	{
		super();

		this.color = new RGBColor(r, g, b);
	}

	/***********************************
	 **         Business logic        **
	 ***********************************/

	@Override
	public RGBColor getColor(ShadeRec sr)
	{
		return this.color;
	}
}
