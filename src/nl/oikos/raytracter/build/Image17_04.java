package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.FishEye;
import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.AABox;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.AmbientOccluder;
import nl.oikos.raytracter.light.PointLight;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.Regular;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 9-3-2018.
 */
public class Image17_04 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 1; // use 9 or 16 samples for production image

		// view plane

		Sampler sampler = new Regular(numberOfSamples);

		viewPlane.setWidth(400);
		viewPlane.setHeight(400);
		viewPlane.setSampler(sampler);

		backgroundColor = RGBColor.WHITE;

		tracer = new RayCast(this);

		AmbientOccluder occluder = new AmbientOccluder();
		occluder.scaleRadiance(1);
		occluder.setMinAmount(RGBColor.BLACK);
		occluder.setSampler(sampler);
		setAmbientLight(occluder);

		// camera
		Pinhole pinHole = new Pinhole();
		pinHole.setEye(new Point3D(25, 20, -45));
		pinHole.setLookat(new Point3D(0, 1, 0));
		pinHole.setViewDistance(5000);
		pinHole.computeUVW();
		setCamera(pinHole);

		Matte matte1 = new Matte();
		matte1.setCd(new RGBColor(1, 1, 0));
		matte1.setKa(0.75);
		matte1.setKd(0);

		Sphere sphere = new Sphere(new Point3D(0, 1, 0), 1);
		sphere.setMaterial(matte1);
		addGeometricObject(sphere);

		Matte matte2 = new Matte();
		matte2.setCd(RGBColor.WHITE);
		matte2.setKa(0.75);
		matte2.setKd(0);

		Plane plane = new Plane(Point3D.O, Normal3D.Y);
		plane.setMaterial(matte2);
		addGeometricObject(plane);
	}
}
