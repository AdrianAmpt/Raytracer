package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.AABox;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.geometricobject.Rectangle;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.AmbientOccluder;
import nl.oikos.raytracter.light.AreaLight;
import nl.oikos.raytracter.material.Emissive;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 10-3-2018.
 */
public class Image18_04 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 100; // use 9 or 16 samples for production image

		// view plane

		Sampler sampler = new MultiJittered(numberOfSamples);

		viewPlane.setWidth(600);
		viewPlane.setHeight(600);
		viewPlane.setSampler(sampler);

		backgroundColor = RGBColor.GRAY;

		tracer = new RayCast(this);

		// camera
		Pinhole pinHole = new Pinhole();
		pinHole.setEye(new Point3D(-20, 10, 20));
		pinHole.setLookat(new Point3D(0, 2, 0));
		pinHole.setViewDistance(1080);
		pinHole.computeUVW();
		setCamera(pinHole);

		Emissive emissive = new Emissive();
		emissive.scaleRadiance(40);
		emissive.setColor(RGBColor.WHITE);

		// define a rectangle for the rectangular light

		double width = 4.0;				// for Figure 18.4(a) & (b)
		double height = 4.0;
		//width = 2.0;				// for Figure 18.4(c)
		//height = 2.0;
		Point3D center = new Point3D(0.0, 7.0, -7.0);	// center of each area light (rectangular, disk, and spherical)
		Point3D p0 = new Point3D(-0.5 * width, center.y - 0.5 * height, center.z);
		Vector3D a = new Vector3D(width, 0.0, 0.0);
		Vector3D b = new Vector3D(0.0, height, 0.0);
		Normal3D normal = new Normal3D(0, 0, 1);

		Rectangle rectangle = new Rectangle(p0, a, b, normal);
		rectangle.setMaterial(emissive);
		rectangle.setSampler(sampler);
		rectangle.setShadows(false);
		addGeometricObject(rectangle);

		AreaLight areaLight = new AreaLight();
		areaLight.setMaterial(emissive);
		areaLight.setObject(rectangle);
		areaLight.setShadows(true);
		addLight(areaLight);

		double boxWidth = 1.0;
		double boxDepth = 1.0;
		double boxHeight = 4.5;
		double gap = 3.0;

		Matte matte1 = new Matte();
		matte1.setCd(new RGBColor(0.4, 0.7, 0.4));
		matte1.setKa(0.25);
		matte1.setKd(0.75);

		AABox box0 = new AABox(new Point3D(-1.5 * gap - 2.0 * boxWidth, 0.0, -0.5 * boxDepth),
							   new Point3D(-1.5 * gap  - boxWidth, boxHeight, 0.5 * boxDepth));
		box0.setMaterial(matte1);
		addGeometricObject(box0);

		AABox box1 = new AABox(new Point3D(- 0.5 * gap - boxWidth, 0.0, -0.5 * boxDepth),
							   new Point3D(-0.5 * gap, boxHeight, 0.5 * boxDepth));
		box1.setMaterial(matte1);
		addGeometricObject(box1);

		AABox box2 = new AABox(new Point3D(0.5 * gap, 0.0, -0.5 * boxDepth),
							   new Point3D(0.5 * gap + boxWidth, boxHeight, 0.5 * boxDepth));
		box2.setMaterial(matte1);
		addGeometricObject(box2);

		AABox box3 = new AABox(new Point3D(1.5 * gap + boxWidth, 0.0, -0.5 * boxDepth),
							   new Point3D(1.5 * gap + 2.0 * boxWidth, boxHeight, 0.5 * boxDepth));
		box3.setMaterial(matte1);
		addGeometricObject(box3);

		Matte matte2 = new Matte();
		matte2.setCd(RGBColor.WHITE);
		matte2.setKa(0.1);
		matte2.setKd(0.9);

		Plane plane = new Plane(Point3D.O, Normal3D.Y);
		plane.setMaterial(matte2);
		addGeometricObject(plane);
	}
}
