package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.ViewPlane;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 1-9-2017.
 */
public class ThinLens extends Camera
{
	/** lens radius. */
	private double lensRadius;
	/** focal plane distance. */
	private double focalDistance;
	/** view plane distance. */
	private double viewDistance;
	/** zoom factor. */
	private double zoom;
	/** lensSampler */
	private Sampler lensSampler;

	public ThinLens()
	{
		super();
		this.viewDistance = 500;
		this.zoom = 1;
		this.lensRadius = 1;
		this.focalDistance = 1;
	}

	public Vector3D getDirection(Point2D point, Point2D lensPoint)
	{
		Point2D p = new Point2D(0);

		p.x = point.x * focalDistance / viewDistance;
		p.y = point.y * focalDistance / viewDistance;

		Vector3D u = this.u.multiply(p.x - lensPoint.x);
		Vector3D v = this.v.multiply(p.y - lensPoint.y);
		Vector3D w = this.w.multiply(focalDistance);

		return u.add(v).subtract(w).normalize();
	}

	@Override
	public RenderedPixel renderScene(World world, Pixel pixel)
	{
		RGBColor L = RGBColor.BLACK;
		Ray ray = new Ray();
		ViewPlane vp = new ViewPlane(world.viewPlane);
		Point2D pp = new Point2D(0);		// sample point on a pixel

		vp.pixelSize /= zoom;

		ShadeRec shadeRec = new ShadeRec(world);

		for (int j = 0; j < vp.sampler.getNumberOfSamples(); j++)
		{
			Point2D sp = vp.sampler.sampleUnitSquare(shadeRec);
			pp.x = vp.pixelSize * (pixel.x - 0.5 * vp.width + sp.x);
			pp.y = vp.pixelSize * (pixel.y - 0.5 * vp.height + sp.y);

			Point2D dp = lensSampler.sampleUnitDisk(shadeRec);
			Point2D lp = dp.multiply(lensRadius);

			ray.o = eye.add(u.multiply(lp.x)).add(v.multiply(lp.y));
			ray.d = this.getDirection(pp, lp);

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

	public void setLensRadius(double lensRadius)
	{
		this.lensRadius = lensRadius;
	}

	public void setFocalDistance(double focalDistance)
	{
		this.focalDistance = focalDistance;
	}

	public void setViewDistance(double viewDistance)
	{
		this.viewDistance = viewDistance;
	}

	public void setZoom(double zoom)
	{
		this.zoom = zoom;
	}

	public void setLensSampler(Sampler lensSampler)
	{
		this.lensSampler = lensSampler;
		this.lensSampler.mapSamplesToUnitDisk();
	}

	@Override
	public void updateNumberOfSamples(int numberOfSamples)
	{
		super.updateNumberOfSamples(numberOfSamples);

		this.lensSampler.setNumberOfSamples(numberOfSamples);
		this.lensSampler.initialize();
		this.lensSampler.mapSamplesToUnitDisk();
	}
}
