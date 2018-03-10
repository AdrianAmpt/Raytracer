package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.FishEye;
import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.AABox;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.light.PointLight;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 7-3-2018.
 */
public class Image16_05 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 25; // use 9 or 16 samples for production image

		// view plane

		viewPlane.setWidth(600);
		viewPlane.setHeight(400);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		//viewPlane.setShowOutOfGamut(true);
		//viewPlane.setMaxDepth(0);

		backgroundColor = RGBColor.WHITE;

		tracer = new RayCast(this);

		// camera
		Pinhole pinHole = new Pinhole();
		pinHole.setEye(new Point3D(350, 1000, 500));
		pinHole.setLookat(new Point3D(-175, 550, -40));
		pinHole.setViewDistance(350);
		pinHole.computeUVW();
		setCamera(pinHole);

		// lights
		PointLight light1 = new PointLight();
		light1.setLocation(new Point3D(1500, 750, 250));
		light1.scaleRadiance(4.5);
		light1.setShadows(true);
		addLight(light1);

		Matte matte1 = new Matte();
		matte1.setCd(new RGBColor(0, 0.5, 0.5));
		matte1.setKa(0.4);
		matte1.setKd(0.5);

		Matte matte2 = new Matte();
		matte2.setCd(new RGBColor(1, 0, 0));
		matte2.setKa(0.4);
		matte2.setKd(0.5);

		Matte matte3 = new Matte();
		matte3.setCd(new RGBColor(0.5, 0.6, 0));
		matte3.setKa(0.4);
		matte3.setKd(0.5);

		int numberOfBoxes = 11;
		double wx = 50;
		double wz = 50;
		double h = 300;
		double s = 100;

		for (int j = 0; j < numberOfBoxes; j++)
		{
			AABox box = new AABox(new Point3D(-wx, 0, -(j + 1) * wz - j * s),
					new Point3D(0, h, -j * wz - j * s));

			box.setMaterial(matte1);
			addGeometricObject(box);
		}

		h = 450;

		for (int j = 0; j < numberOfBoxes; j++)
		{
			AABox box = new AABox(new Point3D(-wx -wx -s, 0, -(j + 1) * wz - j * s),
					new Point3D(-wx - s, h, -j * wz - j * s));

			box.setMaterial(matte2);
			addGeometricObject(box);
		}

		h = 600;

		for (int j = 0; j < numberOfBoxes; j++)
		{
			AABox box = new AABox(new Point3D(-wx - 2 * wx - 2 * s, 0, -(j + 1) * wz - j * s),
					new Point3D(-2 * wx - 2 * s, h, -j * wz - j * s));

			box.setMaterial(matte3);
			addGeometricObject(box);
		}

		// box 1
		Checker3D checker1 = new Checker3D();
		checker1.setSquareSize(2 * wx);
		checker1.setColor1(new RGBColor(0.7));
		checker1.setColor2(RGBColor.WHITE);

		Matte svMatte1 = new Matte();
		svMatte1.setKa(0.15);
		svMatte1.setKd(1.0);
		svMatte1.setCd(checker1);

		Plane plane = new Plane(Point3D.Y, Normal3D.Y);
		plane.setMaterial(svMatte1);
		addGeometricObject(plane);
	}
}
