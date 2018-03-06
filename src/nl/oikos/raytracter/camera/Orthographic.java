package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.ViewPlane;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 3-9-2017.
 */
public class Orthographic  extends Camera
{
	public Orthographic()
	{
		super();
	}

	@Override
	public RenderedPixel renderStereo(World world, Pixel pixel, double xOffset)
	{
		RGBColor L = RGBColor.BLACK;
		Ray ray = new Ray();
		ViewPlane vp = new ViewPlane(world.viewPlane);
		double zw = 100;
		int n = (int) Math.sqrt(vp.sampler.getNumberOfSamples());
		Point2D pp = new Point2D(0);

		ray.d = new Point3D(0,0,-1);
		for (int p = 0; p < n; p++)
		{
			for (int q = 0; q < n; q++)
			{
				pp.x = vp.pixelSize * (pixel.x - 0.5 * vp.width + (q + 0.5) / n) + xOffset;
				pp.y = vp.pixelSize * (pixel.y - 0.5 * vp.width + (p + 0.5) / n);
				ray.o = new Point3D(pp.x, pp.y, zw);
				L = L.add(world.tracer.trace(ray));
			}
		}

		L = L.divide(vp.sampler.getNumberOfSamples());

		return world.displayPixel(pixel, L);
	}
}