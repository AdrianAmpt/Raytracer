package nl.oikos.raytracter.util;

import nl.oikos.raytracter.material.Material;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 1-8-2017.
 */
public class ShadeRec
{

	public boolean hitAnObject;
	public Material material;
	public Point3D hitPoint;
	public Point3D localHitPoint;
	public Normal3D normal;
	public Ray ray;
	public int depth;
	public double t;
	public World world;

	public Integer count;
	public Integer jump;
	public boolean sync;

	public ShadeRec(World world)
	{
		this.hitAnObject = false;
		this.material = null;
		this.hitPoint = Point3D.O;
		this.localHitPoint = Point3D.O;
		this.normal = Normal3D.Z;
		this.ray = new Ray();
		this.depth = 0;
		this.t = 0;
		this.world = world;

		this.count = 0;
		this.jump = 0;
		this.sync = false;
	}

	public ShadeRec(ShadeRec shadeRec)
	{
		this.hitAnObject = shadeRec.hitAnObject;
		this.material = shadeRec.material;
		this.hitPoint = shadeRec.hitPoint;
		this.localHitPoint = shadeRec.localHitPoint;
		this.normal = shadeRec.normal;
		this.ray = shadeRec.ray;
		this.depth = shadeRec.depth;
		this.t = shadeRec.t;
		this.world = shadeRec.world;

		this.count = shadeRec.count;
		this.jump = shadeRec.jump;
		this.sync = false;
	}
}
