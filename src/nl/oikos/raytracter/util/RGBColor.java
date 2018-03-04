package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 31-7-2017.
 */
public class RGBColor
{

	public static final RGBColor BLACK = new RGBColor(0.0);
	public static final RGBColor DARK_GRAY = new RGBColor(0.25);
	public static final RGBColor GRAY = new RGBColor(0.5);
	public static final RGBColor LIGHT_GRAY = new RGBColor(0.75);
	public static final RGBColor WHITE = new RGBColor(1.0);

	public static final RGBColor RED = new RGBColor(1,0,0);
	public static final RGBColor GREEN = new RGBColor(0,1,0);
	public static final RGBColor BLUE = new RGBColor(0,0,1);

	public static final RGBColor YELLOW = new RGBColor(1,1,0);
	public static final RGBColor CYAN = new RGBColor(0,1,1);
	public static final RGBColor MAGENTA = new RGBColor(1,0,1);

	/* Red component from this color in range of 0-1. */
	public final double r;
	/* Green component from this color in range of 0-1. */
	public final double g;
	/* Blue component from this color in range of 0-1. */
	public final double b;

	public RGBColor(final double c)
	{
		this(c, c, c);
	}

	public RGBColor(final double r, final double g, final double b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public RGBColor(final int c)
	{
		this((double) c);
		if (c > 1)
		{
			System.out.println("WARNING int is larger than 1, maybe use RGBIntColor");
		}
	}

	public RGBColor(final int r, final int g, final int b)
	{
		this((double) r, (double) g, (double) b);
		if (r > 1 || g > 1 || b > 1)
		{
			System.out.println("WARNING ints are larger than 1, maybe use RGBIntColor");
		}
	}

	public RGBColor(final RGBColor c)
	{
		this(c.r, c.g, c.b);
	}

	public RGBColor add(RGBColor c)
	{
		return new RGBColor(c.r + r, c.g + g, c.b + b);
	}

	public RGBColor subtract(RGBColor c)
	{
		return new RGBColor(r - c.r, g - c.g, b - c.b);
	}

	public RGBColor multiply(double s)
	{
		return new RGBColor(r  * s, g * s, b * s);
	}

	public RGBColor divide(double s)
	{
		return new RGBColor(r / s, g / s, b / s);
	}

	public RGBColor multiply(RGBColor c)
	{
		return new RGBColor(r * c.r, g * c.g, b * c.b);
	}

	public RGBColor pow(double p)
	{
		return new RGBColor(Math.pow(r, p), Math.pow(g, p), Math.pow(b, p));
	}

	public int getRGB()
	{
		return ((((int)(r*255)) & 0xFF) << 16) |
				((((int)(g*255)) & 0xFF) << 8) |
				((((int)(b*255)) & 0xFF) << 0);
	}

	@Override
	public String toString()
	{
		return r + ", " + g + ", " + b;

		//return "#" + String.format("%06X", this.getRGB());
	}
}
