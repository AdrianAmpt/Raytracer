package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.Jittered;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.Regular;
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
public class Image04_01 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 1;

		// view plane
		viewPlane.setWidth(400);
		viewPlane.setHeight(400);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		viewPlane.setMaxDepth(0);

		backgroundColor = RGBColor.DARK_GRAY;

		tracer = new RayCast(this);

		// camera
		Pinhole thinLens = new Pinhole();
		//thinLens.setEye(125, 500, 125);
		//thinLens.setLookat(125, 0, 125);
		thinLens.setEye(0, 500, 0);
		thinLens.setLookat(0, 0, 0);
		thinLens.setViewDistance(175);
		thinLens.computeUVW();
		setCamera(thinLens);

		// lights
		Ambient ambient = new Ambient();
		ambient.scaleRadiance(1);
		setAmbientLight(ambient);

		Directional directional = new Directional();
		directional.setDirection(new Vector3D(0, -1, 0));
		directional.scaleRadiance(4.0);
		directional.setShadows(false);
		addLight(directional);

		// ground plane
		Checker3D checker = new Checker3D();
		checker.setSquareSize(250);
		checker.setColor1(RGBColor.BLACK);
		checker.setColor2(RGBColor.GREEN);

		Matte matte = new Matte();
		matte.setKa(0.5);
		matte.setKd(0.35);
		matte.setCd(checker);

		Plane plane = new Plane(Point3D.O, Normal3D.Y);
		plane.setMaterial(matte);
		addGeometricObject(plane);
	}
}
