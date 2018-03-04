package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 18-8-2017.
 */
public class RenderedPixel extends Pixel
{
	public final RGBColor color;

	public RenderedPixel(final int x, final int y, RGBColor color)
	{
		super(x, y);
		this.color = color;
	}

	public RenderedPixel(Pixel pixel, RGBColor color)
	{
		this(pixel.x, pixel.y, color);
	}
}
