package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 31-7-2017.
 */
public class Point3D extends Vector3D
{
	
	public static final Point3D O = new Point3D(0,0,0);
	public static final Point3D X = new Point3D(1,0,0);
	public static final Point3D Y = new Point3D(0,1,0);
	public static final Point3D Z = new Point3D(0,0,1);
	public static final Point3D I = new Point3D(1,1,1);

	public Point3D(final double p)
	{
		this(p, p, p);
	}

	public Point3D(final double x, final double y, final double z)
	{
		super(x, y, z);
	}

	public Point3D(final Point3D p)
	{
		this(p.x, p.y, p.z);
	}

	public Point3D(final Vector3D v)
	{
		this(v.x, v.y, v.z);
	}

	public Point3D(final Normal3D n)
	{
		this(n.x, n.y, n.z);
	}

	/**
	 * Returns this + v
	 * @param v
	 * @return this + v
	 */
	public Point3D add(final Vector3D v)
	{
		return new Point3D(super.add(v));
	}

	/**
	 * Returns this - v
	 * @param v
	 * @return this - v
	 */
	public Point3D subtract(final Vector3D v)
	{
		return new Point3D(super.subtract(v));
	}

	/**
	 * Returns -this
	 * @return returns -this
	 */
	@Override
	public Point3D negate()
	{
		return new Point3D(super.negate());
	}

	/**
	 * Returns this * s
	 * @param s
	 * @return this * s
	 */
	public Point3D multiply(final double s)
	{
		return new Point3D(super.multiply(s));
	}

	/**
	 * Return this / s
	 * @param s
	 * @return this / s
	 */
	public Point3D divide(final double s)
	{
		return new Point3D(super.divide(s));
	}

	/**
	 * Returns the squared distance between this and p
	 * @param p
	 * @return |this - p|^2
	 */
	public double distanceSqrd(Point3D p)
	{
		return (x - p.x) * (x - p.x)
			 + (y - p.y) * (y - p.y)
			 + (z - p.z) * (z - p.z);
	}

	/**
	 * Returns the distance between this and p
	 * @param p
	 * @return |this - p|
	 */
	public double distance(Point3D p)
	{
		return Math.sqrt(this.distanceSqrd(p));
	}
}
