package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 4-3-2018.
 */
public class RGBIntColor extends RGBColor
{
	public RGBIntColor(final int c)
	{
		super(c/255d);
	}

	public RGBIntColor(final int r, final int g, final int b)
	{
		super(r/255d, g/255d, b/225d);
	}
}
