package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.OpenCylinder;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.light.PointLight;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 10-3-2018.
 */
public class ImageTestOpenCylinder extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 16;

		// view plane

		Sampler sampler = new MultiJittered(numberOfSamples);

		viewPlane.setWidth(1600);
		viewPlane.setHeight(1600);
		viewPlane.setSampler(sampler);

		backgroundColor = RGBColor.BLACK;

		tracer = new RayCast(this);

		// camera
		Pinhole pinHole = new Pinhole();
		pinHole.setEye(new Point3D(0, 5, 10));
		pinHole.setLookat(new Point3D(0, 0, 0));
		pinHole.setViewDistance(1200);
		pinHole.computeUVW();
		setCamera(pinHole);

		PointLight light = new PointLight();
		light.setLocation(new Point3D(10, 13, 20));
		light.scaleRadiance(3);
		light.setShadows(true);
		addLight(light);

		Matte matte = new Matte();
		matte.setKa(0.3);
		matte.setKd(0.6);
		matte.setCd(new RGBColor(1, 1, 0));

		double y0 = 0.5;
		double y1 = 1.0;
		double radius = 1.0;

		OpenCylinder cylinder = new OpenCylinder(y0, y1, radius);
		cylinder.setMaterial(matte);
		addGeometricObject(cylinder);

		// ground plane with checker:

		Checker3D checker = new Checker3D();
		checker.setSquareSize(1.0);
		checker.setColor1(0.8);
		checker.setColor2(1);

		Matte matte2 = new Matte();
		matte2.setKa(0.30);
		matte2.setKd(0.6);
		matte2.setCd(checker);

		Plane plane = new Plane(new Point3D(0, -2, 0), new Normal3D(0, 1, 0));
		plane.setMaterial(matte2);
		addGeometricObject(plane);
	}
}
