package nl.oikos.raytracter.texture;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 3-3-2018.
 */
public class PlaneChecker extends Texture
{

	/***********************************
	 **          Properties           **
	 ***********************************/

	/** Size of the individual squares. */
	private double squareSize;
	/** Line width. */
	private double outlineWidth;
	/** Checker color 1. */
	private RGBColor color1;
	/** Checker color 2. */
	private RGBColor color2;
	/** Line color. */
	private RGBColor outlineColor;

	/***********************************
	 **          Constructors         **
	 ***********************************/

	public PlaneChecker()
	{
		super();

		this.squareSize = 1;
		this.outlineWidth = 0;
		this.color1 = RGBColor.WHITE;
		this.color2 = RGBColor.GRAY;
		this.outlineColor = RGBColor.BLACK;
	}

	/***********************************
	 **         Business logic        **
	 ***********************************/

	@Override
	public RGBColor getColor(ShadeRec sr)
	{
		double x = sr.localHitPoint.x;
		double z = sr.localHitPoint.z;
		int ix = (int) Math.floor(x / squareSize);
		int iz = (int) Math.floor(z / squareSize);

		double fx = x / squareSize - ix;
		double fz = z / squareSize - iz;
		double width = 0.5 * outlineWidth / squareSize;
		boolean inOutline =	( fx < width || fx > 1.0 - width) ||
				( fz < width || fz > 1.0 - width);

		if(inOutline)
			return outlineColor;
		else if ((ix+iz) % 2 == 0)
			return color1;
		else
			return color2;
	}

	public void setSquareSize(double squareSize)
	{
		this.squareSize = squareSize;
	}

	public void setOutlineWidth(double outlineWidth)
	{
		this.outlineWidth = outlineWidth;
	}

	public void setColor1(RGBColor color1)
	{
		this.color1 = color1;
	}

	public void setColor1(double color1)
	{
		this.color1 = new RGBColor(color1);
	}

	public void setColor1(double r, double g, double b)
	{
		this.color1 = new RGBColor(r, g, b);
	}

	public void setColor2(RGBColor color2)
	{
		this.color2 = color2;
	}

	public void setColor2(double color2)
	{
		this.color2 = new RGBColor(color2);
	}

	public void setColor2(double r, double g, double b)
	{
		this.color2 = new RGBColor(r, g, b);
	}

	public void setOutlineColor(RGBColor outlineColor)
	{
		this.outlineColor = outlineColor;
	}

	public void setOutlineColor(double outlineColor)
	{
		this.outlineColor = new RGBColor(outlineColor);
	}

	public void setOutlineColor(double r, double g, double b)
	{
		this.outlineColor = new RGBColor(r, g, b);
	}
}
