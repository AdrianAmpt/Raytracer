package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 31-7-2017.
 */
public class Normal3D extends Vector3D
{
	public static final Normal3D X = new Normal3D(1,0,0);
	public static final Normal3D Y = new Normal3D(0,1,0);
	public static final Normal3D Z = new Normal3D(0,0,1);

	protected Normal3D(final double x, final double y, final double z, final double length)
	{
		super(x / length, y / length, z / length);
	}

	public Normal3D(final double n)
	{
		this(n, n, n);
	}

	public Normal3D(final double x, final double y, final double z)
	{
		this(x, y, z, Math.sqrt(x * x + y * y + z * z));
	}

	public Normal3D(final Normal3D n)
	{
		this(n.x, n.y, n.z, 1);
	}

	public Normal3D(final Vector3D v)
	{
		this(v.x, v.y, v.z);
	}

	public Normal3D(final Point3D p)
	{
		this(p.x, p.y, p.z);
	}

	/**
	 * Returns -this
	 * @return returns -this
	 */
	@Override
	public Normal3D negate()
	{
		Vector3D result = super.negate();

		return new Normal3D(result.x, result.y, result.z, 1);
	}
}
