package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.ViewPlane;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 21-8-2017.
 */
public class Pinhole extends Camera
{
	/** view plane distance. */
	private double viewDistance;
	/** zoom factor. */
	private double zoom;

	public Pinhole()
	{
		super();
		this.viewDistance = 500;
		this.zoom = 1;
	}

	public Vector3D getDirection(Point2D point)
	{
		Vector3D u = this.u.multiply(point.x);
		Vector3D v = this.v.multiply(point.y);
		Vector3D w = this.w.multiply(viewDistance);

		return u.add(v).subtract(w).normalize();
	}

	@Override
	public RenderedPixel renderScene(World world, Pixel pixel)
	{
		RGBColor L = RGBColor.BLACK;
		ViewPlane vp = new ViewPlane(world.viewPlane);
		Point2D pp = new Point2D(0);		// sample point on a pixel

		vp.pixelSize /= zoom;

		ShadeRec shadeRec = new ShadeRec(world);

		for (int j = 0; j < vp.sampler.getNumberOfSamples(); j++)
		{
			Point2D sp = vp.sampler.sampleUnitSquare(shadeRec);

			pp.x = vp.pixelSize * (pixel.x - 0.5 * vp.width + sp.x);
			pp.y = vp.pixelSize * (pixel.y - 0.5 * vp.height + sp.y);

			Ray ray = new Ray(eye, getDirection(pp));

			Reference<Integer> countRef = new Reference<>(shadeRec.count);
			Reference<Integer> jumpRef = new Reference<>(shadeRec.jump);
			L = L.add(world.tracer.trace(ray, shadeRec.depth, countRef, jumpRef));
			shadeRec.count = countRef.get();
			shadeRec.jump = jumpRef.get();
		}

		L = L.divide(vp.sampler.getNumberOfSamples());
		L = L.multiply(exposureTime);

		return world.displayPixel(pixel, L);
	}

	public void setViewDistance(double viewDistance)
	{
		this.viewDistance = viewDistance;
	}

	public void setZoom(double zoom)
	{
		this.zoom = zoom;
	}
}