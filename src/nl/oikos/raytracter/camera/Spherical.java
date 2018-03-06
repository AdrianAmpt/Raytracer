package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.ViewPlane;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 4-3-2018.
 */
public class Spherical extends Camera
{

	/**
	 * Psi_max in degrees.
	 */
	private double psiMax;

	/**
	 * Lambda_max in degrees.
	 */
	private double lambdaMax;

	public Spherical()
	{
		super();
		this.psiMax = 180;
	}

	public Vector3D getDirection(Point2D point, final ViewPlane vp)
	{
		Point2D pn = new Point2D(2 / (vp.pixelSize * vp.width) * point.x, 2 / (vp.pixelSize * vp.height) * point.y);

		double lambda = pn.x * this.lambdaMax * MathUtils.PI_ON_180;
		double psi = pn.y * this.psiMax * MathUtils.PI_ON_180;

		double phi = Math.PI - lambda;
		double theta = MathUtils.HALF_PI - psi;

		double sinPhi = Math.sin(phi);
		double cosPhi = Math.cos(phi);
		double sinTheta = Math.sin(theta);
		double cosTheta = Math.cos(theta);

		return this.u.multiply(sinTheta * sinPhi).add(v.multiply(cosTheta)).add(w.multiply(sinTheta * cosPhi));
	}

	@Override
	public RenderedPixel renderStereo(World world, Pixel pixel, double xOffset)
	{
		RGBColor L = RGBColor.BLACK;
		ViewPlane vp = new ViewPlane(world.viewPlane);
		Point2D pp = new Point2D(0);        // sample point on a pixel

		ShadeRec shadeRec = new ShadeRec(world);

		for (int j = 0; j < vp.sampler.getNumberOfSamples(); j++)
		{
			Point2D sp = vp.sampler.sampleUnitSquare(shadeRec);

			pp.x = vp.pixelSize * (pixel.x - 0.5 * vp.width + sp.x) + xOffset;
			pp.y = vp.pixelSize * (pixel.y - 0.5 * vp.height + sp.y);

			Ray ray = new Ray(eye, getDirection(pp, vp));

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

	public void setPsiMax(double psiMax)
	{
		this.psiMax = psiMax;
	}

	public void setFovVertical(double fov)
	{
		this.psiMax = fov / 2;
	}

	public void setLambdaMax(double lambdaMax)
	{
		this.lambdaMax = lambdaMax;
	}

	public void setFovHorizontal(double fov)
	{
		this.lambdaMax = fov / 2;
	}
}
