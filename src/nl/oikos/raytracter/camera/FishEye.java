package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.ViewPlane;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 3-3-2018.
 */
public class FishEye extends Camera
{
	private static final double PI_ON_180 = Math.PI / 180;

	/**
	 * Psi_max in degrees.
	 */
	private double psiMax;

	public FishEye()
	{
		super();
		this.psiMax = 180;
	}

	public Vector3D getDirection(Point2D point, final ViewPlane vp, Reference<Double> rSquared)
	{
		Point2D pn = new Point2D(2 / (vp.pixelSize * vp.width) * point.x, 2 / (vp.pixelSize * vp.height) * point.y);
		rSquared.set(pn.x * pn.x + pn.y * pn.y);

		if (rSquared.get() <= 1)
		{
			double r = Math.sqrt(rSquared.get());
			double psi = r * this.psiMax * PI_ON_180;
			double sinPsi = Math.sin(psi);
			double cosPsi = Math.cos(psi);
			double sinAlpha = pn.y / r;
			double cosAlpha = pn.x / r;

			return this.u.multiply(sinPsi * cosAlpha).add(v.multiply(sinPsi * sinAlpha)).subtract(w.multiply(cosPsi));
		}
		else
		{
			return Vector3D.O;
		}
	}

	@Override
	public RenderedPixel renderScene(World world, Pixel pixel)
	{
		RGBColor L = RGBColor.BLACK;
		ViewPlane vp = new ViewPlane(world.viewPlane);
		Point2D pp = new Point2D(0);        // sample point on a pixel
		Reference<Double> rSquared = new Reference<>(0d);

		ShadeRec shadeRec = new ShadeRec(world);

		for (int j = 0; j < vp.sampler.getNumberOfSamples(); j++)
		{
			Point2D sp = vp.sampler.sampleUnitSquare(shadeRec);

			pp.x = vp.pixelSize * (pixel.x - 0.5 * vp.width + sp.x);
			pp.y = vp.pixelSize * (pixel.y - 0.5 * vp.height + sp.y);

			Ray ray = new Ray(eye, getDirection(pp, vp, rSquared));

			if (rSquared.get() <= 1)
			{
				Reference<Integer> countRef = new Reference<>(shadeRec.count);
				Reference<Integer> jumpRef = new Reference<>(shadeRec.jump);
				L = L.add(world.tracer.trace(ray, shadeRec.depth, countRef, jumpRef));
				shadeRec.count = countRef.get();
				shadeRec.jump = jumpRef.get();
			}
		}

		L = L.divide(vp.sampler.getNumberOfSamples());
		L = L.multiply(exposureTime);

		return world.displayPixel(pixel, L);
	}

	public void setPsiMax(double psiMax)
	{
		this.psiMax = psiMax;
	}

	public void setFov(double fov)
	{
		this.psiMax = fov / 2;
	}
}
