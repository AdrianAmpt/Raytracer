package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 1-8-2017.
 */
public class Ray
{

	/** Origin of the ray. */
	public Point3D o;
	/** direction of the ray. */
	public Vector3D d;

	/**
	 * Constructs a Ray at the origin in the Z direction.
	 */
	public Ray()
	{
		this(Point3D.O, Vector3D.Z);
	}

	/**
	 * Contructs as Ray with origin o and direction d.
	 *
	 * @param o
	 * @param d
	 */
	public Ray(Point3D o, Vector3D d)
	{
		this.o = o;
		this.d = d;
	}

	/**
	 * Copy constructor.
	 *
	 * @param r
	 */
	public Ray(Ray r)
	{
		this.o = r.o;
		this.d = r.d;
	}

	@Override
	public String toString()
	{
		return "{o: " + o + ", d: " + d + "}";
	}
}
