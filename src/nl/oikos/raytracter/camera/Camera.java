package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 31-7-2017.
 */
public abstract class Camera
{
	protected Point3D eye;				// eye point
	protected Point3D lookat; 			// lookat point
	protected float ra;					// roll angle
	protected Vector3D u, v, w;			// orthonormal basis vectors
	protected Vector3D up;				// up vector
	float exposureTime;

	protected boolean isStero;
	protected int offset;

	public Camera()
	{
		this.eye = new Point3D(0, 0, 500);
		this.lookat = Point3D.O;
		this.ra = 0;
		this.up = Vector3D.Y;
		this.u = Vector3D.X;
		this.v = Vector3D.Y;
		this.w = Vector3D.Z;
		this.exposureTime = 1;
		this.isStero = false;
		this.offset = 0;
	}

	public void computeUVW()
	{
		w = eye.subtract(lookat);
		w = w.normalize();
		u = up.cross(w);
		u = u.normalize();
		v = w.cross(u);

		// take care of the singularity by hardwiring in specific camera orientations
		if (eye.x == lookat.x && eye.z == lookat.z && eye.y > lookat.y) { // camera looking vertically down
			u = new Vector3D(0, 0, 1);
			v = new Vector3D(1, 0, 0);
			w = new Vector3D(0, 1, 0);
		}

		if (eye.x == lookat.x && eye.z == lookat.z && eye.y < lookat.y) { // camera looking vertically up
			u = new Vector3D(1, 0, 0);
			v = new Vector3D(0, 0, 1);
			w = new Vector3D(0, -1, 0);
		}
	}

	public abstract RenderedPixel renderScene(World world, Pixel pixel);

	public void updateNumberSamples(int numberOfSamples){}

	public boolean isStero()
	{
		return isStero;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setEye(Point3D eye)
	{
		this.eye = eye;
	}

	public void setEye(double x, double y, double z)
	{
		this.eye = new Point3D(x, y, z);
	}

	public void setLookat(Point3D lookat)
	{
		this.lookat = lookat;
	}

	public void setLookat(double x, double y, double z)
	{
		this.lookat = new Point3D(x, y, z);
	}

	public void setUp(Vector3D up)
	{
		this.up = up;
	}

	public void setUp(double x, double y, double z)
	{
		this.up = new Vector3D(x, y, z);
	}

	public void setExposureTime(float exposureTime)
	{
		this.exposureTime = exposureTime;
	}
}
