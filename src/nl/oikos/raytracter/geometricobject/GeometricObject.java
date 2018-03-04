package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.material.Material;
import nl.oikos.raytracter.util.Ray;
import nl.oikos.raytracter.util.Reference;
import nl.oikos.raytracter.util.ShadeRec;
import sun.security.provider.SHA;

import java.util.function.BiConsumer;

/**
 * Created by Adrian on 31-7-2017.
 */
public abstract class GeometricObject
{

	protected Material material;
	protected boolean shadows;

	public abstract boolean hit(Ray ray, Reference<Double> tmin, ShadeRec sr);

	public boolean shadowHit(Ray ray, Reference<Double> tmin)
	{
		if (!this.shadows)
			return false;

		return this.hit(ray, tmin, null);
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public void setShadows(boolean shadows)
	{
		this.shadows = shadows;
	}
}
