package nl.oikos.raytracter.tracer;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Ray;
import nl.oikos.raytracter.util.Reference;
import nl.oikos.raytracter.util.ShadeRec;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 21-8-2017.
 */
public class AreaLighting extends Tracer
{

	public AreaLighting(World world)
	{
		super(world);
	}

	@Override
	public RGBColor trace(final Ray ray)
	{
		ShadeRec sr = world.hitObjects(ray);

		if (sr.hitAnObject) {
			sr.ray = ray;			// used for specular shading
			return sr.material.areaLightShade(sr);
		}
		else
			return world.backgroundColor;
	}

	@Override
	public RGBColor trace(final Ray ray, final int depth)
	{
		return this.trace(ray);
	}

	@Override
	public RGBColor trace(final Ray ray, final int depth, Reference<Integer> count, Reference<Integer> jump)
	{
		ShadeRec sr = world.hitObjects(ray);

		sr.count = count.get();
		sr.jump = jump.get();

		if (sr.hitAnObject)
		{
			sr.ray = ray;			// used for specular shading
			return sr.material.areaLightShade(sr);
		}
		else
			return world.backgroundColor;
	}

}
