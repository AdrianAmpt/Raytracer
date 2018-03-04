package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 26-8-2017.
 */
public class Pixel
{
	/* x component from this pixel. */
	public final int x;
	/* y component from this pixel. */
	public final int y;

	public Pixel(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public Pixel(final Pixel p)
	{
		this(p.x, p.y);
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}
