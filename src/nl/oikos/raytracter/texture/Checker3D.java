package nl.oikos.raytracter.texture;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 2-9-2017.
 */
public class Checker3D extends Texture
{

	/***********************************
	 **          Properties           **
	 ***********************************/

	/** Size of the individual squares. */
	private double squareSize;
	/** Checker color 1. */
	private RGBColor color1;
	/** Checker color 2. */
	private RGBColor color2;

	/***********************************
	 **          Constructors         **
	 ***********************************/

	public Checker3D()
	{
		super();

		this.squareSize = 1;
		this.color1 = RGBColor.WHITE;
		this.color2 = RGBColor.GRAY;
	}

	/***********************************
	 **         Business logic        **
	 ***********************************/

	@Override
	public RGBColor getColor(ShadeRec sr)
	{
		double eps = -0.000187453738;
		double x = sr.localHitPoint.x + eps;
		double y = sr.localHitPoint.y + eps;
		double z = sr.localHitPoint.z + eps;
		int ix = (int) Math.floor(x / squareSize);
		int iy = (int) Math.floor(y / squareSize);
		int iz = (int) Math.floor(z / squareSize);

		if ((ix + iy + iz) % 2 == 0)
			return color1;
		else
			return color2;
	}

	public void setSquareSize(double squareSize)
	{
		this.squareSize = squareSize;
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
}
