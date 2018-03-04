package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 31-7-2017.
 */
public class Point2D
{

	public static final Point2D O = new Point2D(0,0);
	public static final Point2D X = new Point2D(1,0);
	public static final Point2D Y = new Point2D(0,1);
	public static final Point2D I = new Point2D(1,1);

	/* x component from this point. */
	public double x;
	/* y component from this point. */
	public double y;

	public Point2D(final double p)
	{
		this(p, p);
	}

	public Point2D(final double x, final double y)
	{
		this.x = x;
		this.y = y;
	}

	public Point2D(final Point2D p)
	{
		this(p.x, p.y);
	}

	/**
	 * Returns the squared length of this point
	 * @return |p|^2
	 */
	public double lengthSqrd()
	{
		return x * x + y * y;
	}

	/**
	 * Returns the length of this point
	 * @return |p|
	 */
	public double length()
	{
		return Math.sqrt(this.lengthSqrd());
	}

	/**
	 * Returns a unit point with the same direction as this
	 * @return p/|p|
	 */
	public Point2D normalize()
	{
		double length = this.length();
		return new Point2D(x / length, y / length);
	}

	/**
	 * Returns this + p
	 * @param p
	 * @return this + p
	 */
	public Point2D add(final Point2D p)
	{
		return new Point2D(x + p.x, y + p.y);
	}

	/**
	 * Returns this - p
	 * @param p
	 * @return this - p
	 */
	public Point2D subtract(final Point2D p)
	{
		return new Point2D(x - p.x, y - p.y);
	}

	/**
	 * Returns -this
	 * @return returns -this
	 */
	public Point2D negate()
	{
		return new Point2D(-x, -y);
	}

	/**
	 * Returns this * s
	 * @param s
	 * @return this * s
	 */
	public Point2D multiply(final double s)
	{
		return new Point2D(x * s, y * s);
	}

	/**
	 * Return this / s
	 * @param s
	 * @return this / s
	 */
	public Point2D divide(final double s)
	{
		return new Point2D(x / s, y / s);
	}

	/**
	 * Returns the dot product between this and p
	 * @param p
	 * @return this · p
	 */
	public double dot(final Point2D p)
	{
		return (x * p.x + y * p.y);
	}
	
	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}
