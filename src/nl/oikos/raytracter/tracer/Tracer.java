package nl.oikos.raytracter.tracer;

import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Ray;
import nl.oikos.raytracter.util.Reference;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 31-7-2017.
 */
public abstract class Tracer
{

	protected World world;

	public Tracer(World world)
	{
		this.world = world;
	}

	public RGBColor trace(final Ray ray)
	{
		return RGBColor.BLACK;
	}

	public RGBColor trace(final Ray ray, final int depth)
	{
		return RGBColor.BLACK;
	}

	public RGBColor trace(final Ray ray, Reference<Double> tmin, final int depth)
	{
		return RGBColor.BLACK;
	}

	public RGBColor trace(final Ray ray, final int depth, Reference<Integer> count, Reference<Integer> jump)
	{
		return RGBColor.BLACK;
	}
}
