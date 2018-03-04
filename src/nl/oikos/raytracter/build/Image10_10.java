package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.ThinLens;
import nl.oikos.raytracter.geometricobject.AABox;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.SpatialVaryingMatte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 1-9-2017.
 */
public class Image10_10 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 100; // use 9 or 16 samples for production image

		// view plane

		viewPlane.setWidth(800);
		viewPlane.setHeight(600);
		viewPlane.setPixelSize(0.05);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		viewPlane.setShowOutOfGamut(true);
		viewPlane.setMaxDepth(0);

		backgroundColor = RGBColor.WHITE;

		tracer = new RayCast(this);

		// camera
		ThinLens thinLens = new ThinLens();
		thinLens.setLensSampler(new MultiJittered(numberOfSamples));
		thinLens.setEye(0, 6, 50);
		thinLens.setLookat(0, 6, 0);
		thinLens.setViewDistance(40);
		//thinLens.setFocalDistance(50);
		thinLens.setFocalDistance(74);
		//thinLens.setFocalDistance(98);
		thinLens.setLensRadius(1);
		thinLens.computeUVW();
		setCamera(thinLens);


		// lights

		Ambient ambient = new Ambient();
		ambient.scaleRadiance(0.5);
		setAmbientLight(ambient);

		Directional directional = new Directional();
		directional.setDirection(new Vector3D(1, 1, 1));
		directional.scaleRadiance(7.5);
		directional.setShadows(true);
		addLight(directional);

		// Matte material reflection coefficients - common to all box materials
		double ka = 0.5;
		double kd = 0.35;

		// box 1
		Checker3D checker1 = new Checker3D();
		checker1.setSquareSize(2);
		checker1.setColor1(new RGBColor(1, 1, 0.33)); // lemon
		checker1.setColor2(RGBColor.BLACK);

		SpatialVaryingMatte matte1 = new SpatialVaryingMatte();
		matte1.setKa(ka);
		matte1.setKd(kd);
		matte1.setCd(checker1);

		AABox box1 = new AABox(new Point3D(-9, 0, -1), new Point3D(-3, 12, 0));
		box1.setMaterial(matte1);
		addGeometricObject(box1);

		// box 2
		Checker3D checker2 = new Checker3D();
		checker2.setSquareSize(2);
		checker2.setColor1(RGBColor.BLACK);
		checker2.setColor2(new RGBColor(0.1, 1, 0.5)); // green

		SpatialVaryingMatte matte2 = new SpatialVaryingMatte();
		matte2.setKa(ka);
		matte2.setKd(kd);
		matte2.setCd(checker2);

		AABox box2 = new AABox(new Point3D(-3.25, 0, -25), new Point3D(4.75, 14, -24));
		box2.setMaterial(matte2);
		addGeometricObject(box2);

		// box 3
		Checker3D checker3 = new Checker3D();
		checker3.setSquareSize(2);
		checker3.setColor1(RGBColor.BLACK);
		checker3.setColor2(new RGBColor(1, 0.6, 0.15)); // orange

		SpatialVaryingMatte matte3 = new SpatialVaryingMatte();
		matte3.setKa(ka);
		matte3.setKd(kd);
		matte3.setCd(checker3);

		AABox box3 = new AABox(new Point3D(8, 0, -49), new Point3D(18, 15, -48));
		box3.setMaterial(matte3);
		addGeometricObject(box3);

		// ground plane
		Checker3D checker = new Checker3D();
		checker.setSquareSize(8);
		checker.setColor1(new RGBColor(0.25)); // gray
		checker.setColor2(RGBColor.WHITE);

		SpatialVaryingMatte matte = new SpatialVaryingMatte();
		matte.setKa(0.5);
		matte.setKd(0.35);
		matte.setCd(checker);

		Plane plane = new Plane(Point3D.O, Normal3D.Y);
		plane.setMaterial(matte);
		addGeometricObject(plane);
	}
}
