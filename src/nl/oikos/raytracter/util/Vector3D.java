package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 31-7-2017.
 */
public class Vector3D
{

	public static final Vector3D O = new Vector3D(0,0,0);
	public static final Vector3D X = new Vector3D(1,0,0);
	public static final Vector3D Y = new Vector3D(0,1,0);
	public static final Vector3D Z = new Vector3D(0,0,1);
	public static final Vector3D I = new Vector3D(1,1,1);

	/* x component from this vector. */
	public final double x;
	/* y component from this vector. */
	public final double y;
	/* z component from this vector. */
	public final double z;

	/**
	 * Creates a Vector with [v, v, v]
	 * @param v
	 */
	public Vector3D(final double v)
	{
		this(v, v, v);
	}

	public Vector3D(final double x, final double y, final double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(final Vector3D v)
	{
		this(v.x, v.y, v.z);
	}

	public Vector3D(final Normal3D n)
	{
		this(n.x, n.y, n.z);
	}

	public Vector3D(final Point3D p)
	{
		this(p.x, p.y, p.z);
	}

	/**
	 * Returns the squared length of this vector
	 * @return |v|^2
	 */
	public double lengthSqrd()
	{
		return x * x + y * y + z * z;
	}

	/**
	 * Returns the length of this vector
	 * @return |v|
	 */
	public double length()
	{
		return Math.sqrt(this.lengthSqrd());
	}

	/**
	 * Returns a unit vector with the same direction as this
	 * @return v/|v|
	 */
	public Normal3D normalize()
	{
		double length = this.length();
		return new Normal3D(x / length, y / length, z / length);
	}

	/**
	 * Returns this + v
	 * @param v
	 * @return this + v
	 */
	public Vector3D add(final Vector3D v)
	{
		return new Vector3D(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Returns this - v
	 * @param v
	 * @return this - v
	 */
	public Vector3D subtract(final Vector3D v)
	{
		return new Vector3D(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * Returns -this
	 * @return returns -this
	 */
	public Vector3D negate()
	{
		return new Vector3D(-x, -y, -z);
	}

	/**
	 * Returns this * s
	 * @param s
	 * @return this * s
	 */
	public Vector3D multiply(final double s)
	{
		return new Vector3D(x * s, y * s, z * s);
	}

	/**
	 * Return this / s
	 * @param s
	 * @return this / s
	 */
	public Vector3D divide(final double s)
	{
		return new Vector3D(x / s, y / s, z / s);
	}

	/**
	 * Returns the dot product between this and v
	 * @param v
	 * @return this · v
	 */
	public double dot(final Vector3D v)
	{
		return (x * v.x + y * v.y + z * v.z);
	}

	/**
	 * Returns the cross product between this and v
	 * @param v
	 * @return this x v
	 */
	public Vector3D cross(final Vector3D v)
	{
		return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Vector3D)) return false;

		Vector3D vector3D = (Vector3D) o;

		return Double.compare(vector3D.x, x) == 0 && Double.compare(vector3D.y, y) == 0 && Double.compare(vector3D.z, z) == 0;
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "," + z + "]";
	}
}
